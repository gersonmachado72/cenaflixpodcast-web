package com.cenaflixpodcast.security;

import com.cenaflixpodcast.model.Usuario;
import com.cenaflixpodcast.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tenta encontrar por nome (username) ou email
        Usuario usuario = usuarioRepository.findByNome(username)
            .orElseGet(() -> usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username)));
        
        if (!usuario.getAtivo()) {
            throw new UsernameNotFoundException("Usuário está inativo: " + username);
        }
        
        return usuario; 
    }
}
