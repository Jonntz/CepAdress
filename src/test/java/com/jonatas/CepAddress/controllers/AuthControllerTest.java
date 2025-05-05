package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.authDtos.LoginRequestDto;
import com.jonatas.CepAddress.dtos.authDtos.LoginResponseDto;
import com.jonatas.CepAddress.dtos.authDtos.RegisterRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequestDto registerRequest = new RegisterRequestDto();
        registerRequest.setName("New User");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");

        ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", registerRequest, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuário registrado com sucesso!", response.getBody());
    }

    @Test
    void shouldLoginUserSuccessfully() {
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setEmail("test@user.com");
        loginRequest.setPassword("password123");

        ResponseEntity<LoginResponseDto> response = restTemplate.postForEntity("/api/auth/login", loginRequest, LoginResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getToken());
    }
}
