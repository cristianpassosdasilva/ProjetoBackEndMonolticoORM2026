package br.edu.iftm.pbackorm.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/matematica")
public class MatematicaController {

    // -------------------------------------------------------------------------
    // Exercício 1 - GET /matematica/soma?a=10&b=20
    // Retorna a soma de dois números
    // -------------------------------------------------------------------------
    @GetMapping("/soma")
    public ResponseEntity<?> soma(
            @RequestParam(required = false) String a,
            @RequestParam(required = false) String b) {

        if (a == null || b == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Parâmetros 'a' e 'b' são obrigatórios"));
        }

        try {
            double numA = Double.parseDouble(a);
            double numB = Double.parseDouble(b);
            return ResponseEntity.ok(Map.of("resultado", numA + numB));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Os parâmetros 'a' e 'b' devem ser numéricos"));
        }
    }

    // -------------------------------------------------------------------------
    // Exercício 2 - GET /matematica/fatorial?n=5
    // Retorna o fatorial de n (n deve ser >= 0)
    // -------------------------------------------------------------------------
    @GetMapping("/fatorial")
    public ResponseEntity<?> fatorial(@RequestParam(required = false) String n) {

        if (n == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "Parâmetro 'n' é obrigatório"));
        }

        try {
            int numero = Integer.parseInt(n);

            if (numero < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("erro", "O parâmetro 'n' deve ser maior ou igual a 0"));
            }

            long resultado = calcularFatorial(numero);
            return ResponseEntity.ok(Map.of("resultado", resultado));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O parâmetro 'n' deve ser um número inteiro"));
        }
    }

    // Método auxiliar para calcular o fatorial
    private long calcularFatorial(int n) {
        if (n == 0 || n == 1) return 1;
        long resultado = 1;
        for (int i = 2; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }

    // -------------------------------------------------------------------------
    // Exercício 3 - POST /matematica/media
    // Corpo: { "valores": [10, 20, 30] }
    // Retorna a média dos valores
    // -------------------------------------------------------------------------
    @PostMapping("/media")
    public ResponseEntity<?> media(@RequestBody Map<String, Object> corpo) {

        Object valoresObj = corpo.get("valores");

        if (!(valoresObj instanceof List<?> valores)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O campo 'valores' deve ser uma lista"));
        }

        if (valores.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A lista não pode ser vazia"));
        }

        try {
            double soma = 0;
            for (Object valor : valores) {
                soma += Double.parseDouble(valor.toString());
            }
            double media = soma / valores.size();
            return ResponseEntity.ok(Map.of("resultado", media));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A lista deve conter apenas números válidos"));
        }
    }

    // -------------------------------------------------------------------------
    // Exercício 4 - POST /matematica/soma-linhas
    // Corpo: { "matriz": [[1, 2], [3, 4]] }
    // Retorna a soma de cada linha da matriz
    // -------------------------------------------------------------------------
    @PostMapping("/soma-linhas")
    public ResponseEntity<?> somaLinhas(@RequestBody Map<String, Object> corpo) {

        Object matrizObj = corpo.get("matriz");

        if (!(matrizObj instanceof List<?> matriz) || matriz.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "O campo 'matriz' não pode ser vazio"));
        }

        try {
            int tamanhoLinha = -1;
            List<Double> somasLinhas = new ArrayList<>();

            for (Object linhaObj : matriz) {
                if (!(linhaObj instanceof List<?> linha)) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("erro", "Formato de matriz inválido"));
                }

                // Valida que todas as linhas têm o mesmo tamanho
                if (tamanhoLinha == -1) {
                    tamanhoLinha = linha.size();
                } else if (linha.size() != tamanhoLinha) {
                    return ResponseEntity.badRequest()
                            .body(Map.of("erro", "Todas as linhas devem ter o mesmo tamanho"));
                }

                double somaLinha = 0;
                for (Object valor : linha) {
                    somaLinha += Double.parseDouble(valor.toString());
                }
                somasLinhas.add(somaLinha);
            }

            return ResponseEntity.ok(Map.of("resultado", somasLinhas));

        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("erro", "A matriz deve conter apenas números válidos"));
        }
    }
}
