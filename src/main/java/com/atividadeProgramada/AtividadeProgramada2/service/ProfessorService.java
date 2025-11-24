package com.atividadeProgramada.AtividadeProgramada2.service;

import com.atividadeProgramada.AtividadeProgramada2.entity.Professor;
import com.atividadeProgramada.AtividadeProgramada2.repository.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    //listar professor
    public List<Professor> listarProfessor() {
        return professorRepository.findAll();
    }
    //buscar professor pelo id
    public Optional<Professor> buscarIdProfessor(String id) {
        return professorRepository.findById(id);
    }

    public Professor salvarProfessor(Professor professor) {
        return professorRepository.save(professor);

    }

    public void excluirProfessor(String id) {
        professorRepository.deleteById(id);
    }

  public Professor atualizarProfessor(Professor professor) {
        return professorRepository.findById(professor.getId())
                .map(p -> {
                    p.setNome(professor.getNome());
                    p.setEmail(professor.getEmail());
                    p.setSenha(professor.getSenha());
                    return professorRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));


    }

   public Optional<Professor> buscarProfessorEmail(String email) {
        return professorRepository.findByEmail(email);

    }
}