package com.cenaflixpodcast.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.cenaflixpodcast.model.Usuario;
import com.cenaflixpodcast.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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

    // PÁGINA DE CADASTRO - liberada para todos (não precisa estar logado)
    @GetMapping("/novo")
    public String showNovoUsuario(Model model, Authentication auth) {
        // Se já estiver logado, redireciona para dashboard
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            return "redirect:/dashboard";
        }
        
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("titulo", "Novo Usuário");
        return "usuarios/novo";
    }

    // SALVAR USUÁRIO - liberado para todos (qualquer um pode se cadastrar)
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

        // Verificar se email já existe
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
                    "Usuário '" + usuario.getNome() + "' cadastrado com sucesso! Faça login para continuar.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro",
                    "Erro ao cadastrar usuário: " + e.getMessage());
        }

        return "redirect:/login";
    }

    // LISTAR USUÁRIOS - apenas ADMIN
    @GetMapping("/listar")
    public String listarUsuarios(Model model, Authentication auth) {
        Usuario usuarioLogado = usuarioService.buscarPorNome(auth.getName());
        if (usuarioLogado == null || !"ADMIN".equals(usuarioLogado.getPerfil())) {
            return "redirect:/dashboard?erro=acesso_negado";
        }
        
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("titulo", "Gerenciamento de Usuários");
        return "usuarios/listar";
    }

    // EDITAR USUÁRIO - ADMIN pode editar qualquer, EDITOR pode editar a si mesmo
    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Integer id, Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = usuarioService.buscarPorNome(auth.getName());
        Usuario usuarioEditando = usuarioService.buscarPorId(id);
        
        if (usuarioLogado == null) {
            redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado!");
            return "redirect:/dashboard";
        }
        
        if (usuarioEditando == null) {
            redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado!");
            return "redirect:/usuarios/listar";
        }
        
        boolean isAdmin = "ADMIN".equals(usuarioLogado.getPerfil());
        boolean isEditingSelf = usuarioLogado.getId().equals(id);
        
        if (!isAdmin && !isEditingSelf) {
            redirectAttributes.addFlashAttribute("erro", "Você não tem permissão para editar este usuário!");
            return "redirect:/dashboard";
        }
        
        model.addAttribute("usuario", usuarioEditando);
        model.addAttribute("titulo", "Editar Usuário");
        return "usuarios/editar";
    }

    // ATUALIZAR USUÁRIO - ADMIN pode atualizar qualquer, EDITOR pode atualizar a si mesmo
    @PostMapping("/atualizar/{id}")
    public String atualizarUsuario(@PathVariable Integer id,
            @ModelAttribute Usuario usuario,
            @RequestParam(value = "senha", required = false) String novaSenha,
            @RequestParam(value = "confirmarSenha", required = false) String confirmarSenha,
            RedirectAttributes redirectAttributes,
            Authentication auth) {

        try {
            Usuario usuarioLogado = usuarioService.buscarPorNome(auth.getName());
            Usuario usuarioExistente = usuarioService.buscarPorId(id);
            
            if (usuarioLogado == null) {
                redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado!");
                return "redirect:/dashboard";
            }
            
            if (usuarioExistente == null) {
                redirectAttributes.addFlashAttribute("erro", "Usuário não encontrado!");
                return "redirect:/usuarios/listar";
            }
            
            boolean isAdmin = "ADMIN".equals(usuarioLogado.getPerfil());
            boolean isEditingSelf = usuarioLogado.getId().equals(id);
            
            if (!isAdmin && !isEditingSelf) {
                redirectAttributes.addFlashAttribute("erro", "Acesso negado!");
                return "redirect:/dashboard";
            }

            // Se não for ADMIN, não pode alterar o perfil
            if (!isAdmin && usuario.getPerfil() != null && !usuario.getPerfil().equals(usuarioExistente.getPerfil())) {
                usuario.setPerfil(usuarioExistente.getPerfil());
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
                usuario.setSenha(usuarioExistente.getSenha());
            }

            usuario.setId(id);
            usuario.setCreatedAt(usuarioExistente.getCreatedAt());

            usuarioService.salvarUsuario(usuario);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar usuário: " + e.getMessage());
        }

        return "redirect:/usuarios/listar";
    }

    // EXCLUIR USUÁRIO - apenas ADMIN
    @GetMapping("/excluir/{id}")
    public String excluirUsuario(@PathVariable Integer id, RedirectAttributes redirectAttributes, Authentication auth) {
        Usuario usuarioLogado = usuarioService.buscarPorNome(auth.getName());
        if (usuarioLogado == null || !"ADMIN".equals(usuarioLogado.getPerfil())) {
            redirectAttributes.addFlashAttribute("erro", "Acesso negado!");
            return "redirect:/dashboard";
        }
        
        try {
            usuarioService.excluirUsuario(id);
            redirectAttributes.addFlashAttribute("sucesso", "Usuário excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir usuário: " + e.getMessage());
        }
        return "redirect:/usuarios/listar";
    }
}
