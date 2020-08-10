package com.example.currencyconverter.controller;

import com.example.currencyconverter.domain.Conversion;
import com.example.currencyconverter.domain.Currency;
import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.service.ConverterService;
import com.example.currencyconverter.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/history")
public class ConversionController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private ConverterService converterService;

    @GetMapping
    public String history(
            @RequestParam(required = false) String sourceCurrencyId,
            @RequestParam(required = false) String targetCurrencyId,
            @RequestParam(required = false) String date,
            @AuthenticationPrincipal User user,
            Model model
    ) {
        List<Currency> currencies = currencyService.getAllCurrencies();
        List<Conversion> conversions;

        if (date == null || date.isEmpty()) {
            conversions = converterService.getAllConversionsByUser(user);
        } else {
            conversions = converterService.getAllConversionsByUserAndDate(user, LocalDate.parse(date));
            model.addAttribute("date", date);
        }

        if (sourceCurrencyId != null && !sourceCurrencyId.equals("0")) {
            conversions = conversions.stream()
                    .filter(conversion -> conversion.getSourceCurrency().getId().equals(sourceCurrencyId))
                    .collect(Collectors.toList());
            model.addAttribute("sourceCurrencyId", sourceCurrencyId);
        }

        if (targetCurrencyId != null && !targetCurrencyId.equals("0")) {
            conversions = conversions.stream()
                    .filter(conversion -> conversion.getTargetCurrency().getId().equals(targetCurrencyId))
                    .collect(Collectors.toList());
            model.addAttribute("targetCurrencyId", targetCurrencyId);
        }

        model.addAttribute("currencies", currencies);
        model.addAttribute("conversions", conversions);

        return "history";
    }
}
