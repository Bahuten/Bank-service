package project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import project.service.impl.CurrencyExchangeServiceImpl;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class CurrencyExchangeServiceImplTest {
    private CurrencyExchangeServiceImpl currencyExchangeServiceImpl;

    @BeforeEach
    public void setup() throws IOException {
        currencyExchangeServiceImpl = new CurrencyExchangeServiceImpl();
    }

    @Test
    public void getConvertedAmountTest() {
        String baseCurrency = "USD";
        String targetCurrency = "GBP";
        double amount = 20.00;

        boolean actualResult = currencyExchangeServiceImpl.getConvertedCurrencyAmount(baseCurrency, targetCurrency, amount) != 0;
        assertTrue(actualResult);
    }

    @Test
    public void getConvertedAmountTestFailed() {
        String baseCurrency = "USD";
        String targetCurrency = "GBP";
        double amount = -20.00;

        boolean actualResult = currencyExchangeServiceImpl.getConvertedCurrencyAmount(baseCurrency, targetCurrency, amount) != 0;
        assertFalse(actualResult);
    }

    @Test
    public void getAllSupportedCurrencyCodes() {
        int size = 162;
        int actualSupportedCodes = currencyExchangeServiceImpl.getCurrencyCodes().size();
        assertEquals(size, actualSupportedCodes);
    }

    @Test
    public void getAllLatestCurrencyRatesFromBaseCurrency() {
        String baseCurrency = "USD";
        int expectedSize = 162;

        int actualApiResponseSize = currencyExchangeServiceImpl.getLatestCurrencyRates(baseCurrency).size();
        assertEquals(expectedSize, actualApiResponseSize);
    }

    @Test
    public void getAllLatestCurrencyRatesFromBaseCurrencyFailed() {
        String baseCurrency = "AAA";
        int expectedSize = 0;

        int actualApiResponseSize = currencyExchangeServiceImpl.getLatestCurrencyRates(baseCurrency).size();
        assertEquals(expectedSize, actualApiResponseSize);
    }
}
