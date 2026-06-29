package br.edu.iftm.tspi.pbackorm.e_commerce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.edu.iftm.tspi.pbackorm.e_commerce.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido,Integer>{

    public List<Pedido> findByClienteIdAndDataPedidoBetween(
            String clienteId, LocalDateTime dataInicio, LocalDateTime dataFim);

}
