package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UnidadesCompradasJpqlDTO {

    private String nomeProduto;

    private Long unidadesCompradas;

}
