package com.atividadeProgramada.AtividadeProgramada2.service;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.entity.Professor;
import com.atividadeProgramada.AtividadeProgramada2.repository.AlunoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AlunoService {
    private final AlunoRepository alunoRepository;

    public List<Aluno> ListarAluno(){
        return alunoRepository.findAll();
    }

    public Aluno SalvarAluno (Aluno aluno){
        return alunoRepository.save(aluno);
    }

    public Aluno AtualizarAluno (Aluno aluno){
        return alunoRepository.findById(aluno.getId())
                .map( a -> {
                    a.setNome(aluno.getNome());
                    a.setIdade(aluno.getIdade());
                    a.setSexo(aluno.getSexo());
                    return alunoRepository.save(a);
        })
                .orElseThrow(() -> new RuntimeException("aluno não encontrado"));
    }
    public Optional<Aluno> buscarIdAluno(String id) {
        return alunoRepository.findById(id);
    }

}
