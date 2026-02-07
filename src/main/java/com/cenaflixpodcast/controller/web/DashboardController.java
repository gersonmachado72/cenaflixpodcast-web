package com.cenaflixpodcast.controller.web;

import com.cenaflixpodcast.model.Podcast;
import com.cenaflixpodcast.service.PodcastService;
import com.cenaflixpodcast.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private PodcastService podcastService;

    @Autowired
    private ReportService reportService;

    @GetMapping("/dashboard")
    @PreAuthorize("isAuthenticated()")
    public String dashboard(Model model) {
        try {
            logger.info("Acessando dashboard...");
            
            // Obter estatísticas
            Map<String, Object> stats = reportService.generateDashboardStats();
            logger.info("Estatísticas obtidas com sucesso");
            
            // Obter podcasts recentes (últimos 5)
            List<Podcast> allPodcasts = podcastService.listarTodos();
            logger.info("Total de podcasts encontrados: {}", allPodcasts.size());
            
            List<Podcast> recentPodcasts = allPodcasts.size() > 5 ? 
                allPodcasts.subList(0, Math.min(5, allPodcasts.size())) : allPodcasts;
            
            // Calcular podcasts do último mês
            long lastMonthPodcasts = allPodcasts.stream()
                .filter(p -> p.getDataCriacao() != null && p.getDataCriacao().isAfter(LocalDateTime.now().minusMonths(1)))
                .count();
            
            // Adicionar atributos ao modelo com valores padrão seguros
            model.addAttribute("totalPodcasts", stats.getOrDefault("totalPodcasts", 0L));
            model.addAttribute("totalEpisodes", stats.getOrDefault("totalEpisodes", 0L));
            model.addAttribute("averageDuration", stats.getOrDefault("averageDuration", "00:00"));
            
            // Tratar podcastsByProducer
            Object podcastsByProducerObj = stats.get("podcastsByProducer");
            if (podcastsByProducerObj instanceof Map) {
                Map<?, ?> producerMap = (Map<?, ?>) podcastsByProducerObj;
                model.addAttribute("podcastsByProducer", producerMap);
                model.addAttribute("totalProducers", producerMap.size());
            } else {
                model.addAttribute("podcastsByProducer", Collections.emptyMap());
                model.addAttribute("totalProducers", 0);
            }
            
            model.addAttribute("recentPodcasts", recentPodcasts);
            model.addAttribute("lastMonthPodcasts", lastMonthPodcasts);
            
            logger.info("Dashboard carregado com sucesso");
            return "dashboard";
            
        } catch (Exception e) {
            logger.error("Erro ao carregar dashboard", e);
            // Valores padrão em caso de erro
            model.addAttribute("totalPodcasts", 0L);
            model.addAttribute("totalEpisodes", 0L);
            model.addAttribute("averageDuration", "00:00");
            model.addAttribute("podcastsByProducer", Collections.emptyMap());
            model.addAttribute("totalProducers", 0);
            model.addAttribute("recentPodcasts", Collections.emptyList());
            model.addAttribute("lastMonthPodcasts", 0L);
            return "dashboard";
        }
    }
}
