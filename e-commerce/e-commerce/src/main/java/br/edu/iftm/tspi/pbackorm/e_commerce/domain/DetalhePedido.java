package br.edu.iftm.tspi.pbackorm.e_commerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "detalhes_pedido")
@Getter
@Setter
@NoArgsConstructor
public class DetalhePedido {

    @EmbeddedId
    private DetalhePedidoId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pedidoId")
    @JoinColumn(name = "PedidoID")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("produtoId")
    @JoinColumn(name = "ProdutoID")
    @JsonIgnoreProperties({ "categoria", "hibernateLazyInitializer", "handler" })
    private Produto produto;

    @Column(name = "precoVenda", nullable = false)
    private Double precoVenda;

    @Column(name = "quantidade", nullable = false)
    private Short quantidade;

    @Column(name = "desconto", nullable = false)
    private Double desconto;
}
