package com.atividadeProgramada.AtividadeProgramada2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    Optional<Aluno> findByIdAndUsuario(Long id, Usuario usuario);
    
    List<Aluno> findByUsuario(Usuario usuario);
    
    List<Aluno> findByNomeContainingIgnoreCase(String nome);
}
