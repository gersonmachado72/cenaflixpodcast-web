package com.cenaflixpodcast.controller.web;

import com.cenaflixpodcast.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/reports")
public class ReportController {
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public String showDashboard(Model model) {
        model.addAttribute("stats", reportService.generateDashboardStats());
        return "reports/dashboard";
    }
    
    @GetMapping("/producer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PRODUCER')")
    public String showProducerReport(@RequestParam String producer, Model model) {
        model.addAttribute("report", reportService.generateProducerReport(producer));
        return "reports/producer-report";
    }
    
    @GetMapping("/monthly")
    @PreAuthorize("hasRole('ADMIN')")
    public String showMonthlyReport(Model model) {
        model.addAttribute("monthlyStats", reportService.getMonthlyStatistics());
        return "reports/monthly-report";
    }
}
