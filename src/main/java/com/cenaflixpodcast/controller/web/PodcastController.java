package com.cenaflixpodcast.controller.web;

import com.cenaflixpodcast.model.Podcast;
import com.cenaflixpodcast.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/podcasts")
public class PodcastController {

    @Autowired
    private PodcastService podcastService;

    @GetMapping("/listar")
    public String listarPodcasts(Model model) {
        List<Podcast> podcasts = podcastService.listarTodos();
        model.addAttribute("podcasts", podcasts);
        return "listagem-podcasts";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesPodcast(@PathVariable Integer id, Model model) {
        Podcast podcast = podcastService.buscarPorId(id);
        if (podcast == null) {
            return "redirect:/podcasts/listar";
        }
        model.addAttribute("podcast", podcast);
        return "detalhes-podcast";
    }

    @GetMapping("/novo")
    public String showCadastroPodcast(Model model) {
        model.addAttribute("podcast", new Podcast());
        return "cadastro-podcast";
    }

    @PostMapping("/salvar")
    public String salvarPodcast(@ModelAttribute Podcast podcast) {
        podcastService.salvarPodcast(podcast);
        return "redirect:/podcasts/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarPodcast(@PathVariable Integer id, Model model) {
        Podcast podcast = podcastService.buscarPorId(id);
        if (podcast == null) {
            return "redirect:/podcasts/listar";
        }
        model.addAttribute("podcast", podcast);
        return "cadastro-podcast";
    }

    @PostMapping("/editar/{id}")
    public String atualizarPodcast(@PathVariable Integer id, @ModelAttribute Podcast podcast) {
        podcast.setId(id);
        podcastService.salvarPodcast(podcast);
        return "redirect:/podcasts/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirPodcast(@PathVariable Integer id) {
        podcastService.excluirPodcast(id);
        return "redirect:/podcasts/listar";
    }

    @GetMapping("/buscar")
    public String buscarPorProdutor(@RequestParam String produtor, Model model) {
        List<Podcast> podcasts = podcastService.listarPorProdutor(produtor);
        model.addAttribute("podcasts", podcasts);
        model.addAttribute("produtorFiltro", produtor);
        return "listagem-podcasts";
    }
}
