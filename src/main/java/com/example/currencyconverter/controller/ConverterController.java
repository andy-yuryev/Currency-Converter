package com.example.currencyconverter.controller;

import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.service.ConversionsService;
import com.example.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/")
public class ConverterController {

    @Autowired
    private ConversionsService conversionsService;

    @Autowired
    private CurrencyService currencyService;

    @EventListener(ApplicationReadyEvent.class)
    public void loadCurrenciesOnStartup() {
        currencyService.loadCurrenciesFromXml();
    }

    @GetMapping
    public String converter(Model model) {
        List<Currency> currencies = currencyService.getAllCurrencies();

        model.addAttribute("currencies", currencies);
        model.addAttribute("sourceCurrencyId", "R00001");
        model.addAttribute("targetCurrencyId", "R00001");

        return "converter";
    }

    @PostMapping
    public String converter(
            @RequestParam BigDecimal amount,
            @RequestParam String sourceCurrencyId,
            @RequestParam String targetCurrencyId,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        List<Currency> currencies = currencyService.getAllCurrencies();
        Currency sourceCurrency = currencyService.getCurrencyById(sourceCurrencyId);
        Currency targetCurrency = currencyService.getCurrencyById(targetCurrencyId);

        BigDecimal convertedAmount = conversionsService.convert(amount, sourceCurrencyId, targetCurrencyId, user);

        model.addAttribute("currencies", currencies);
        model.addAttribute("sourceCurrencyId", sourceCurrencyId);
        model.addAttribute("targetCurrencyId", targetCurrencyId);
        model.addAttribute("amount", amount);
        model.addAttribute("convertedAmount", convertedAmount);

        return "converter";
    }
}
