package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.userDtos.CreateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateUserSuccessfully() {
        CreateUserDto createUserDto = new CreateUserDto();
        createUserDto.setName("Test User");
        createUserDto.setEmail("test@user.com");
        createUserDto.setPassword("password123");

        ResponseEntity<UserDto> response = restTemplate.postForEntity("/api/users", createUserDto, UserDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createUserDto.getName(), response.getBody().getName());
        assertEquals(createUserDto.getEmail(), response.getBody().getEmail());
    }
}
