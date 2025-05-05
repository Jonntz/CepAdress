package com.jonatas.CepAddress.services.userServices;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.dtos.userDtos.CreateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UpdateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.exceptions.BadRequestException;
import com.jonatas.CepAddress.exceptions.NotFoundException;
import com.jonatas.CepAddress.models.Enums.Roles;
import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return mapToDTO(user);
    }

    @Override
    public List<UserDto> getAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::mapToDTO)
                .getContent();
    }

    @Override
    public UserDto create(CreateUserDto dto)  {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BadRequestException("Email já cadastrado");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole() != null ? dto.getRole() : Roles.USER);

        userRepository.save(user);

        return mapToDTO(user);
    }

    @Override
    public UserDto update(UUID id, UpdateUserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return mapToDTO(user);
    }

    @Override
    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    private UserDto mapToDTO(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAddresses(user.getAddresses().stream().map(addr -> {
            AddressDto address = new AddressDto();
            address.setId(addr.getId());
            address.setCep(addr.getCep());
            address.setLogradouro(addr.getLogradouro());
            address.setNumero(addr.getNumero());
            address.setComplemento(addr.getComplemento());
            address.setBairro(addr.getBairro());
            address.setCidade(addr.getCidade());
            address.setEstado(addr.getEstado());
            return address;
        }).toList());

        return dto;
    }
}
