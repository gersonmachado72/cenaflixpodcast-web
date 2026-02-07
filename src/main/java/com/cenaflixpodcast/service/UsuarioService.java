package com.cenaflixpodcast.service;

import com.cenaflixpodcast.model.Usuario;
import com.cenaflixpodcast.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario buscarPorNomeESenha(String nome, String senha) {
        Optional<Usuario> usuario = usuarioRepository.findByNomeAndSenha(nome, senha);
        return usuario.orElse(null);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Integer id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario buscarPorNome(String nome) {
        return usuarioRepository.findByNome(nome).orElse(null);
    }

    public boolean existePorEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public Usuario salvarUsuario(Usuario usuario) {
        // Criptografar senha se não estiver criptografada
        if (usuario.getSenha() != null && !usuario.getSenha().startsWith("$2a$")) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }

        // Definir tipo baseado no perfil (para compatibilidade)
        if (usuario.getPerfil() != null) {
            usuario.setTipo(usuario.getPerfil());
        }

        // Ativar por padrão
        usuario.setAtivo(true);
        usuario.setEnabled(true);

        return usuarioRepository.save(usuario);
    }

    public boolean existeUsuario(String nome) {
        return usuarioRepository.existsByNome(nome);
    }

    public void excluirUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
