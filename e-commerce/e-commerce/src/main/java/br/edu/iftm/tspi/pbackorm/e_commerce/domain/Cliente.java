package br.edu.iftm.tspi.pbackorm.e_commerce.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
public class Cliente {

    @Id
    @Column(name = "ClienteID", length = 5)
    private String clienteId;

    @NotBlank(message = "nome e obrigatorio")
    @Size(max = 30, message = "nome deve ter no maximo 30 caracteres")
    @Column(name = "nome", length = 30)
    private String nome;

    @Size(max = 30, message = "cargo deve ter no maximo 30 caracteres")
    @Column(name = "cargo", length = 30)
    private String cargo;

    @Size(max = 60, message = "endereco deve ter no maximo 60 caracteres")
    @Column(name = "endereco", length = 60)
    private String endereco;

    @Size(max = 15, message = "cidade deve ter no maximo 15 caracteres")
    @Column(name = "cidade", length = 15)
    private String cidade;

    @Size(max = 10, message = "cep deve ter no maximo 10 caracteres")
    @Column(name = "cep", length = 10)
    private String cep;

    @Size(max = 15, message = "pais deve ter no maximo 15 caracteres")
    @Column(name = "pais", length = 15)
    private String pais;

    @Size(max = 24, message = "telefone deve ter no maximo 24 caracteres")
    @Column(name = "telefone", length = 24)
    private String telefone;

    @Size(max = 24, message = "fax deve ter no maximo 24 caracteres")
    @Column(name = "Fax", length = 24)
    private String fax;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();
}
