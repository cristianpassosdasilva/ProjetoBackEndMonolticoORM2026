package br.edu.iftm.pbackorm.contatos.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.pbackorm.contatos.domain.Contato;


@RestController
@RequestMapping("/contatos")
public class ContatoController {
    
    private final List<Contato> contatos = new ArrayList<>(List.of(
        new Contato(1,"Kakaroto"),
        new Contato(2,"Vegeta")
    ));

    @GetMapping
    public ResponseEntity<List<Contato>> listar() {
        return ResponseEntity.ok(contatos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> buscarPorId(@PathVariable Integer id) {
        for (Contato contato: contatos) {
            if (contato.getCodigo().equals(id)) {
                return ResponseEntity.ok(contato);
            }
        }
        return ResponseEntity.notFound().build();
    }
    




}
