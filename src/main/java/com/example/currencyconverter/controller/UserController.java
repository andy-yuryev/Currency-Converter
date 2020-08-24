package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.UserDto;
import com.example.currencyconverter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registration")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String registerUser(@Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                    fieldError -> fieldError.getField() + "Error",
                    FieldError::getDefaultMessage
            );
            Map<String, String> errors = bindingResult.getFieldErrors().stream().collect(collector);
            model.mergeAttributes(errors);
            return "registration";
        }

        if (!userService.registerUser(user)) {
            model.addAttribute("usernameError", "Логин занят");
            return "registration";
        }

        return "redirect:/login";
    }
}
