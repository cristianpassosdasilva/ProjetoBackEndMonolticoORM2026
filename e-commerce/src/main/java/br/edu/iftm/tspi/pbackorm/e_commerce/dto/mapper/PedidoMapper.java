package br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Pedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.DetalhePedidoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.PedidoDTO;

@Mapper(componentModel= "spring")
public interface PedidoMapper {
    
    @Mapping(source = "idCliente", target = "cliente.id")
    Pedido toEntity(PedidoDTO pedidoDto);

    @Mapping(source = "cliente.id", target = "idCliente")
    PedidoDTO toDto(Pedido pedido);

    @Mapping(source = "pedido.id", target = "idPedido")
    @Mapping(source = "produto.id", target = "idProduto")
    DetalhePedidoDTO toDetalheDto(DetalhePedido detalhe);
    
    @Mapping(source = "idPedido", target = "pedido.id")
    @Mapping(source = "idProduto", target = "produto.id")
    DetalhePedido toDetalheEntity(DetalhePedidoDTO detalheDto);


    List<DetalhePedidoDTO> toDetalhesDtoList(List<DetalhePedido> detalhes);
    List<DetalhePedido> toDetalhesEntityList(List<DetalhePedidoDTO> detalhesDto);

    List<Pedido> toEntityList(List<PedidoDTO> PedidoDto);
    List<PedidoDTO> toDtoList(List<Pedido> Pedido);
}