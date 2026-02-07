package com.cenaflixpodcast.repository;

import com.cenaflixpodcast.model.Arquivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArquivoRepository extends JpaRepository<Arquivo, Integer> {
    
    List<Arquivo> findByOrderByDataUploadDesc();
    
    List<Arquivo> findByTipoContainingIgnoreCase(String tipo);
    
    List<Arquivo> findByNomeOriginalContainingIgnoreCase(String nome);
    
    List<Arquivo> findByPodcastId(Integer podcastId);
}
