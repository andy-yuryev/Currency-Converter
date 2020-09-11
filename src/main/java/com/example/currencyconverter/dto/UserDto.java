package com.example.currencyconverter.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDto {

    @Pattern(regexp = "\\w+",
            message = "Логин должен содержать только латинские буквы, цифры, нижнее подчеркивание и дефис")
    @Size(max = 30, message = "Логин должен содержать не более 30 символов")
    private String username;

    @Size(min = 5, message = "Пароль должен содержать не менее 5 символов")
    @Size(max = 30, message = "Пароль должен содержать не более 30 символов")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
