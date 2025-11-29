package com.atividadeProgramada.AtividadeProgramada2.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atividadeProgramada.AtividadeProgramada2.dto.User.UpdateUserRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository uRepository;
    private final PasswordEncoder passwordEncoder;

    public void altSenha(UpdateUserRequestDTO dto, Usuario user) {
        user.setPassword(passwordEncoder.encode(dto.senhan()));
        uRepository.save(user);
    }

    public void altcampos(String campo, String valor, Usuario user) {
        switch (campo) {
            case "nome" -> user.setNome(valor);
            case "email" -> user.setEmail(valor);
            default -> throw new RuntimeException("Campo inválido: " + campo);
        }

        uRepository.save(user);
    }

    public void deleteconta(Usuario user) {
        uRepository.delete(user);
    }

}
