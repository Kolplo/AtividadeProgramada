package com.atividadeProgramada.AtividadeProgramada2.dto.User.aluno;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;

public record AlunoResponseDTO(
        Long id,
        String nome,
        String idade,
        String serie,
        String nomeresponsavel,
        String contatoresponsavel
) {

    public static AlunoResponseDTO fromEntity(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId(),
                aluno.getNome(),
                aluno.getIdade(),
                aluno.getSerie(),
                aluno.getNomeresponsavel(),
                aluno.getContatoresponsavel()
        );
    }
}
