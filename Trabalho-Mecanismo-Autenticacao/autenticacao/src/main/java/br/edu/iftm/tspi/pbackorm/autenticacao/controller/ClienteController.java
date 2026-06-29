package br.edu.iftm.tspi.pbackorm.autenticacao.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.autenticacao.domain.Cliente;
import br.edu.iftm.tspi.pbackorm.autenticacao.repository.ClienteRepository;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteRepository repository;

    @GetMapping()
    public ResponseEntity<List<Cliente>> getClientes() {
        List<Cliente> clientes = repository.findAll();
        if (clientes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(clientes);
    }

    @PostMapping()
    public ResponseEntity<Cliente> postCliente(@RequestBody Cliente cliente) {
        Cliente salvo = repository.save(cliente);
        return ResponseEntity.ok().body(salvo);
    }
}
