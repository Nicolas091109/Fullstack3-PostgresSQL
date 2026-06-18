package cl.duoc.sanosysalvos.identity.security;

import cl.duoc.sanosysalvos.identity.model.Usuario;
import cl.duoc.sanosysalvos.identity.repository.UsuarioRepository;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepo;

    public CustomUserDetailsService(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toUpperCase()));

        return new User(
                usuario.getEmail(),
                usuario.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
