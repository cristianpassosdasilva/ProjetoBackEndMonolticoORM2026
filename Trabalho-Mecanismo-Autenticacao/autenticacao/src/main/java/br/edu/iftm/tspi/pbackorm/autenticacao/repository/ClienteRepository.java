package br.edu.iftm.tspi.pbackorm.autenticacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iftm.tspi.pbackorm.autenticacao.domain.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

}
