package br.edu.iftm.pbackorm.controller;

import br.edu.iftm.pbackorm.model.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    // Simula um banco de dados em memória usando um Map
    // Chave = id do produto, Valor = produto
    private final Map<Long, Produto> produtos = new HashMap<>();
    private Long proximoId = 1L;

    // -------------------------------------------------------------------------
    // GET /produtos
    // GET /produtos?nome=notebook
    // GET /produtos?precoMin=100&precoMax=500
    // GET /produtos?nome=mouse&precoMax=200
    // -------------------------------------------------------------------------
    @GetMapping
    public ResponseEntity<List<Produto>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) Double precoMin,
            @RequestParam(required = false) Double precoMax) {

        List<Produto> lista = new ArrayList<>(produtos.values());

        // Filtra por nome (busca parcial, ignora maiúsculas/minúsculas)
        if (nome != null) {
            lista = lista.stream()
                    .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filtra por preço mínimo
        if (precoMin != null) {
            lista = lista.stream()
                    .filter(p -> p.getPreco() >= precoMin)
                    .collect(Collectors.toList());
        }

        // Filtra por preço máximo
        if (precoMax != null) {
            lista = lista.stream()
                    .filter(p -> p.getPreco() <= precoMax)
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(lista);
    }

    // -------------------------------------------------------------------------
    // GET /produtos/{id}
    // -------------------------------------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        Produto produto = produtos.get(id);

        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Produto com id " + id + " não encontrado"));
        }

        return ResponseEntity.ok(produto);
    }

    // -------------------------------------------------------------------------
    // POST /produtos
    // -------------------------------------------------------------------------
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Produto produto) {
        // Valida nome
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O nome do produto é obrigatório"));
        }

        // Valida preço
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O preço deve ser maior que zero"));
        }

        // Valida estoque
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A quantidade em estoque não pode ser negativa"));
        }

        // Valida código de barras duplicado
        if (codigoBarrasDuplicado(produto.getCodigoBarras(), null)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe um produto com esse código de barras"));
        }

        // Gera o ID e salva
        produto.setId(proximoId++);
        produtos.put(produto.getId(), produto);

        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    // -------------------------------------------------------------------------
    // PUT /produtos/{id}
    // -------------------------------------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Produto produto) {
        // Verifica se o produto existe
        if (!produtos.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Produto com id " + id + " não encontrado"));
        }

        // Valida nome
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O nome do produto é obrigatório"));
        }

        // Valida preço
        if (produto.getPreco() == null || produto.getPreco() <= 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O preço deve ser maior que zero"));
        }

        // Valida estoque
        if (produto.getQuantidadeEstoque() == null || produto.getQuantidadeEstoque() < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A quantidade em estoque não pode ser negativa"));
        }

        // Valida código de barras duplicado (ignora o próprio produto)
        if (codigoBarrasDuplicado(produto.getCodigoBarras(), id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Já existe um produto com esse código de barras"));
        }

        // Atualiza e salva
        produto.setId(id);
        produtos.put(id, produto);

        return ResponseEntity.ok(produto);
    }

    // -------------------------------------------------------------------------
    // DELETE /produtos/{id}
    // -------------------------------------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        if (!produtos.containsKey(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Produto com id " + id + " não encontrado"));
        }

        produtos.remove(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    // -------------------------------------------------------------------------
    // Método auxiliar: verifica se outro produto já usa o mesmo código de barras.
    // O parâmetro "idIgnorado" é usado no PUT para não comparar o produto consigo mesmo.
    // -------------------------------------------------------------------------
    private boolean codigoBarrasDuplicado(String codigoBarras, Long idIgnorado) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            return false;
        }
        return produtos.values().stream()
                .anyMatch(p -> !p.getId().equals(idIgnorado)
                        && codigoBarras.equals(p.getCodigoBarras()));
    }
}
