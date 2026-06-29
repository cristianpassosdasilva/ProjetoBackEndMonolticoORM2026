package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VendaProdutoDTO {

    private Integer numeroPedido;

    private Short quantidadeVendida;

    private Double valorTotalProduto;

    private Double valorTotalDesconto;

}
