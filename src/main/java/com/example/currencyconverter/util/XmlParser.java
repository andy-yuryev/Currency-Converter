package com.example.currencyconverter.util;

import com.example.currencyconverter.dto.CurrenciesDto;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class XmlParser {

    @Value("${cbr.url}")
    private String cbrUrl;

    public CurrenciesDto parse() {
        CurrenciesDto currenciesDto = new CurrenciesDto();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = dtf.format(LocalDate.now());

        try {
            URL url = new URL(cbrUrl);
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
