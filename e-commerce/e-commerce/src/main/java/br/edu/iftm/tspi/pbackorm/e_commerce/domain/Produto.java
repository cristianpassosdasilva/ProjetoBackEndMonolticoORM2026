package br.edu.iftm.tspi.pbackorm.e_commerce.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProdutoID")
    private Integer produtoId;

    @NotBlank(message = "nome do produto e obrigatorio")
    @Size(max = 60, message = "nome do produto deve ter no maximo 60 caracteres")
    @Column(name = "ProdutoNome", nullable = false, length = 60)
    private String produtoNome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoriaID")
    @JsonIgnoreProperties({ "produtos", "hibernateLazyInitializer", "handler" })
    private Categoria categoria;

    @NotNull(message = "preco e obrigatorio")
    @Column(name = "preco")
    private Double preco;

    @Column(name = "UnidadesEmEstoque")
    private Short unidadesEmEstoque;

    @Size(max = 100, message = "imagem deve ter no maximo 100 caracteres")
    @Column(name = "Imagem", length = 100)
    private String imagem;
}
