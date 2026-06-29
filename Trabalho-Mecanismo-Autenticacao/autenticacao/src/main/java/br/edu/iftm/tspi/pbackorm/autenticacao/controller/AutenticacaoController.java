package br.edu.iftm.tspi.pbackorm.autenticacao.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.iftm.tspi.pbackorm.autenticacao.security.AutenticacaoService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/autenticacao")
@AllArgsConstructor
public class AutenticacaoController {

    private final AutenticacaoService autenticacaoService;

    @PostMapping
    public String autenticar(Authentication authentication) {
        return autenticacaoService.authenticate(authentication);
    }
}
