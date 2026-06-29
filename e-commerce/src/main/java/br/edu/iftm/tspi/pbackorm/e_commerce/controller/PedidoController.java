package br.edu.iftm.tspi.pbackorm.e_commerce.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> buscarPorClienteEPeriodo(
                        @RequestParam(required = true) String clienteId,
                        @RequestParam(required = true) LocalDate dataInicio,
                        @RequestParam(required = true) LocalDate dataFim) {
        List<PedidoDTO> pedidos = pedidoService
                        .buscarPedidosPorClienteEPeriodo(clienteId, dataInicio, dataFim);
        if (pedidos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pedidos);
    }

}
