package com.cenaflixpodcast.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class DebugController {
    
    @GetMapping("/debug/health")
    @ResponseBody
    public Map<String, String> healthCheck() {
        Map<String, String> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.LocalDateTime.now().toString());
        health.put("service", "Cenaflix Podcast Web");
        health.put("version", "1.0.0");
        return health;
    }
    
    @GetMapping("/debug/routes")
    @ResponseBody
    public Map<String, String> listRoutes() {
        Map<String, String> routes = new HashMap<>();
        routes.put("/login", "LoginController - GET");
        routes.put("/dashboard", "DashboardController - GET");
        routes.put("/podcasts/listar", "PodcastController - GET");
        routes.put("/podcasts/novo", "PodcastController - GET");
        routes.put("/podcasts/editar/{id}", "PodcastController - GET");
        routes.put("/podcasts/detalhes/{id}", "PodcastController - GET");
        routes.put("/podcasts/salvar", "PodcastController - POST");
        routes.put("/podcasts/excluir/{id}", "PodcastController - GET");
        routes.put("/podcasts/buscar", "PodcastController - GET");
        routes.put("/reports/dashboard", "ReportController - GET");
        routes.put("/reports/producer", "ReportController - GET");
        routes.put("/reports/monthly", "ReportController - GET");
        routes.put("/arquivo", "ArquivoController - GET");
        routes.put("/debug/health", "DebugController - GET");
        routes.put("/debug/routes", "DebugController - GET");
        return routes;
    }
}
