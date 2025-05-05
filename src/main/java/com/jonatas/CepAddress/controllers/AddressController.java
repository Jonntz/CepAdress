package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.dtos.addressDtos.CreateAddressDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.services.addressServices.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Endereços", description = "CRUD de Endereços com integração à API do ViaCep")
@Order(3)
@RestController
@RequestMapping("/api/addresses")
public class AddressController {
    @Autowired
    private AddressService addressService;

    @Operation(summary = "Lista todos os endereços de um usuário com base no Id do usuário")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressDto>> getAllByUser(@PathVariable UUID userId) {
        List<AddressDto> addresses = addressService.getAllByUser(userId);
        return ResponseEntity.ok(addresses);
    }

    @Operation(summary = "Traz um endereço por ID")
    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(addressService.getAddressById(id));
    }

    @Operation(summary = "Adiciona um endereço a um usuário específico com base no Id do usuário")
    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressDto> create(
            @PathVariable UUID userId,
            @RequestBody CreateAddressDto dto
    ) {
        AddressDto createdAddress = addressService.create(userId, dto);
        return ResponseEntity.ok(createdAddress);
    }

    @Operation(summary = "Remove um endereço de um usuário específico com base no Id do endereço")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> delete(@PathVariable UUID addressId) {
        addressService.delete(addressId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Edita as informações de um endereço de um usuário específico com base no Id do endereço")
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDto> update(@PathVariable UUID addressId, @RequestBody @Valid CreateAddressDto dto) {
        return ResponseEntity.ok(addressService.update(addressId, dto));
    }
}
