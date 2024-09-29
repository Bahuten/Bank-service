package project.service.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.service.interfaces.CurrencyExchangeService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static project.utility.CommonUtils.*;

@Slf4j
@Service
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final DecimalFormat decimalFormat;
    private final String API_KEY;
    private final String API_URL;
    private HttpURLConnection httpConnection;
    private URL exchangeRateApi;
    private String PAIR_API_ENDPOINT;
    private String LATEST_RATES_API_ENDPOINT;

    @Autowired
    public CurrencyExchangeServiceImpl(@Value("${exchange.rate.api.key}") String apiKey, 
        @Value("${exchange.rate.api.url}") String apiUrl) throws IOException {
        this.API_KEY = apiKey;
        this.API_URL = apiUrl;
        this.PAIR_API_ENDPOINT = API_URL + API_KEY + PAIR_ENDPOINT + "/%s" + "/%s" + "/%s";
        this.LATEST_RATES_API_ENDPOINT = API_URL + API_KEY + LATEST_ENDPOINT + "/%s";
        this.decimalFormat = new DecimalFormat("0.00");
    }

    @Override
    public Map<String, String> getCurrencyCodes() {
        JsonArray jsonArray;
        Map<String, String> supportedCodes = new HashMap<>();
        try {
            String SUPPORTED_CODES_ENDPOINT = API_URL + API_KEY + CODES_ENDPOINT;
            exchangeRateApi = new URL(SUPPORTED_CODES_ENDPOINT);
            httpConnection = (HttpURLConnection) exchangeRateApi.openConnection();
            httpConnection.connect();

            JsonElement element = new JsonParser().parse(new InputStreamReader((InputStream) httpConnection.getContent()));
            JsonObject jsonObj = element.getAsJsonObject();

            if (jsonObj.has(RESULT) && jsonObj.has(SUPPORTED_CODE)) {
                jsonArray = jsonObj.get(SUPPORTED_CODE).getAsJsonArray();
                for (JsonElement code : jsonArray) {
                    String key = code.getAsJsonArray().get(0).toString();
                    String value = code.getAsJsonArray().get(1).toString();
                    supportedCodes.put(key, value);
                }
            }
            log.info(supportedCodes.toString());
        } catch (IOException e) {
            log.error("Error Message: " + e.getMessage() + ", " + "Caused by: " + e.getCause());
        } finally {
            httpConnection.disconnect();
        }
        return supportedCodes;
    }

    @Override
    public Map<String, String> getLatestCurrencyRates(String baseCurrency) {
        JsonObject currencyRatesObject;
        Map<String, String> currencyRates = new HashMap<>();
        LATEST_RATES_API_ENDPOINT = String.format(LATEST_RATES_API_ENDPOINT, baseCurrency);
        try {
            exchangeRateApi = new URL(LATEST_RATES_API_ENDPOINT);
            httpConnection = (HttpURLConnection) exchangeRateApi.openConnection();
            httpConnection.connect();

            JsonElement element = new JsonParser().parse(new InputStreamReader((InputStream) httpConnection.getContent()));
            JsonObject jsonObj = element.getAsJsonObject();

            if (jsonObj.has(RESULT) && jsonObj.has(CONVERSION_RATES)) {
                currencyRatesObject = jsonObj.get(CONVERSION_RATES).getAsJsonObject();
                for (Entry<String, JsonElement> entry : currencyRatesObject.entrySet()) {
                    String key = entry.getKey();
                    String value = valueFormat(entry.getValue().getAsDouble());
                    currencyRates.put(key, value);
                }
            }
            log.info(currencyRates.toString());
        } catch (IOException e) {
            log.error("Error Message: " + e.getMessage() + ", " + "Caused by: " + e.getCause());
        } finally {
            httpConnection.disconnect();
        }
        return currencyRates;
    }

    @Override
    public double getConvertedCurrencyAmount(String baseCurrency, String targetCurrency, double amount) {
        PAIR_API_ENDPOINT = String.format(PAIR_API_ENDPOINT, baseCurrency, targetCurrency, amount);
        double result = 0;
        try {
            log.info("Base currency = " + baseCurrency + ", Target currency = " + targetCurrency + ", Amount to be converted = " + amount);
            exchangeRateApi = new URL(PAIR_API_ENDPOINT);
            httpConnection = (HttpURLConnection) exchangeRateApi.openConnection();
            httpConnection.connect();

            JsonElement element = new JsonParser().parse(new InputStreamReader((InputStream) httpConnection.getContent()));
            JsonObject jsonObj = element.getAsJsonObject();

            if (jsonObj.has(RESULT) && jsonObj.has(CONVERSION_RESULT)) {
                result = jsonObj.get(CONVERSION_RESULT).getAsDouble();
            }
            log.info("Converted result = " + result + " " + targetCurrency + " from " + amount + " " + baseCurrency);
        } catch (IOException e) {
            log.error("Error Message: " + e.getLocalizedMessage() + ", " + "Caused by: " + e.getCause());
        } finally {
            httpConnection.disconnect();
        }
        return result;
    }

    private String valueFormat(double value) {
        return decimalFormat.format(value);
    }
}
