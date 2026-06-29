package br.edu.iftm.tspi.pbackorm.e_commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedido;
import br.edu.iftm.tspi.pbackorm.e_commerce.domain.DetalhePedidoID;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.VendaProdutoDTO;
import br.edu.iftm.tspi.pbackorm.e_commerce.dto.ValorConsumidoPorProdutoDTO;

@Repository
public interface DetalhePedidoRepository extends JpaRepository<DetalhePedido, DetalhePedidoID> {

    @Query("""
              SELECT new br.edu.iftm.tspi.pbackorm.e_commerce.dto.VendaProdutoDTO(
                    dp.pedido.id, dp.quantidade,
                    dp.precoVenda * dp.quantidade,
                    dp.desconto * dp.quantidade)
              FROM DetalhePedido dp
              WHERE dp.produto.id = :produtoId
            """)
    public List<VendaProdutoDTO> buscarVendasPorProduto(@Param("produtoId") Integer produtoId);

    @Query("""
              SELECT new br.edu.iftm.tspi.pbackorm.e_commerce.dto.ValorConsumidoPorProdutoDTO(
                    p.id, p.nome, SUM(dp.precoVenda * dp.quantidade))
              FROM DetalhePedido dp
                   JOIN dp.produto p
                   JOIN dp.pedido pd
              WHERE pd.cliente.id = :clienteId
              GROUP BY p.id, p.nome
            """)
    public List<ValorConsumidoPorProdutoDTO> buscarValorConsumidoPorProdutoCliente(
            @Param("clienteId") String clienteId);

    @Query("""
              SELECT new br.edu.iftm.tspi.pbackorm.e_commerce.dto.ValorConsumidoPorProdutoDTO(
                    p.id, p.nome, SUM(dp.precoVenda * dp.quantidade))
              FROM DetalhePedido dp
                   JOIN dp.produto p
              WHERE p.categoria.id = :categoriaId
              GROUP BY p.id, p.nome
            """)
    public List<ValorConsumidoPorProdutoDTO> buscarValorConsumidoPorProdutoCategoria(
            @Param("categoriaId") Integer categoriaId);

}
