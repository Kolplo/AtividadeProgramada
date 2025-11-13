package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.repository.AlunoRepository;
import com.atividadeProgramada.AtividadeProgramada2.service.AlunoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aluno")
@AllArgsConstructor
public class AlunoContrroller {
    private final AlunoService alunoService;

    @GetMapping
    public ResponseEntity<List<Aluno>> ListarAlunos() {
        List<Aluno> aluno = alunoService.ListarAluno();
        return ResponseEntity.ok(aluno);
    }

   @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarIdAluno(@PathVariable String id) {
       return alunoService.buscarIdAluno(id)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
   }

   @PostMapping
    public ResponseEntity<Aluno> salvarAluno(@RequestBody Aluno aluno) {
        Aluno novoAluno = alunoService.SalvarAluno(aluno);
       return ResponseEntity.status(HttpStatus.CREATED).body(novoAluno);
   }


   @PutMapping
    public ResponseEntity<?> atualizarAluno(@RequestBody Aluno aluno) {
        Aluno alterarAluno = alunoService.AtualizarAluno(aluno);
        return ResponseEntity.ok(alterarAluno);
   }
}
