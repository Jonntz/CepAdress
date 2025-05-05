package com.jonatas.CepAddress.services.userServices;

import com.jonatas.CepAddress.dtos.userDtos.CreateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UpdateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    UserDto getById(UUID id);
    List<UserDto> getAll(Pageable pageable);
    UserDto create(CreateUserDto dto) throws BadRequestException;
    UserDto update(UUID id, UpdateUserDto dto);
    void delete(UUID id);
}
