package com.example.currencyconverter.controller;

import com.example.currencyconverter.dto.UserDto;
import com.example.currencyconverter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public String registerUser(@Valid UserDto userDto) {
        userService.registerUser(userDto.getUsername(), userDto.getPassword());
        return "redirect:/login";
    }
}
