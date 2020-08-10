package com.example.currencyconverter.service;

import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.Rate;
import com.example.currencyconverter.dto.CurrenciesDto;
import com.example.currencyconverter.dto.CurrencyDto;
import com.example.currencyconverter.repository.CurrencyRepository;
import com.example.currencyconverter.repository.RateRepository;
import com.example.currencyconverter.util.Utils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CurrencyService {

    @Value("${cbr.url}")
    private String cbrUrl;

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private RateRepository rateRepository;

    public Currency getCurrencyById(String currencyId) {
        return currencyRepository.findById(currencyId);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }

    public void loadCurrenciesFromCbr(LocalDate date) {
        boolean dbContainsDate = rateRepository.findRateDate(date);

        if (dbContainsDate) {
            return;
        }

        CurrenciesDto currenciesDto = parseXml(date);
        List<CurrencyDto> currencyList = currenciesDto.getCurrencyList();

        Currency rubleCurrency = new Currency("R00001", "643", "RUB", "Российский рубль");
        Rate rubleRate = new Rate(rubleCurrency, 1, new BigDecimal(1), date);
        currencyRepository.save(rubleCurrency);
        rateRepository.save(rubleRate);

        for (CurrencyDto currencyDto : currencyList) {
            Currency currency = Utils.convertCurrencyDtoToCurrency(currencyDto);
            Rate rate = Utils.convertCurrencyDtoToRate(currencyDto);
            rate.setDate(date);
            currencyRepository.save(currency);
            rateRepository.save(rate);
        }
    }

    private CurrenciesDto parseXml(LocalDate date) {
        CurrenciesDto currenciesDto = new CurrenciesDto();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String requiredDate = dtf.format(LocalDate.parse(String.valueOf(date)));

        try {
            URL url = new URL(cbrUrl + requiredDate);
            URLConnection urlConnection = url.openConnection();
            InputStream is = new BufferedInputStream(urlConnection.getInputStream());
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            XMLStreamReader xmlStreamReader = xmlInputFactory.createXMLStreamReader(is);
            XmlMapper mapper = new XmlMapper();
            currenciesDto = mapper.readValue(xmlStreamReader, CurrenciesDto.class);
        } catch (IOException | XMLStreamException ex) {
            System.out.print(ex.getMessage());
        }

        return currenciesDto;
    }
}
