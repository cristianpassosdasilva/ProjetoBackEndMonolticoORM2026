package br.edu.iftm.tspi.pbackorm.e_commerce.dto.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.DetalhePedidoDTO;

@Mapper(componentModel = "spring")
public interface DetalhePedidoMapper {

    @Mapping(source = "idProduto", target = "id.produtoId")
    @Mapping(source = "idProduto", target = "produto.id")
    DetalhePedido toEntity(DetalhePedidoDTO dto);

    @Mapping(source = "id.produtoId", target = "idProduto")
    DetalhePedidoDTO toDto(DetalhePedido entity);

    List<DetalhePedidoDTO> toDtoList(List<DetalhePedido> entities);

    List<DetalhePedido> toEntityList(List<DetalhePedidoDTO> dtos);
}
