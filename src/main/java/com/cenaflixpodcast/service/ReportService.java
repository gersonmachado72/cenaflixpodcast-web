package com.cenaflixpodcast.service;

import com.cenaflixpodcast.model.Podcast;
import com.cenaflixpodcast.repository.PodcastRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {
    
    @Autowired
    private PodcastRepository podcastRepository;
    
    public Map<String, Object> generateDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long totalPodcasts = podcastRepository.count();
        long totalEpisodes = podcastRepository.sumEpisodes() != null ? 
                           podcastRepository.sumEpisodes() : 0L;
        
        // Usando PageRequest para o limite de 5
        Pageable topFive = PageRequest.of(0, 5);
        List<String> topProducers = podcastRepository.findTopProducers(topFive);
        
        List<Object[]> producerCounts = podcastRepository.countByProducer();
        
        Map<String, Long> podcastsByProducer = new HashMap<>();
        for (Object[] obj : producerCounts) {
            podcastsByProducer.put((String) obj[0], (Long) obj[1]);
        }
        
        Map<String, Long> monthlyStats = getMonthlyStatistics();
        
        stats.put("totalPodcasts", totalPodcasts);
        stats.put("totalEpisodes", totalEpisodes);
        stats.put("topProducers", topProducers);
        stats.put("podcastsByProducer", podcastsByProducer);
        stats.put("monthlyStats", monthlyStats);
        stats.put("averageDuration", calculateAverageDuration());
        
        return stats;
    }
    
    public Map<String, Long> getMonthlyStatistics() {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusMonths(6), LocalTime.MIN);
        LocalDateTime endDate = LocalDateTime.now();
        
        List<Podcast> podcasts = podcastRepository.findByDataCriacaoBetween(startDate, endDate);
        
        return podcasts.stream()
            .collect(Collectors.groupingBy(
                p -> p.getDataCriacao().getMonth().toString(),
                Collectors.counting() // CORRIGIDO: Era 'Collectiors'
            ));
    }
    
    public String calculateAverageDuration() {
        List<Podcast> podcasts = podcastRepository.findAll();
        return calculateAverageDurationForList(podcasts);
    }
    
    public Map<String, Object> generateProducerReport(String producer) {
        Map<String, Object> report = new HashMap<>();
        
        List<Podcast> podcasts = podcastRepository.findByProdutor(producer);
        
        report.put("producer", producer);
        report.put("totalPodcasts", podcasts.size());
        report.put("totalEpisodes", podcasts.stream()
            .mapToInt(Podcast::getNumeroEpisodio)
            .sum());
        report.put("averageDuration", calculateAverageDurationForList(podcasts));
        report.put("podcasts", podcasts.stream()
            .sorted(Comparator.comparing(Podcast::getNumeroEpisodio).reversed())
            .collect(Collectors.toList()));
        
        return report;
    }
    
    // Método auxiliar para evitar repetição de código
    private String calculateAverageDurationForList(List<Podcast> podcasts) {
        if (podcasts == null || podcasts.isEmpty()) return "00:00";
        
        long totalSeconds = podcasts.stream()
            .mapToLong(p -> {
                try {
                    if (p.getDuracao() != null && p.getDuracao().contains(":")) {
                        String[] parts = p.getDuracao().split(":");
                        if (parts.length == 2) {
                            return Long.parseLong(parts[0]) * 60 + Long.parseLong(parts[1]);
                        }
                    }
                } catch (Exception e) {
                    // Ignora formatos inválidos
                }
                return 0;
            })
            .sum();
        
        long averageSeconds = totalSeconds / podcasts.size();
        long minutes = averageSeconds / 60;
        long seconds = averageSeconds % 60;
        
        return String.format("%02d:%02d", minutes, seconds);
    }
}
