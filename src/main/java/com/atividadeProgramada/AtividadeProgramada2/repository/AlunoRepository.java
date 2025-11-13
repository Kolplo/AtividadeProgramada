package com.atividadeProgramada.AtividadeProgramada2.repository;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, String> { }
