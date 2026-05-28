package br.edu.iftm.tspi.pbackorm.e_commerce.domain;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DetalhePedidoId implements Serializable {

    @Column(name = "PedidoID")
    private Integer pedidoId;

    @Column(name = "ProdutoID")
    private Integer produtoId;
}
