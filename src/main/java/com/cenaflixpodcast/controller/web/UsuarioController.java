package com.cenaflixpodcast.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cenaflixpodcast.model.Usuario;
import com.cenaflixpodcast.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public String redirectToListar() {
        return "redirect:/usuarios/listar";
    }

    @GetMapping("/novo")
    public String showNovoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Novo Usuário");
        return "usuarios/novo";
    }

    @PostMapping("/salvar")
    public String salvarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam("confirmarSenha") String confirmarSenha,
            RedirectAttributes redirectAttributes) {

        // Validações básicas
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Nome é obrigatório!");
            return "redirect:/usuarios/novo";
        }

        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("erro", "Email é obrigatório!");
            return "redirect:/usuarios/novo";
        }

        // Verificar se email já existe (usando existsByEmail que retorna boolean)
        if (usuarioService.existePorEmail(usuario.getEmail())) {
            redirectAttributes.addFlashAttribute("erro", "Este email já está cadastrado!");
            return "redirect:/usuarios/novo";
        }

        if (usuario.getSenha() == null || usuario.getSenha().length() < 6) {
            redirectAttributes.addFlashAttribute("erro", "Senha deve ter no mínimo 6 caracteres!");
            return "redirect:/usuarios/novo";
        }

        if (!usuario.getSenha().equals(confirmarSenha)) {
            redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem!");
            return "redirect:/usuarios/novo";
        }

        // Definir perfil padrão se não informado
        if (usuario.getPerfil() == null || usuario.getPerfil().isEmpty()) {
            usuario.setPerfil("VISUALIZADOR");
        }

        // Salvar usuário
        try {
            usuarioService.salvarUsuario(usuario);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Usuário '" + usuario.getNome() + "' cadastrado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar usuário: " + e.getMessage());
        }

        return "redirect:/usuarios/listar";
    }

    @GetMapping("/listar")
    public String listarUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Gerenciamento de Usuários");
        return "usuarios/listar";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return "redirect:/usuarios/listar";
        }
        model.addAttribute("usuario", usuario);
        model.addAttribute("titulo", "Editar Usuário");
        return "usuarios/editar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizarUsuario(@PathVariable Integer id,
            @ModelAttribute Usuario usuario,
            @RequestParam(value = "senha", required = false) String novaSenha,
            @RequestParam(value = "confirmarSenha", required = false) String confirmarSenha,
            RedirectAttributes redirectAttributes) {

        try {
            Usuario usuarioExistente = usuarioService.buscarPorId(id);
            if (usuarioExistente == null) {
                redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado!");
                return "redirect:/usuarios/listar";
            }

            // Verificar se email já existe (excluindo o próprio usuário)
            if (!usuarioExistente.getEmail().equals(usuario.getEmail())
                    && usuarioService.existePorEmail(usuario.getEmail())) {
                redirectAttributes.addFlashAttribute("erro", "Este email já está cadastrado!");
                return "redirect:/usuarios/editar/" + id;
            }

            // Validar senha se for alterada
            if (novaSenha != null && !novaSenha.isEmpty()) {
                if (novaSenha.length() < 6) {
                    redirectAttributes.addFlashAttribute("erro", "Senha deve ter no mínimo 6 caracteres!");
                    return "redirect:/usuarios/editar/" + id;
                }

                if (!novaSenha.equals(confirmarSenha)) {
                    redirectAttributes.addFlashAttribute("erro", "As senhas não coincidem!");
                    return "redirect:/usuarios/editar/" + id;
                }

                usuario.setSenha(novaSenha);
            } else {
                // Manter a senha existente
                usuario.setSenha(usuarioExistente.getSenha());
            }

            // Manter outros campos
            usuario.setId(id);
            usuario.setCreatedAt(usuarioExistente.getCreatedAt());

            usuarioService.salvarUsuario(usuario);
            redirectAttributes.addFlashAttribute("sucesso",
                    "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao atualizar usuário: " + e.getMessage());
        }

        return "redirect:/usuarios/listar";
    }

    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.excluirUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}
