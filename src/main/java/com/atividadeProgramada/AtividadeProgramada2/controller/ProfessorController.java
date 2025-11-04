package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Professor;
import com.atividadeProgramada.AtividadeProgramada2.service.ProfessorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/api")
@AllArgsConstructor
public class ProfessorController {
    private final ProfessorService professorService;

    //mostra todos os professsores
    @GetMapping
    public List<Professor> ListarProfessor() {
        return professorService.listarProfessor();
    }

    //busca o professor pelo id
    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarProfessor(@PathVariable String id) {
        return professorService.buscarIdProfessor(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //Adciona novos prfessores ao bd
    @PostMapping
    public ResponseEntity<Professor> adicionarProfessor(@RequestBody Professor professor) {
        Professor novoProfessor = professorService.salvarProfessor(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProfessor);
    }

    //deletar professor por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarProfessor(@PathVariable String id) {
        professorService.excluirProfessor(id);
        return ResponseEntity.ok().build();
    }

    //atualizar os dados, vulgo alterar dados
    @PutMapping
    public ResponseEntity<?> atualizarProfessor(@RequestBody Professor professor) {
        Professor alterarProfessor = professorService.atualizarProfessor(professor);
        return ResponseEntity.ok(alterarProfessor);
    }

    @GetMapping("/{nome}")
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome) {
        return professorService.buscarProfessorNome(nome)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}












