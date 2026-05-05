package br.edu.iftm.tspi.pbackorm.e_commerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Categoria;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Produto;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.CategoriaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/categorias")
@AllArgsConstructor
public class CategoriaController {

    private final CategoriaRepository repository;

    @GetMapping
    public List<Categoria> listar(@RequestParam(required = false) String nome) {
        nome = (nome == null) ? "": nome;
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        Categoria categoria = repository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                            "Categoria de ID "+id+" não encontrado"
                                        ));
        return ResponseEntity.ok().body(categoria);
    }

    @GetMapping("/{id}/produtos")
    public ResponseEntity<List<Produto>> buscarProdutos(@PathVariable Integer id) {
        Categoria categoria = repository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                            "Categoria de ID "+id+" não encontrado"
                                        ));
        return ResponseEntity.ok().body(categoria.getProdutos());
    }

    @PostMapping
    public ResponseEntity<Categoria> novo(@Valid @RequestBody Categoria novo) {
        Categoria categoria = repository.save(novo);
        return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(categoria);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@PathVariable Integer id,
                    @Valid @RequestBody Categoria categoriaAtualizar) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Categoria de ID "+id+" não encontrado");
        }
        categoriaAtualizar.setId(id);
        Categoria categoriaAtualizada = repository.save(categoriaAtualizar);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Categoria categoria = repository.findById(id)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                            "Categoria de ID "+id+" não encontrado"
                                        ));
        repository.delete(categoria);
        return ResponseEntity.noContent().build();
    }

                    

}
