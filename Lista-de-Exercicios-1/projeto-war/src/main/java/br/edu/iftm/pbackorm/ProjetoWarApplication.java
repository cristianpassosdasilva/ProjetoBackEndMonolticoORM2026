package br.edu.iftm.pbackorm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ProjetoWarApplication extends SpringBootServletInitializer {

    /*
     * Necessário para rodar dentro de um servidor Tomcat externo (ex: Xampp).
     * O Tomcat chama este método para inicializar a aplicação.
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ProjetoWarApplication.class);
    }

    // Permite rodar localmente também com "mvn spring-boot:run"
    public static void main(String[] args) {
        SpringApplication.run(ProjetoWarApplication.class, args);
    }
}
