package br.edu.iftm.pbackorm.contatos.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

import br.edu.iftm.pbackorm.contatos.domain.Contato;
import br.edu.iftm.pbackorm.contatos.repository.ContatoRepository;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private final ContatoRepository repository;

    public ContatoController(ContatoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Integer id) {
        Optional<Contato> contatoOptional = repository.findById(id);
        return contatoOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Contato>> listar(@RequestParam(required = false) String nome) {
        if (nome == null || nome.isBlank()) {
            return ResponseEntity.ok(repository.findAll());
        }

        return ResponseEntity.ok(repository.findByNomeContainingIgnoreCase(nome));
    }

    @PostMapping
    public ResponseEntity<Contato> novo(@RequestBody Contato contato) {
        if (contato.getNome() == null || contato.getNome().isBlank()
                || contato.getEmail() == null || contato.getEmail().isBlank()
                || contato.getTelefone() == null || contato.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        contato.setCodigo(null);
        contato.setDataCadastro(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(repository.save(contato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contato> atualizar(@PathVariable Integer id, @RequestBody Contato contatoAtualizado) {
        Optional<Contato> contatoOptional = repository.findById(id);
        if (contatoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (contatoAtualizado.getNome() == null || contatoAtualizado.getNome().isBlank()
                || contatoAtualizado.getEmail() == null || contatoAtualizado.getEmail().isBlank()
                || contatoAtualizado.getTelefone() == null || contatoAtualizado.getTelefone().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Contato contato = contatoOptional.get();
        contato.setNome(contatoAtualizado.getNome());
        contato.setEmail(contatoAtualizado.getEmail());
        contato.setTelefone(contatoAtualizado.getTelefone());

        return ResponseEntity.ok(repository.save(contato));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
