package br.edu.iftm.tspi.pbackorm.e_commerce.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Pedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Produto;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.PedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.PedidoMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.exception.EstoqueInsuficienteException;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.PedidoRepository;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.ProdutoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    private final ProdutoRepository produtoRepository;

    private final PedidoMapper pedidoMapper;

    @Transactional
    public Pedido salvar(Pedido pedidoNovo) {
        pedidoNovo.setDataPedido(LocalDateTime.now());

        for (DetalhePedido detalhe : pedidoNovo.getDetalhesPedido()) {
            Integer produtoID = detalhe.getProduto().getId();
            Produto produto = produtoRepository.findById(produtoID)
                    .orElseThrow(() -> new EntityNotFoundException(
                    "Produto de ID " + produtoID + " não encontrado"));
            if (produto.getEstoque() < detalhe.getQuantidade()) {
                throw new EstoqueInsuficienteException(
                        "Estoque insuficiente para o produto " + produto.getNome());
            }
            Short estoqueNovo = (short) (produto.getEstoque() - detalhe.getQuantidade());
            produto.setEstoque(estoqueNovo);
            detalhe.setProduto(produto);
            detalhe.setPedido(pedidoNovo);
            produtoRepository.save(produto);
        }
        return pedidoRepository.save(pedidoNovo);
    }

    public List<PedidoDTO> buscarPedidosPorClienteEPeriodo(
            String clienteId, LocalDate dataInicio, LocalDate dataFim) {
        List<Pedido> pedidos = pedidoRepository.findByClienteIdAndDataPedidoBetween(
                clienteId, dataInicio.atStartOfDay(), dataFim.atTime(23, 59, 59));
        return pedidos.stream()
                .map(pedido -> {
                    PedidoDTO dto = pedidoMapper.toDto(pedido);
                    double valorTotal = pedido.getDetalhesPedido().stream()
                            .mapToDouble(detalhe -> detalhe.getPrecoVenda() * detalhe.getQuantidade())
                            .sum();
                    dto.setValorTotal(valorTotal);
                    return dto;
                })
                .toList();
    }

}
