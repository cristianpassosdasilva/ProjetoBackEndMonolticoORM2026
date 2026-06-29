package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValorConsumidoPorProdutoDTO {

    private Integer idProduto;

    private String nomeProduto;

    private Double valorTotalConsumido;

}
