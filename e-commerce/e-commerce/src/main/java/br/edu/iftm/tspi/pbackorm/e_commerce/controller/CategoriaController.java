package br.edu.iftm.tspi.pbackorm.e_commerce.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Categoria;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Produto;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.CategoriaRepository;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;
    private final ProdutoRepository produtoRepository;

    public CategoriaController(CategoriaRepository categoriaRepository, ProdutoRepository produtoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.produtoRepository = produtoRepository;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Categoria buscarPorId(@PathVariable Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria nao encontrada: " + id));
    }

    @GetMapping("/{id}/produtos")
    public List<Produto> buscarProdutosDaCategoria(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria nao encontrada: " + id);
        }
        return produtoRepository.findByCategoriaCategoriaId(id);
    }

    @PostMapping
    public ResponseEntity<Categoria> criar(@Valid @RequestBody Categoria categoria) {
        Categoria categoriaSalva = categoriaRepository.save(categoria);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoriaSalva.getCategoriaId())
                .toUri();

        return ResponseEntity.created(location).body(categoriaSalva);
    }

    @PutMapping("/{id}")
    public Categoria atualizar(@PathVariable Integer id, @Valid @RequestBody Categoria categoriaAtualizada) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoria nao encontrada: " + id));

        categoria.setCategoria(categoriaAtualizada.getCategoria());
        if (categoriaAtualizada.getDescricao() != null) {
            categoria.setDescricao(categoriaAtualizada.getDescricao());
        }

        return categoriaRepository.save(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!categoriaRepository.existsById(id)) {
            throw new EntityNotFoundException("Categoria nao encontrada: " + id);
        }
        categoriaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
