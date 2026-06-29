package br.edu.iftm.tspi.pbackorm.e_commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.edu.iftm.tspi.pbackorm.e_commerce.dto.ValorConsumidoPorProdutoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.repository.DetalhePedidoRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ClienteService {

    private final DetalhePedidoRepository detalhePedidoRepository;

    public List<ValorConsumidoPorProdutoDTO> buscarValorConsumidoPorProduto(String clienteId) {
        return detalhePedidoRepository.buscarValorConsumidoPorProdutoCliente(clienteId);
    }

}
