package com.example.currencyconverter.controller;

import com.example.currencyconverter.domain.User;
import com.example.currencyconverter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String registerUser(@RequestParam User user, Model model) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "Логин не может быть пустым");
            return "registration";
        }
        if (password == null || password.isEmpty()) {
            model.addAttribute("error", "Пароль не может быть пустым");
            return "registration";
        }
        boolean registrationSuccessful = userService.registerUser(username, password);
        if (!registrationSuccessful) {
            model.addAttribute("error", "Логин занят");
            return "registration";
        }
        return "redirect:/login";
    }
}
