# Decisões de implementação — Exercícios 1 a 6 (e-commerce)

Este documento registra as decisões tomadas durante a implementação dos 6 itens pedidos, e o porquê de cada uma.

## Item 1 — Unidades compradas via JPQL

Já existia um método `findUnidadesCompradasPeriodo` em `ProdutoRepository`, mas ele usa **nativeQuery** (SQL puro), e o exercício pede explicitamente JPQL.

**Decisão:** criar um método novo e separado, `buscarUnidadesCompradasPeriodoJpql`, usando JPQL com `SELECT new ...DTO(...)`, sem alterar o método nativo existente (que continua funcionando como antes).

**Motivo:** preservar o que já existia e atender ao requisito exato do exercício (JPQL), sem misturar as duas abordagens no mesmo método.

## DTO do item 1 — por que `UnidadesCompradasJpqlDTO` e não reaproveitar `UnidadesCompradasDTO`

O DTO existente `UnidadesCompradasDTO` é uma **interface de projection**, que só funciona com `nativeQuery`. Uma consulta JPQL com `SELECT new ...` precisa de uma **classe concreta** com construtor.

**Decisão:** criar uma classe nova chamada `UnidadesCompradasJpqlDTO`, com os mesmos campos (`nomeProduto`, `unidadesCompradas`).

**Motivo:** Java não permite uma classe e uma interface com o mesmo nome no mesmo pacote — então não dava para simplesmente reaproveitar o nome `UnidadesCompradasDTO`. O nome novo deixa claro que essa versão é a do método JPQL.

## Item 2 — Parâmetro opcional de código de produto

**Decisão:** o parâmetro opcional `produtoId` foi adicionado na **mesma consulta JPQL criada para o item 1**, em vez de criar um método/endpoint separado.

**Motivo:** o exercício já trabalha em cima da mesma lógica de período (data início/fim) do item 1 — fazia sentido reaproveitar a query e simplesmente filtrar por produto quando o parâmetro for informado (`AND (:produtoId IS NULL OR p.id = :produtoId)`), e trazer todos os produtos quando ele for omitido.

## Itens 3 a 6 — Camadas (Repository, Service, Controller)

**Decisão:** para os itens 3, 4, 5 e 6 foi implementada a stack completa: método de consulta JPQL no Repository, método no Service que chama o Repository, e endpoint REST no Controller.

**Motivo:** o enunciado pede explicitamente para "criar um serviço", então a tarefa não se limitava só à query — precisava existir uma camada de Service de fato, com um endpoint para o professor poder testar via HTTP.

## Item 3 — Vendas de um produto por pedido

Pedido: "ao informar o ID do produto, retorne o número do pedido, a quantidade de produtos vendidos, o valor total do produto naquele pedido e o valor total do desconto aplicado".

**Decisões:**
- **Granularidade:** retorna uma **lista**, com um registro para cada pedido em que aquele produto foi vendido (não um único valor agregado somando tudo).
- **Cálculo dos valores:** `valorTotalProduto = precoVenda * quantidade` e `valorTotalDesconto = desconto * quantidade`.
- **DTO novo:** `VendaProdutoDTO` (numeroPedido, quantidadeVendida, valorTotalProduto, valorTotalDesconto).
- **Onde ficou a query:** em `DetalhePedidoRepository`, porque a entidade `DetalhePedido` já tem chave composta (pedido + produto), então cada linha da consulta já corresponde exatamente a "esse produto nesse pedido" — não precisou de `GROUP BY`.
- **Endpoint:** `GET /produtos/{id}/vendas`, exposto via `ProdutoService` (novo).

## Item 4 — Valor total consumido por produto (por cliente)

Pedido: "ao informar o ID do cliente, retorne o valor total consumido por produto".

**Decisão:** retorna uma lista com um registro por produto distinto comprado por aquele cliente (`idProduto`, `nomeProduto`, `valorTotalConsumido`), somando `precoVenda * quantidade` de todos os pedidos do cliente para aquele produto (`GROUP BY` produto).

**DTO novo:** `ValorConsumidoPorProdutoDTO`.

**Endpoint:** `GET /clientes/{clienteId}/valor-consumido-por-produto`, exposto via `ClienteService` (novo).

## Item 5 — Valor total consumido por categoria

Pedido: "ao informar o ID da categoria, retorne o valor total consumido na mesma".

**Decisão:** em vez de devolver um único número agregado, foi feito um **detalhamento por produto dentro da categoria** — mesmo formato do item 4 (`idProduto`, `nomeProduto`, `valorTotalConsumido`), mas filtrando por categoria.

**Motivo:** essa opção foi escolhida para dar mais visibilidade de quais produtos compõem o total da categoria, em vez de só um número isolado.

**Reaproveitamento de DTO:** como o formato é idêntico ao do item 4 (mudando só o filtro: cliente vs. categoria), foi reaproveitado o **mesmo DTO** `ValorConsumidoPorProdutoDTO` para os dois itens, em vez de criar uma classe duplicada.

**Endpoint:** `GET /categorias/{id}/valor-consumido-por-produto`, exposto via `CategoriaService` (novo).

## Item 6 — Pedidos de um cliente em um período, com produtos e valor total

Pedido: "ao informar data início, data fim e código do cliente, retorne todos os pedidos com seus respectivos produtos e valor total".

**Decisões:**
- **Formato do retorno:** uma lista de pedidos, cada um já trazendo a lista de produtos/detalhes (`detalhesPedido`) e o valor total do pedido.
- **DTO:** em vez de criar uma classe nova só para essa resposta, foi adicionado um campo `valorTotal` direto no `PedidoDTO` já existente (usado também na criação de pedidos via POST). Isso foi uma decisão explícita: o `PedidoDTO` passou a ter esse campo em todas as respostas que o usam, mesmo que em alguns casos (como no POST de criação) ele venha vazio/nulo.
- **Cálculo do valor total:** soma de `precoVenda * quantidade` de todos os detalhes do pedido — calculado no `PedidoService`, já que o `Pedido` (entidade) não tem esse valor armazenado.
- **Onde ficou a query:** método `findByClienteIdAndDataPedidoBetween` em `PedidoRepository` (Spring Data, sem precisar escrever JPQL manual, já que é um filtro direto por dois campos).
- **Endpoint:** `GET /pedidos?clienteId=...&dataInicio=...&dataFim=...`.

## Observação geral

A implementação foi feita item a item, exatamente conforme pedido no enunciado — sem adicionar funcionalidades extras. Toda vez que uma decisão de modelagem não estava explícita no enunciado (nome de classe, formato de retorno, onde colocar a query, etc.), a escolha foi validada antes de codificar, para não tomar decisões por conta própria que pudessem fugir do que foi pedido.
