package com.example.currencyconverter.dto;

import javax.validation.constraints.NotNull;

public class UserDto {

    private Long id;

    @NotNull(message = "Поле обязательно для заполнения")
    private String username;

    @NotNull(message = "Поле обязательно для заполнения")
    private String password;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
