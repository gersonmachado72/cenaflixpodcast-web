package com.cenaflixpodcast.controller.web;

import com.cenaflixpodcast.model.Arquivo;
import com.cenaflixpodcast.service.ArquivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/arquivo")
public class ArquivoController {

    @Autowired
    private ArquivoService arquivoService;

    @GetMapping
    public String redirectToListar() {
        return "redirect:/arquivo/listar";
    }

    @GetMapping("/novo")
    public String showNovoArquivo(Model model) {
        model.addAttribute("titulo", "Novo Arquivo");
        model.addAttribute("arquivo", new Arquivo());
        return "arquivo/novo";
    }

    private static final Logger logger = LoggerFactory.getLogger(ArquivoController.class);

    @PostMapping("/upload")
    public String uploadArquivo(@RequestParam("arquivo") MultipartFile file,
            @RequestParam(value = "descricao", required = false) String descricao,
            RedirectAttributes redirectAttributes) {

        logger.info("=== INICIANDO UPLOAD ===");
        logger.info("Nome original: {}", file.getOriginalFilename());
        logger.info("Tamanho: {} bytes", file.getSize());
        logger.info("Tipo MIME: {}", file.getContentType());
        logger.info("Descrição: {}", descricao);

        try {
            if (file.isEmpty()) {
                logger.warn("Arquivo vazio recebido");
                redirectAttributes.addFlashAttribute("erro", "Por favor, selecione um arquivo para upload.");
                return "redirect:/arquivo/novo";
            }

            Arquivo arquivoSalvo = arquivoService.salvarArquivo(file, descricao, null);

            // CORREÇÃO AQUI: estava usando mensagem como ID
            logger.info("Arquivo salvo com sucesso! ID: {}, Nome: {}",
                    arquivoSalvo.getId(), arquivoSalvo.getNomeOriginal());

            redirectAttributes.addFlashAttribute("sucesso",
                    "Arquivo '" + file.getOriginalFilename() + "' upload realizado com sucesso!");

        } catch (Exception e) {
            logger.error("ERRO no upload: ", e);
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao fazer upload: " + e.getMessage());
        }

        return "redirect:/arquivo/listar";
    }

    @GetMapping("/listar")
    public String listarArquivos(Model model) {
        logger.info("=== LISTANDO ARQUIVOS ===");
        List<Arquivo> arquivos = arquivoService.listarTodos();
        long totalArquivos = arquivoService.contarArquivos();

        logger.info("Total de arquivos encontrados: {}", totalArquivos);

        model.addAttribute("titulo", "Lista de Arquivos");
        model.addAttribute("arquivo", arquivos);
        model.addAttribute("totalArquivos", totalArquivos);
        return "arquivo/listar";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhesArquivo(@PathVariable Integer id, Model model) {
        Arquivo arquivo = arquivoService.buscarPorId(id);
        if (arquivo == null) {
            return "redirect:/arquivo/listar";
        }
        model.addAttribute("arquivo", arquivo);
        return "arquivo/detalhes";
    }

    @GetMapping("/excluir/{id}")
    public String excluirArquivo(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            arquivoService.excluirArquivo(id);
            redirectAttributes.addFlashAttribute("sucesso", "Arquivo excluído com sucesso!");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir arquivo: " + e.getMessage());
        }
        return "redirect:/arquivo/listar";
    }

    @GetMapping("/buscar")
    public String buscarArquivos(@RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "tipo", required = false) String tipo,
            Model model) {

        logger.info("=== BUSCANDO ARQUIVOS ===");
        logger.info("Parâmetros - Nome: {}, Tipo: {}", nome, tipo);
        List<Arquivo> arquivos;

        if (nome != null && !nome.isEmpty()) {
            arquivos = arquivoService.buscarPorNome(nome);
            model.addAttribute("filtro", "Resultados para: " + nome);
            logger.info("Busca por '{}' retornou {} resultados", nome, arquivos.size());
        } else if (tipo != null && !tipo.isEmpty()) {
            arquivos = arquivoService.buscarPorTipo(tipo);
            model.addAttribute("filtro", "Arquivos do tipo: " + tipo);
            logger.info("Busca por tipo '{}' retornou {} resultados", tipo, arquivos.size());
        } else {
            arquivos = arquivoService.listarTodos();
            model.addAttribute("filtro", "Todos os arquivos");
            logger.info("Listando todos os arquivos: {} resultados", arquivos.size());
        }

        model.addAttribute("arquivos", arquivos);
        model.addAttribute("totalArquivos", arquivos.size());
        return "arquivo/listar";
    }
}
