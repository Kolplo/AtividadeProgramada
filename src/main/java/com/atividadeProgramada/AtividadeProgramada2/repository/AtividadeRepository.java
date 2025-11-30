package com.atividadeProgramada.AtividadeProgramada2.repository;

import com.atividadeProgramada.AtividadeProgramada2.entity.Atividade;
import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AtividadeRepository extends JpaRepository<Atividade, Long> {
    
    List<Atividade> findByAluno(Aluno aluno);
    
    List<Atividade> findByAlunoId(Long alunoId);
    
    List<Atividade> findByUsuario(com.atividadeProgramada.AtividadeProgramada2.entity.Usuario usuario);

    List<Atividade> findByUsuarioId(Long usuarioId);
    
    List<Atividade> findByUsuarioIdOrderByDataDesc(Long usuarioId);
}
