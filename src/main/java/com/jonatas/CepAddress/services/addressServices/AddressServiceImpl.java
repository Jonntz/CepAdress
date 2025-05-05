package com.jonatas.CepAddress.services.addressServices;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.dtos.addressDtos.CreateAddressDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.exceptions.NotFoundException;
import com.jonatas.CepAddress.models.Address;
import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.AddressRepository;
import com.jonatas.CepAddress.repositories.UserRepository;
import com.jonatas.CepAddress.dtos.addressDtos.ViaCepResponseDTO;
import com.jonatas.CepAddress.services.viaCepService.ViaCepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AddressServiceImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ViaCepService viaCepService;

    @Override
    public List<AddressDto> getAllByUser(UUID userId) {
        return addressRepository.findByUserId(userId)
                .stream().map(this::mapToDto).toList();
    }

    @Override
    public AddressDto getAddressById(UUID id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        return mapToDto(address);
    }



    @Override
    public AddressDto create(UUID userId, CreateAddressDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        ViaCepResponseDTO cepData = viaCepService.getCepInfo(dto.getCep());

        Address address = new Address();
        address.setUser(user);
        address.setCep(dto.getCep());
        address.setNumero(dto.getNumero());
        address.setComplemento(dto.getComplemento());
        address.setLogradouro(cepData.getLogradouro());
        address.setBairro(cepData.getBairro());
        address.setCidade(cepData.getLocalidade());
        address.setEstado(cepData.getUf());

        return mapToDto(addressRepository.save(address));
    }

    @Override
    public void delete(UUID id) {
        addressRepository.deleteById(id);
    }

    @Override
    public AddressDto update(UUID id, CreateAddressDto dto) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Endereço não encontrado"));

        address.setCep(dto.getCep());
        address.setLogradouro(dto.getLogradouro());

        address.setEstado(dto.getEstado());
        address.setCidade(dto.getCidade());
        address.setBairro(dto.getBairro());
        address.setNumero(dto.getNumero());
        address.setComplemento(dto.getComplemento());

        addressRepository.save(address);
        return mapToDto(address);
    }

    private AddressDto mapToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setCep(address.getCep());
        dto.setLogradouro(address.getLogradouro());
        dto.setNumero(address.getNumero());
        dto.setComplemento(address.getComplemento());
        dto.setBairro(address.getBairro());
        dto.setCidade(address.getCidade());
        dto.setEstado(address.getEstado());
        return dto;
    }
}
