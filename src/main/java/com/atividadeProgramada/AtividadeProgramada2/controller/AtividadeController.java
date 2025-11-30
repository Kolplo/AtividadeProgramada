package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Atividade;
import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.repository.AtividadeRepository;
import com.atividadeProgramada.AtividadeProgramada2.repository.AlunoRepository;
import com.atividadeProgramada.AtividadeProgramada2.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/atividade")
@RequiredArgsConstructor
public class AtividadeController {
    
    private final AtividadeService atividadeService;
    private final AlunoRepository alunoRepository;
    private final AtividadeRepository atividadeRepository;
    
    @GetMapping
    public String listarAtividades(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Aluno> alunos = alunoRepository.findByUsuario(usuario);
        model.addAttribute("alunos", alunos);
        model.addAttribute("atividades", atividadeRepository.findAll());
        return "atividade/atividade_lista";
    }
    
    @PostMapping
    public ResponseEntity<?> criarAtividade(@RequestBody AtividadeRequest request, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        
        Aluno aluno = alunoRepository.findById(Long.parseLong(request.alunoId))
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        Atividade atividade = new Atividade();
        atividade.setAluno(aluno);
        atividade.setDescricao(request.descricao);
        atividade.setData(LocalDateTime.parse(request.data));
        atividade.setStatus(request.status != null ? request.status : "Pendente");
        atividade.setUsuario(usuario);
        
        atividadeService.createAtividade(atividade);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable Long id) {
        atividadeService.deleteAtividade(id);
        return ResponseEntity.ok().build();
    }
    
    // Request DTO
    public static class AtividadeRequest {
        public String alunoId;
        public String descricao;
        public String data;
        public String status;
    }
}
