package com.atividadeProgramada.AtividadeProgramada2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Professor {
   //
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
//comentario
    private String nome;
    @Column(unique = true)
    private String email;
    private String senha;

    private String role = "ADMIN"; // usuário administrador
}
