package br.edu.iftm.tspi.pbackorm.e_commerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.e_commerce.repository.ProdutoRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Produto;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.ProdutoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.ProdutoMapper;


@RestController
@RequestMapping("/produtos")
@AllArgsConstructor
public class ProdutoController {

    private final ProdutoRepository repository;

    private final ProdutoMapper mapper;

    @PostMapping
    public ResponseEntity<ProdutoDTO> novo(@Valid @RequestBody ProdutoDTO produtoNovoDto) {
        Produto produtoEntidade = mapper.toEntity(produtoNovoDto);
        Produto produtoSalvo = repository.save(produtoEntidade);
        ProdutoDTO produtoSalvoDto = mapper.toDto(produtoSalvo);
        return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(produtoSalvoDto);
    }
    



}
