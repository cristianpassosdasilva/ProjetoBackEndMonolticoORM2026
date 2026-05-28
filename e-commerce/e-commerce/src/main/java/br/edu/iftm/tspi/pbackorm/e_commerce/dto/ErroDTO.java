package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErroDTO {

    private String mensagem;

    private LocalDateTime dataHora;

    private Map<String, String> campos;
}
