package com.jonatas.CepAddress.services.userServices;

import com.jonatas.CepAddress.dtos.userDtos.CreateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UpdateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.exceptions.BadRequestException;
import com.jonatas.CepAddress.models.Enums.Roles;
import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("John Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setRole(Roles.USER);
    }

    @Test
    void testGetById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto userDto = userService.getById(userId);

        assertEquals(user.getName(), userDto.getName());
        assertEquals(user.getEmail(), userDto.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetAll() {
        when(userRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(user)));

        List<UserDto> users = userService.getAll(Pageable.unpaged());

        assertEquals(1, users.size());
        assertEquals(user.getName(), users.get(0).getName());
    }

    @Test
    void testCreate_Success() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("John Doe");
        createUserDto.setEmail("john@example.com");
        createUserDto.setPassword("password");
        createUserDto.setRole(Roles.USER);

        when(userRepository.existsByEmail(createUserDto.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(createUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto createdUser = userService.create(createUserDto);

        assertEquals(user.getName(), createdUser.getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreate_EmailAlreadyExists() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setEmail("existing@example.com");

        when(userRepository.existsByEmail(createUserDto.getEmail())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.create(createUserDto));
    }

    @Test
    void testUpdate_Success() {
        UpdateUserDto createUserDto = new UpdateUserDto();
        createUserDto.setName("Updated Name");
        createUserDto.setEmail("updated@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDto updatedUser = userService.update(userId, createUserDto);

        assertEquals(createUserDto.getName(), updatedUser.getName());
        assertEquals(createUserDto.getEmail(), updatedUser.getEmail());
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(userId);

        userService.delete(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
