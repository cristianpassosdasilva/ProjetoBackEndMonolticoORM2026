package br.edu.iftm.pbackorm.contatos.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TB_CONTATO")
@NoArgsConstructor
@Data
public class Contato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COD_CONTATO")
    private Integer codigo;

    @Column(name="NOM_CONTATO",nullable=false)
    private String nome;

    @Column(name="DES_EMAIL",nullable=false)
    private String email;

    @Column(name="DES_TELEFONE",nullable=false)
    private String telefone;

    @Column(name="DAT_CADASTRO")
    private LocalDateTime dataCadastro;
}
