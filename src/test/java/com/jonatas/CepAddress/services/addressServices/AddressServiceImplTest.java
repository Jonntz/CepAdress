package com.jonatas.CepAddress.services.addressServices;

import com.jonatas.CepAddress.dtos.addressDtos.AddressDto;
import com.jonatas.CepAddress.dtos.addressDtos.CreateAddressDto;
import com.jonatas.CepAddress.exceptions.NotFoundException;
import com.jonatas.CepAddress.models.Address;
import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.AddressRepository;
import com.jonatas.CepAddress.repositories.UserRepository;
import com.jonatas.CepAddress.services.userServices.UserServiceImpl;
import com.jonatas.CepAddress.dtos.addressDtos.ViaCepResponseDTO;
import com.jonatas.CepAddress.services.viaCepService.ViaCepService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AddressServiceImplTest {
    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ViaCepService viaCepService;

    @Test
    void shouldReturnAddressDtoWhenAddressExists() {
        UUID addressId = UUID.fromString("8f9b18ef-ef2c-4185-8792-e355d0534956");

        Address address = new Address();
        address.setId(addressId);
        address.setCep("08215510");
        address.setNumero("32");
        address.setComplemento("4");
        address.setLogradouro("Rua Exemplo");
        address.setCidade("São Paulo");

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(address));

        AddressDto addressDto = addressService.getAddressById(addressId);

        assertNotNull(addressDto);
        assertEquals(address.getId(), addressDto.getId());
        assertEquals(address.getCep(), addressDto.getCep());
        assertEquals(address.getNumero(), addressDto.getNumero());
        assertEquals(address.getComplemento(), addressDto.getComplemento());
        assertEquals(address.getLogradouro(), addressDto.getLogradouro());
        assertEquals(address.getCidade(), addressDto.getCidade());
    }


    @Test
    void shouldThrowNotFoundExceptionWhenAddressDoesNotExist() {
        UUID addressId = UUID.randomUUID();
        when(addressRepository.findById(addressId)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> addressService.getAddressById(addressId));
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        CreateAddressDto createAddressDto = new CreateAddressDto();
        createAddressDto.setCep("08215510");
        createAddressDto.setNumero("32");
        createAddressDto.setComplemento("4");

        User user = new User();
        user.setId(UUID.randomUUID());

        ViaCepResponseDTO viaCepResponseDTO = new ViaCepResponseDTO();
        viaCepResponseDTO.setLogradouro("Rua Teste");
        viaCepResponseDTO.setBairro("Bairro Teste");
        viaCepResponseDTO.setLocalidade("São Paulo");
        viaCepResponseDTO.setUf("SP");

        Address address = new Address();
        address.setId(UUID.randomUUID());
        address.setUser(user);
        address.setCep(createAddressDto.getCep());
        address.setNumero(createAddressDto.getNumero());
        address.setComplemento(createAddressDto.getComplemento());
        address.setLogradouro(viaCepResponseDTO.getLogradouro());
        address.setBairro(viaCepResponseDTO.getBairro());
        address.setCidade(viaCepResponseDTO.getLocalidade());
        address.setEstado(viaCepResponseDTO.getUf());

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(viaCepService.getCepInfo(createAddressDto.getCep())).thenReturn(viaCepResponseDTO);
        when(addressRepository.save(address)).thenReturn(address);

        AddressDto createdAddress = addressService.create(user.getId(), createAddressDto);

        assertNotNull(createdAddress);

        assertEquals(address.getLogradouro(), createdAddress.getLogradouro());
        assertEquals(address.getCidade(), createdAddress.getCidade());
        assertEquals(address.getCep(), createdAddress.getCep());
        assertEquals(address.getNumero(), createdAddress.getNumero());
        assertEquals(address.getComplemento(), createdAddress.getComplemento());
    }
}
