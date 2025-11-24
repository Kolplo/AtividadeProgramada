package com.atividadeProgramada.AtividadeProgramada2.service;

import com.atividadeProgramada.AtividadeProgramada2.entity.Professor;
import com.atividadeProgramada.AtividadeProgramada2.repository.ProfessorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private ProfessorRepository professorRepository;
  //  private PasswordEncoder passwordEncoder;

 /*   public Professor login(String email, String senha) {
        Professor prof = professorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        if (!passwordEncoder.matches(senha, prof.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        return prof;
    }*/
}
