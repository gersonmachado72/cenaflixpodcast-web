package com.cenaflixpodcast.repository;

import com.cenaflixpodcast.model.Podcast;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PodcastRepository extends JpaRepository<Podcast, Integer> {
    
    List<Podcast> findByProdutor(String produtor);
    
    @Query("SELECT p FROM Podcast p ORDER BY p.numeroEpisodio DESC")
    List<Podcast> findAllOrderByNumeroEpisodioDesc();
    
    Page<Podcast> findAll(Pageable pageable);
    
    @Query("SELECT COUNT(p) FROM Podcast p")
    Long countPodcasts();
    
    @Query("SELECT SUM(p.numeroEpisodio) FROM Podcast p")
    Long sumEpisodes();
    
    @Query("SELECT p.produtor, COUNT(p) as count FROM Podcast p GROUP BY p.produtor ORDER BY count DESC")
    List<Object[]> countByProducer();
    
    // CORREÇÃO: Removido o @Param("limit") que não é suportado em JPQL desta forma.
    // Para limitar, o ideal é passar um Pageable no Service ou usar Native Query.
    @Query("SELECT p.produtor FROM Podcast p GROUP BY p.produtor ORDER BY COUNT(p) DESC")
    List<String> findTopProducers(Pageable pageable);
    
    List<Podcast> findByDataCriacaoBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT DISTINCT p.produtor FROM Podcast p")
    List<String> findAllProducers();
}