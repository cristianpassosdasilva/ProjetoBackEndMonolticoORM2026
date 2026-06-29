package br.edu.iftm.tspi.pbackorm.autenticacao.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GerarSenha {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String[] senhasOriginais = { "cadu123", "joao123" };

        for (String senhaOriginal : senhasOriginais) {
            String senhaCriptografada = encoder.encode(senhaOriginal);
            System.out.println("Senha original: " + senhaOriginal);
            System.out.println("Senha BCrypt: " + senhaCriptografada);
        }
    }
}
