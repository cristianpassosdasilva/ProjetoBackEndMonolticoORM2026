package br.edu.iftm.tspi.pbackorm.contato.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.contato.domain.Contato;

@RestController
@RequestMapping("/contatos")
public class ContatoController {

    private List<Contato> contatos = new ArrayList<>(List.of(
            new Contato(1, "Goku"),
            new Contato(2, "Vegeta"))
    );

    @GetMapping
    public ResponseEntity<List<Contato>> listar() {
        return ResponseEntity.ok(contatos);
    }

    @GetMapping
    public ResponseEntity<List<Contato>> buscar() {
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Integer id) {
        for (Contato contato : contatos) {
            if (contato.getCodigo().equals(id)) {
                return ResponseEntity.ok(contato);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Contato>> buscarPorNome(@RequestParam(required = true) String nome) {
        List<Contato> resposta = new ArrayList<>();
        for (Contato contato : contatos) {
            if (contato.getNome().toLowerCase().contains(nome)) {
                resposta.add(contato);
            }
        }
        return ResponseEntity.ok(resposta);
    }

    @GetMapping()
    public ResponseEntity<List<Contato>> buscar(@RequestParam(required = false) String nome) {
        if (nome == null) {
            return ResponseEntity.ok(contatos);
        }
        List<Contato> resposta = new ArrayList<>();
        for (Contato contato : contatos) {
            if (contato.getNome().toLowerCase().contains(nome.toLowerCase())) {
                resposta.add(contato);
            }
        }
        return ResponseEntity.ok(resposta);
    }

    @PostMapping
    public ResponseEntity<Contato> novo(@RequestBody Contato contato) {
        contatos.add(contato);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(contato);
    }

}
