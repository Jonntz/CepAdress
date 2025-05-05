package com.jonatas.CepAddress.controllers;

import com.jonatas.CepAddress.dtos.addressDtos.ViaCepResponseDTO;
import com.jonatas.CepAddress.dtos.userDtos.UserDto;
import com.jonatas.CepAddress.services.viaCepService.ViaCepService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/cep")
public class CepController {

    @GetMapping("/{cep}")
    public ResponseEntity<ViaCepResponseDTO> getAddressByCep(@PathVariable String cep) {
        return ResponseEntity.ok(ViaCepService.getCepInfo(cep));
    }
}
