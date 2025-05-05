package com.jonatas.CepAddress.services.viaCepService;

import com.jonatas.CepAddress.dtos.addressDtos.ViaCepResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ViaCepService {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static ViaCepResponseDTO getCepInfo(String cep) {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        return restTemplate.getForObject(url, ViaCepResponseDTO.class);
    }
}
