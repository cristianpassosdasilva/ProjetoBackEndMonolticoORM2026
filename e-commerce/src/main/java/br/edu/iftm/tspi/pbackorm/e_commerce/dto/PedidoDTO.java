package br.edu.iftm.tspi.pbackorm.e_commerce.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class PedidoDTO {

    private Integer id;

    @NotNull
    private String idCliente;

    private LocalDateTime dataPedido;

    @Valid
    @NotEmpty(message = "A lista de detalhes do pedido deve possuir pelo menos um item")
    private List<DetalhePedidoDTO> detalhesPedido;

    private Double valorTotal;

}
