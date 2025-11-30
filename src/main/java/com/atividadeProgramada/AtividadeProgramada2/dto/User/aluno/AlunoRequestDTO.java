package com.atividadeProgramada.AtividadeProgramada2.dto.User.aluno;

public record AlunoRequestDTO(
                String nome,
                String idade,
                String areaTotal,
                String tipoSolo,
                String tipoProducao,
                String tipoCultura,
                String fone,
                Long cidadeId) {
}
