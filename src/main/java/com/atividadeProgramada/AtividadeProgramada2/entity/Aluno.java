package com.atividadeProgramada.AtividadeProgramada2.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "aluno")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "idade", nullable = false, unique = false)
    private String idade;

    @Column(name = "serie", nullable = true)
    private String serie;

    @Column(name = "nomeresponsavel", nullable = true)
    private String nomeresponsavel;

    @Column(name = "contatoresponsavel", nullable = true)
    private String contatoresponsavel;
      
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atividade> atividades = new ArrayList<>();

}
/*
atributos de propriedade

Long id
String nome
String codigoRural
String areaTotal
String tipoSolo
String tipoProducao
String tipoCultura
String fone
Cidade cidade
Usuario usuario
List<Grao> graos
*/