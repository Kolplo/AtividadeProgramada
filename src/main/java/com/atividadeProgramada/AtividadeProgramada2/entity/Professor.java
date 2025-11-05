package com.atividadeProgramada.AtividadeProgramada2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String email;
    private String telefone;


}
