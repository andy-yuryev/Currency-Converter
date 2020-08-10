package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.Conversion;
import com.example.currencyconverter.domain.Rate;
import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.repository.ConversionRepository;
import com.example.currencyconverter.repository.CurrencyRepository;
import com.example.currencyconverter.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class ConverterService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private CurrencyService currencyService;

    public BigDecimal convert(BigDecimal amount, String sourceCurrencyId, String targetCurrencyId, LocalDate date) {
        boolean dbContainsDate = rateRepository.findRateDate(date);

        if (!dbContainsDate) {
            currencyService.loadCurrenciesFromCbr(date);
        }

        Rate sourceCurrencyRate = rateRepository.findByCurrencyIdAndDate(sourceCurrencyId, date);
        Rate targetCurrencyRate = rateRepository.findByCurrencyIdAndDate(targetCurrencyId, date);
        BigDecimal sourceValue = sourceCurrencyRate.getValue();
        BigDecimal targetValue = targetCurrencyRate.getValue();
        int sourceNominal = sourceCurrencyRate.getNominal();
        int targetNominal = targetCurrencyRate.getNominal();

        return sourceValue
                .divide(BigDecimal.valueOf(sourceNominal), RoundingMode.HALF_UP)
                .multiply(amount)
                .divide(targetValue, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(targetNominal)).setScale(2, RoundingMode.HALF_UP);
    }

    public List<Conversion> getAllConversionsByUser(User user) {
        return conversionRepository.findAllByUser(user);
    }

    public List<Conversion> getAllConversionsByUserAndDate(User user, LocalDate date) {
        return conversionRepository.findAllByUserAndDate(user, date);
    }

    public void addConversion(Conversion conversion) {
        conversionRepository.save(conversion);
    }
}
