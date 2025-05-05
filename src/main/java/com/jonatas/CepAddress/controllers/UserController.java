package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.userDtos.CreateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UpdateUserDto;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.services.userServices.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Usuários", description = "CRUD de usuários com validações")
@Order(2)
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Traz um usuario por ID")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @Operation(summary = "Lista todos os usuários (Apenas administradores tem acesso a este recurso)")
    @GetMapping("/allUsers")
    public ResponseEntity<List<UserDto>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return ResponseEntity.ok(userService.getAll(pageable));
    }

    @Operation(summary = "Adiciona um usuário no sistema")
    @PostMapping
    public ResponseEntity<UserDto> create(@RequestBody @Valid CreateUserDto dto) throws BadRequestException {
        return new ResponseEntity<UserDto>(userService.create(dto), HttpStatus.CREATED);
    }

    @Operation(summary = "Editar um usuário com base no Id do usuário")
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable UUID id, @RequestBody @Valid UpdateUserDto dto) {
        return ResponseEntity.ok(userService.update(id, dto));
    }

    @Operation(summary = "Deletar um usuário com base no Id do usuário")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
