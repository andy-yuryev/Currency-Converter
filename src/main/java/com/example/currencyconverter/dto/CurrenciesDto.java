package com.example.currencyconverter.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "ValCurs")
public class CurrenciesDto {

    @JacksonXmlProperty(localName = "Date", isAttribute = true)
    private String date;

    @JacksonXmlProperty(localName = "name", isAttribute = true)
    private String name;

    @JacksonXmlElementWrapper(localName = "Valute", useWrapping = false)
    @JacksonXmlProperty(localName = "Valute")
    private List<CurrencyDto> currencyDtoList;

    public List<CurrencyDto> getCurrencyList() {
        return currencyDtoList;
    }

    public void setCurrencyList(List<CurrencyDto> currencyDtoList) {
        this.currencyDtoList = currencyDtoList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
