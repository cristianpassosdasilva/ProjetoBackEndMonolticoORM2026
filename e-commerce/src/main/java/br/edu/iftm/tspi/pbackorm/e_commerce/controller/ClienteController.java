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
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Cliente;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.ClienteDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.PedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.ValorConsumidoPorProdutoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.ClienteMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.PedidoMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.ClienteRepository;
import br.edu.iftm.tspi.pbackorm.e_commerce.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/clientes")
@AllArgsConstructor
public class ClienteController {

    private final ClienteRepository repository;
    private final ClienteMapper mapper;
    private final PedidoMapper pedidoMapper;
    private final ClienteService service;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> listar() {
        return ResponseEntity.ok(mapper.toDtoList(repository.findAll()));
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<ClienteDTO> buscarPorId(@PathVariable String clienteId) {
        Cliente cliente = repository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente de ID " + clienteId + " não encontrado"));
        return ResponseEntity.ok(mapper.toDto(cliente));
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> novo(@Valid @RequestBody ClienteDTO dto) {
        Cliente salvo = repository.save(mapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(salvo));
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<ClienteDTO> atualizar(@PathVariable String clienteId,
                                                @Valid @RequestBody ClienteDTO dto) {
        if (!repository.existsById(clienteId)) {
            throw new EntityNotFoundException("Cliente de ID " + clienteId + " não encontrado");
        }
        Cliente cliente = mapper.toEntity(dto);
        cliente.setId(clienteId);
        return ResponseEntity.ok(mapper.toDto(repository.save(cliente)));
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> deletar(@PathVariable String clienteId) {
        Cliente cliente = repository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente de ID " + clienteId + " não encontrado"));
        repository.delete(cliente);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{clienteId}/pedidos")
    public ResponseEntity<List<PedidoDTO>> listarPedidos(@PathVariable String clienteId) {
        Cliente cliente = repository.findById(clienteId)
                .orElseThrow(() -> new EntityNotFoundException("Cliente de ID " + clienteId + " não encontrado"));
        return ResponseEntity.ok(pedidoMapper.toDtoList(cliente.getPedidos()));
    }

    @GetMapping("/{clienteId}/valor-consumido-por-produto")
    public ResponseEntity<List<ValorConsumidoPorProdutoDTO>> buscarValorConsumidoPorProduto(
            @PathVariable String clienteId) {
        List<ValorConsumidoPorProdutoDTO> valores = service.buscarValorConsumidoPorProduto(clienteId);
        if (valores.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(valores);
    }
}
