package com.example.currencyconverter.util;

import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.Rate;
import com.example.currencyconverter.dto.CurrencyDto;

import java.math.BigDecimal;

public class Utils {

    public static Currency convertCurrencyDtoToCurrency(CurrencyDto currencyDto) {
        Currency currency = new Currency();
        currency.setId(currencyDto.getId());
        currency.setNumCode(currencyDto.getNumCode());
        currency.setCharCode(currencyDto.getCharCode());
        currency.setName(currencyDto.getName());
        return currency;
    }

    public static Rate convertCurrencyDtoToRate(CurrencyDto currencyDto) {
        Rate rate = new Rate();
        Currency currency = convertCurrencyDtoToCurrency(currencyDto);
        rate.setCurrency(currency);
        rate.setNominal(currencyDto.getNominal());
        rate.setValue(new BigDecimal(currencyDto.getValue().replace(",", ".")));
        return rate;
    }
}
