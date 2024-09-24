package project.service.interfaces;

import java.io.IOException;
import java.util.Map;

public interface CurrencyExchangeService {
    Map<String, String> getCurrencyCodes() throws IOException;
    Map<String, String> getLatestCurrencyRates(String baseCurrency);
    double getConvertedCurrencyAmount(String baseCurrency, String targetCurrency, double amount);
}
