package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.AlunoResponseDTO;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.CreateAlunoRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.UpdateAlunoRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.service.AlunoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aluno")
@RequiredArgsConstructor
public class AlunoController {
    
    private final AlunoService alunoService;
    
    @GetMapping
    public String listarAlunos(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<AlunoResponseDTO> alunos = alunoService.getAllAlunosByUsuario(usuario);
        model.addAttribute("alunos", alunos);
        return "aluno/aluno_lista";
    }
    
    @GetMapping("/{id}")
    public String detalharAluno(@PathVariable Long id, Authentication authentication, Model model) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        AlunoResponseDTO aluno = alunoService.getAlunoById(id, usuario);
        model.addAttribute("aluno", aluno);
        return "aluno/aluno_detalhes";
    }
    
    @GetMapping("/novo")
    public String novoAluno(Model model) {
        model.addAttribute("dto", new CreateAlunoRequestDTO());
        return "aluno/aluno_cadastro";
    }
    
    @PostMapping("/criar")
    public String criarAluno(@ModelAttribute CreateAlunoRequestDTO dto, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        alunoService.createAluno(dto, usuario);
        return "redirect:/aluno";
    }
    
    @GetMapping("/editar/{id}")
    public String editarAluno(@PathVariable Long id, Authentication authentication, Model model) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        AlunoResponseDTO aluno = alunoService.getAlunoById(id, usuario);
        model.addAttribute("aluno", aluno);
        return "aluno/aluno_editar";
    }
    
    @PutMapping("/{id}")
    public String atualizarAluno(@PathVariable Long id, @ModelAttribute UpdateAlunoRequestDTO dto, 
            Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        dto.setId(id);
        alunoService.updateAluno(dto, usuario);
        return "redirect:/aluno";
    }

    // Support form submissions that post to the resource path (some setups don't convert _method)
    @PostMapping("/{id}")
    public String atualizarAlunoPost(@PathVariable Long id, @ModelAttribute UpdateAlunoRequestDTO dto,
                                     Authentication authentication) {
        return atualizarAluno(id, dto, authentication);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAluno(@PathVariable Long id, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        alunoService.deleteAluno(id, usuario);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/search")
    public String pesquisarAluno(@RequestParam String nome, Model model) {
        List<AlunoResponseDTO> alunos = alunoService.searchAlunoByNome(nome);
        model.addAttribute("alunos", alunos);
        return "aluno/aluno_lista";
    }
}
