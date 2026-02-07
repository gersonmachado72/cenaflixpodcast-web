package com.cenaflixpodcast.service;

import com.cenaflixpodcast.model.Podcast;
import com.cenaflixpodcast.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PodcastService {
    
    @Autowired
    private PodcastRepository podcastRepository;
    
    @Cacheable(value = "podcasts")
    public List<Podcast> listarTodos() {
        return podcastRepository.findAllOrderByNumeroEpisodioDesc();
    }
    
    @Cacheable(value = "podcastsByProducer", key = "#produtor")
    public List<Podcast> listarPorProdutor(String produtor) {
        return podcastRepository.findByProdutor(produtor);
    }
    
    public Podcast buscarPorId(Integer id) {
        return podcastRepository.findById(id).orElse(null);
    }
    
    @CacheEvict(value = {"podcasts", "podcastsByProducer"}, allEntries = true)
    public Podcast salvarPodcast(Podcast podcast) {
        return podcastRepository.save(podcast);
    }
    
    @CacheEvict(value = {"podcasts", "podcastsByProducer"}, allEntries = true)
    public void excluirPodcast(Integer id) {
        podcastRepository.deleteById(id);
    }
    
    public Page<Podcast> findAll(Pageable pageable) {
        return podcastRepository.findAll(pageable);
    }
    
    public long count() {
        return podcastRepository.count();
    }
    
    public List<String> getAllProducers() {
        return podcastRepository.findAllProducers();
    }
    
    public long getTotalEpisodes() {
        Long sum = podcastRepository.sumEpisodes();
        return sum != null ? sum : 0L;
    }
}
