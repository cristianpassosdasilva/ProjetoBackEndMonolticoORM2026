package br.edu.iftm.tspi.pbackorm.contato.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.contato.domain.Contato;


@RestController
@CrossOrigin(origins = "*")
public class ContatoController {

    @GetMapping("/contatos")
    public List<Contato> getContatos() {
        Contato contato1 = new Contato(1, "Jonas");
        Contato contato2 = new Contato(2, "Tadeu");
        List<Contato> contatos = new ArrayList<>();
        contatos.add(contato1);
        contatos.add(contato2);
        return contatos;
    }
}
