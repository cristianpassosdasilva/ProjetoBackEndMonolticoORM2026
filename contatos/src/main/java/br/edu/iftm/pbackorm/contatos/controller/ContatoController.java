package br.edu.iftm.pbackorm.contatos.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        if (contatoOptional.isPresent()) {
            return ResponseEntity.ok().body(contatoOptional.get());
        }
        return ResponseEntity.notFound().build();        
    }
 
    @GetMapping
    public List<Contato> listar(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String email,
        @RequestParam(required = false) String telefone
    ) {
        nome = (nome == null) ? "" : nome;
        email = (email == null) ? "" : email;
        telefone = (telefone == null) ? "" : telefone;

        return repository
                .findByNomeContainingIgnoreCaseAndEmailContainingIgnoreCaseAndTelefoneContaining(
                    nome, email, telefone);
    }

    @PostMapping
    public ResponseEntity<Contato> novo(@RequestBody Contato novoContato) {
        Contato contato = repository.save(novoContato);
        return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(contato);
    }
/* 
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Integer id,
                                       @RequestBody Contato contatoAtualizado) {
        for (Contato contato: contatos) {
            if (contato.getCodigo().equals(id)) {
                if (contatoAtualizado.getNome() == null || 
                    contatoAtualizado.getNome().isBlank()) {
                     return ResponseEntity.status(HttpStatus.BAD_REQUEST) 
                        .body(new ErroDTO("Contato com nome vazio",LocalDateTime.now()));
                }
                contato.setNome(contatoAtualizado.getNome());
                return ResponseEntity.ok(contatoAtualizado);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErroDTO("Contato não encontrado "+id,LocalDateTime.now()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletar(@PathVariable Integer id) {
        boolean removido = contatos.removeIf(
            contato -> contato.getCodigo().equals(id));
        if (removido) {
            return ResponseEntity.noContent().build();
        }    
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ErroDTO("Contato não encontrado "+id, LocalDateTime.now()));
    }
*/
}
