package br.edu.iftm.pbackorm.contatos.handler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.iftm.pbackorm.contatos.dto.ErroDTO;
import br.edu.iftm.pbackorm.contatos.handler.exception.RecursoNaoEncontradoException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroDTO> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroDTO erroDTO = ErroDTO.builder()
                .mensagem(ex.getMessage())
                .dataHora(LocalDateTime.now())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erroDTO);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroDTO> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> errosPorCampo = new LinkedHashMap<>();

        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            errosPorCampo.put(erro.getField(), erro.getDefaultMessage());
        }

        ErroDTO erroDTO = ErroDTO.builder()
                .mensagem("Falha de validacao")
                .dataHora(LocalDateTime.now())
                .campos(errosPorCampo)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroDTO);
    }
}
