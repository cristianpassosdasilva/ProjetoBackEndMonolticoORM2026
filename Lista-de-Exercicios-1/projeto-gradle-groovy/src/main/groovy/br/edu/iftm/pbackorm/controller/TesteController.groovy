package br.edu.iftm.pbackorm.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/teste")
class TesteController {

    @GetMapping
    Map<String, String> teste() {
        return [mensagem: "Projeto Gradle-Groovy + Groovy funcionando na porta 10000!"]
    }
}
