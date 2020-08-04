package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.Rate;
import com.example.currencyconverter.dto.CurrenciesDto;
import com.example.currencyconverter.dto.CurrencyDto;
import com.example.currencyconverter.repository.CurrencyRepository;
import com.example.currencyconverter.repository.RateRepository;
import com.example.currencyconverter.util.Utils;
import com.example.currencyconverter.util.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class CurrencyService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RateRepository rateRepository;

    public void loadCurrenciesFromXml() {
        XmlParser parser = new XmlParser();

        CurrenciesDto currenciesDto = parser.parse();
        List<CurrencyDto> currencyList = currenciesDto.getCurrencyList();

        Date lastRateDate = rateRepository.findLastRateDate();
        Date currentDate = new Date(System.currentTimeMillis());

        if (lastRateDate != null && lastRateDate.toLocalDate().equals(currentDate.toLocalDate())) {
            return;
        }

        Currency rubleCurrency = new Currency("R00001", "643", "RUB", "Российский рубль");
        Rate rubleRate = new Rate(rubleCurrency, 1, new BigDecimal(1), currentDate);

        currencyRepository.save(rubleCurrency);
        rateRepository.save(rubleRate);

        for (CurrencyDto currencyDto : currencyList) {
            Currency currency = Utils.convertCurrencyDtoToCurrency(currencyDto);
            Rate rate = Utils.convertCurrencyDtoToRate(currencyDto);
            currencyRepository.save(currency);
            rateRepository.save(rate);
        }
    }

    public Currency getCurrencyById(String currencyId) {
        return currencyRepository.findById(currencyId);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}
