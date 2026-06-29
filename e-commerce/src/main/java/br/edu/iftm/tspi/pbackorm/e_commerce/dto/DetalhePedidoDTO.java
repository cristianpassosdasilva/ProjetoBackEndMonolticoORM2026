package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class DetalhePedidoDTO {

    @NotNull
    private Integer idProduto;

    private Integer idPedido;
    
    @Positive(message = "O preço de venda deve ser maior do que 0")
    private Double precoVenda;

    @Min(value = 1, message= "Deve comprar pelo menos uma unidade do item")
    private Short quantidade;

    private Double desconto;

}
