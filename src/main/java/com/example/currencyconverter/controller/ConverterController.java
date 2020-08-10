package com.example.currencyconverter.controller;

import com.example.currencyconverter.domain.Conversion;
import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.service.ConverterService;
import com.example.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/")
public class ConverterController {

    @Autowired
    private ConverterService converterService;

    @Autowired
    private CurrencyService currencyService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCurrenciesOnStartup() {
        currencyService.loadCurrenciesFromCbr(LocalDate.now());
    }

    @GetMapping
    public String converter(Model model) {
        List<Currency> currencies = currencyService.getAllCurrencies();

        model.addAttribute("currencies", currencies);
        model.addAttribute("sourceCurrencyId", "R00001");
        model.addAttribute("targetCurrencyId", "R01235");
        model.addAttribute("date", LocalDate.now());
        model.addAttribute("maxDate", LocalDate.now());

        return "converter";
    }

    @PostMapping
    public String converter(
            @RequestParam String amount,
            @RequestParam String sourceCurrencyId,
            @RequestParam String targetCurrencyId,
            @RequestParam String date,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        List<Currency> currencies = currencyService.getAllCurrencies();

        BigDecimal originalAmount = null;
        BigDecimal convertedAmount = null;

        if (amount != null && !amount.isEmpty()) {
            originalAmount = new BigDecimal(amount.replace(",", "."));
            convertedAmount = converterService.convert(originalAmount, sourceCurrencyId, targetCurrencyId, LocalDate.parse(date));
            Currency sourceCurrency = currencyService.getCurrencyById(sourceCurrencyId);
            Currency targetCurrency = currencyService.getCurrencyById(targetCurrencyId);
            Conversion conversion = new Conversion(sourceCurrency, targetCurrency, originalAmount, convertedAmount, LocalDate.parse(date), user);
            converterService.addConversion(conversion);
        }

        model.addAttribute("currencies", currencies);
        model.addAttribute("sourceCurrencyId", sourceCurrencyId);
        model.addAttribute("targetCurrencyId", targetCurrencyId);
        model.addAttribute("amount", originalAmount);
        model.addAttribute("convertedAmount", convertedAmount);
        model.addAttribute("date", date);
        model.addAttribute("maxDate", LocalDate.now());

        return "converter";
    }
}
