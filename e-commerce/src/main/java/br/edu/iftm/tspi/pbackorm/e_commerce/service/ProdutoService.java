package br.edu.iftm.tspi.pbackorm.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.iftm.tspi.pbackorm.e_commerce.dto.VendaProdutoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.DetalhePedidoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProdutoService {

    private final DetalhePedidoRepository detalhePedidoRepository;

    public List<VendaProdutoDTO> buscarVendasPorProduto(Integer produtoId) {
        return detalhePedidoRepository.buscarVendasPorProduto(produtoId);
    }

}
