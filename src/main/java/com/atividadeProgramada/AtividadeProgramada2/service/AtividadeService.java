package com.atividadeProgramada.AtividadeProgramada2.service;

import com.atividadeProgramada.AtividadeProgramada2.entity.Atividade;
import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.repository.AtividadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AtividadeService {
    
    private final AtividadeRepository atividadeRepository;
    
    public Atividade createAtividade(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }
    
    public Atividade updateAtividade(Atividade atividade) {
        return atividadeRepository.save(atividade);
    }
    
    public Atividade getAtividadeById(Long id) {
        return atividadeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Atividade não encontrada"));
    }
    
    public List<Atividade> getAtividadesByAluno(Aluno aluno) {
        return atividadeRepository.findByAluno(aluno);
    }
    
    public List<Atividade> getAtividadesByAlunoId(Long alunoId) {
        return atividadeRepository.findByAlunoId(alunoId);
    }
    
    public void deleteAtividade(Long id) {
        atividadeRepository.deleteById(id);
    }
}
