package com.jonatas.CepAddress.services.addressServices;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.dtos.addressDtos.CreateAddressDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.models.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressService {
    AddressDto getAddressById(UUID id);
    AddressDto create(UUID userId, CreateAddressDto dto);
    List<AddressDto> getAllByUser(UUID userId);
    void delete(UUID id);
    AddressDto update(UUID id, CreateAddressDto dto);
}
