package br.edu.iftm.tspi.pbackorm.autenticacao.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.edu.iftm.tspi.pbackorm.autenticacao.domain.Usuario;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsuarioAutenticado implements UserDetails {

    private final Usuario usuario;

    @Override
    public String getUsername() {
        return usuario.getLogin();
    }

    @Override
    public String getPassword() {
        return usuario.getSenha();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return usuario.getRoles()
                .stream()
                .flatMap(role -> role.getPermissoes().stream())
                .map(permissao -> new SimpleGrantedAuthority(permissao.getNome()))
                .toList();
    }
}
