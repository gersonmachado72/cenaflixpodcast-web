package com.cenaflixpodcast.repository;

import com.cenaflixpodcast.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByNome(String nome);

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE u.nome = :nome AND u.senha = :senha")
    Optional<Usuario> findByNomeAndSenha(@Param("nome") String nome, @Param("senha") String senha);

    boolean existsByNome(String nome);
    boolean existsByEmail(String email);
}
