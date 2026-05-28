package br.edu.iftm.tspi.pbackorm.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    List<Produto> findByCategoriaCategoriaId(Integer categoriaId);
}
