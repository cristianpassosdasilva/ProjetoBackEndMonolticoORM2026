package br.edu.iftm.tspi.pbackorm.autenticacao.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.edu.iftm.tspi.pbackorm.autenticacao.repository.UsuarioRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return repository.findById(login)
                .map(UsuarioAutenticado::new)
                .orElseThrow(
                        () -> new UsernameNotFoundException("User Not Found with username: " + login));
    }
}
