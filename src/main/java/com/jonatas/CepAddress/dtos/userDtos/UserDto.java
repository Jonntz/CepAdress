package com.jonatas.CepAddress.dtos.userDtos;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.models.Enums.Roles;

import java.util.List;
import java.util.UUID;

public class UserDto {
    private UUID id;
    private String name;
    private String email;
    private List<AddressDto> addresses;

    private Roles role;



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public List<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
