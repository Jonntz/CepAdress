package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.authDtos.LoginRequestDto;
import com.jonatas.CepAddress.dtos.authDtos.LoginResponseDto;
import com.jonatas.CepAddress.dtos.authDtos.RegisterRequestDto;
import com.jonatas.CepAddress.models.Enums.Roles;
import com.jonatas.CepAddress.models.User;
import com.jonatas.CepAddress.repositories.UserRepository;
import com.jonatas.CepAddress.security.JwtProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Autenticação", description = "Cadastro e login no sistema")
@Order(1)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(summary = "Fazer login no sistema para acessar os recursos")
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token = jwtProvider.generateToken(user.getId(), user.getEmail(), user.getRole().name());
        return new LoginResponseDto(token);
    }

    @Operation(summary = "Cadastrar-se no sistema")
    @PostMapping("/register")
    @Transactional
    public ResponseEntity<String> register(@RequestBody RegisterRequestDto request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado.");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Roles.USER);

        userRepository.save(user);

        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }
}
