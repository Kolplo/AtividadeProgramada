package com.atividadeProgramada.AtividadeProgramada2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository <Usuario, Long> {
    Optional<Usuario> findByEmail(String email);   
}
