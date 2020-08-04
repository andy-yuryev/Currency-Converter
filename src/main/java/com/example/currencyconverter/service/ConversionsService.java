package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.Conversion;
import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.Rate;
import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.repository.ConversionRepository;
import com.example.currencyconverter.repository.CurrencyRepository;
import com.example.currencyconverter.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

@Service
public class ConversionsService {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RateRepository rateRepository;

    @Autowired
    private ConversionRepository conversionRepository;

    @Autowired
    private CurrencyService currencyService;

    public BigDecimal convert(BigDecimal amount, String sourceCurrencyId, String targetCurrencyId, User user) {
        Date lastRateDate = rateRepository.findLastRateDate();
        Date currentDate = new Date(System.currentTimeMillis());

        if (lastRateDate == null || !lastRateDate.toLocalDate().equals(currentDate.toLocalDate())) {
            currencyService.loadCurrenciesFromXml();
        }

        Rate sourceCurrencyRate = rateRepository.findByCurrencyIdAndDate(sourceCurrencyId, currentDate);
        Rate targetCurrencyRate = rateRepository.findByCurrencyIdAndDate(targetCurrencyId, currentDate);
        BigDecimal sourceValue = sourceCurrencyRate.getValue();
        BigDecimal targetValue = targetCurrencyRate.getValue();
        int sourceNominal = sourceCurrencyRate.getNominal();
        int targetNominal = targetCurrencyRate.getNominal();

        BigDecimal convertedAmount = sourceValue
                .divide(BigDecimal.valueOf(sourceNominal), RoundingMode.HALF_UP)
                .multiply(amount)
                .divide(targetValue, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(targetNominal));

        Currency sourceCurrency = currencyRepository.findById(sourceCurrencyId);
        Currency targetCurrency = currencyRepository.findById(targetCurrencyId);
        Conversion conversion = new Conversion(sourceCurrency, targetCurrency, amount, convertedAmount,
                new Date(System.currentTimeMillis()), user);
        conversionRepository.save(conversion);

        return convertedAmount;
    }

    public List<Conversion> getAllConversionsByUser(User user) {
        return conversionRepository.findAllByUser(user);
    }

    public List<Conversion> getAllConversionsByUserAndDate(User user, Date date) {
        return conversionRepository.findAllByUserAndDate(user, date);
    }
}
