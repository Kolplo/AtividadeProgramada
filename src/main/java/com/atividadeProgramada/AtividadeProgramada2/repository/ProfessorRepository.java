package com.atividadeProgramada.AtividadeProgramada2.repository;

import com.atividadeProgramada.AtividadeProgramada2.entity.Professor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProfessorRepository extends JpaRepository<Professor, String> {
    Optional<Professor> findByNome(String nome);

}
//comenatrio jsdhkchdkchldhbdhcbdhch