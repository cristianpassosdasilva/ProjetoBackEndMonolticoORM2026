package br.edu.iftm.tspi.pbackorm.e_commerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Pedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.PedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper.PedidoMapper;
import br.edu.iftm.tspi.pbackorm.e_commerce.service.PedidoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/pedidos")
@AllArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    private final PedidoMapper pedidoMapper;

    @PostMapping
    public ResponseEntity<PedidoDTO> salvar(@Valid @RequestBody PedidoDTO pedidoNovoDTO) {
        Pedido pedidoNovo = pedidoMapper.toEntity(pedidoNovoDTO);        
        Pedido pedidoSalvo = pedidoService.salvar(pedidoNovo);
        PedidoDTO pedidoSalvoDto = pedidoMapper.toDto(pedidoSalvo);
        return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(pedidoSalvoDto);
    }
        
}
