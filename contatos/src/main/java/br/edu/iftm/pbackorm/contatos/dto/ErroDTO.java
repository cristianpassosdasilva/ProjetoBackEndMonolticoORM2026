package br.edu.iftm.pbackorm.contatos.dto;

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
