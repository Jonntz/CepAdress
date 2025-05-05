package com.jonatas.CepAddress.dtos.authDtos;


import com.jonatas.CepAddress.models.Enums.Roles;

public class RegisterRequestDto {
    private String name;
    private String email;
    private String password;
    private Roles role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
