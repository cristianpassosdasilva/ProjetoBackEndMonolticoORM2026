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

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedidoID;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Pedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.DetalhePedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.PedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.DetalhePedidoMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.PedidoMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.DetalhePedidoRepository;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoRepository repository;
    private final DetalhePedidoRepository detalhePedidoRepository;
    private final PedidoMapper mapper;
    private final DetalhePedidoMapper detalheMapper;

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listar() {
        return ResponseEntity.ok(mapper.toDtoList(repository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPorId(@PathVariable Integer id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de ID " + id + " não encontrado"));
        return ResponseEntity.ok(mapper.toDto(pedido));
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> novo(@Valid @RequestBody PedidoDTO dto) {
        Pedido pedido = mapper.toEntity(dto);
        if (pedido.getDetalhesPedido() != null) {
            pedido.getDetalhesPedido().forEach(item -> item.setPedido(pedido));
        }
        Pedido salvo = repository.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDto(salvo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> atualizar(@PathVariable Integer id,
                                               @Valid @RequestBody PedidoDTO dto) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Pedido de ID " + id + " não encontrado");
        }
        Pedido pedido = mapper.toEntity(dto);
        pedido.setId(id);
        if (pedido.getDetalhesPedido() != null) {
            pedido.getDetalhesPedido().forEach(item -> {
                item.setPedido(pedido);
                item.getId().setPedidoId(id);
            });
        }
        return ResponseEntity.ok(mapper.toDto(repository.save(pedido)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Pedido pedido = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de ID " + id + " não encontrado"));
        repository.delete(pedido);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{pedidoId}/itens")
    public ResponseEntity<List<DetalhePedidoDTO>> listarItens(@PathVariable Integer pedidoId) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de ID " + pedidoId + " não encontrado"));
        return ResponseEntity.ok(detalheMapper.toDtoList(pedido.getDetalhesPedido()));
    }

    @PostMapping("/{pedidoId}/itens")
    public ResponseEntity<DetalhePedidoDTO> adicionarItem(@PathVariable Integer pedidoId,
                                                          @Valid @RequestBody DetalhePedidoDTO dto) {
        Pedido pedido = repository.findById(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException("Pedido de ID " + pedidoId + " não encontrado"));
        DetalhePedido item = detalheMapper.toEntity(dto);
        item.getId().setPedidoId(pedidoId);
        item.setPedido(pedido);
        DetalhePedido salvo = detalhePedidoRepository.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(detalheMapper.toDto(salvo));
    }

    @PutMapping("/{pedidoId}/itens/{index}")
    public ResponseEntity<DetalhePedidoDTO> atualizarItem(@PathVariable Integer pedidoId,
                                                          @PathVariable Integer index,
                                                          @Valid @RequestBody DetalhePedidoDTO dto) {
        DetalhePedidoID chave = new DetalhePedidoID();
        chave.setPedidoId(pedidoId);
        chave.setProdutoId(index);

        DetalhePedido item = detalhePedidoRepository.findById(chave)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item com produto ID " + index + " não encontrado no pedido " + pedidoId));

        item.setPrecoVenda(dto.getPrecoVenda());
        item.setQuantidade(dto.getQuantidade());
        item.setDesconto(dto.getDesconto());

        return ResponseEntity.ok(detalheMapper.toDto(detalhePedidoRepository.save(item)));
    }

    @DeleteMapping("/{pedidoId}/itens/{index}")
    public ResponseEntity<Void> removerItem(@PathVariable Integer pedidoId,
                                            @PathVariable Integer index) {
        DetalhePedidoID chave = new DetalhePedidoID();
        chave.setPedidoId(pedidoId);
        chave.setProdutoId(index);

        DetalhePedido item = detalhePedidoRepository.findById(chave)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Item com produto ID " + index + " não encontrado no pedido " + pedidoId));

        detalhePedidoRepository.delete(item);
        return ResponseEntity.noContent().build();
    }
}
