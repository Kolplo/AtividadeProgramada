package com.atividadeProgramada.AtividadeProgramada2.dto.Aluno;

public class UpdateAlunoRequestDTO {
    private Long id;
    private String nome;
    private String idade;
    private String serie;
    private String nomeresponsavel;
    private String contatoresponsavel;

    public UpdateAlunoRequestDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getIdade() { return idade; }
    public void setIdade(String idade) { this.idade = idade; }

    public String getSerie() { return serie; }
    public void setSerie(String serie) { this.serie = serie; }

    public String getNomeresponsavel() { return nomeresponsavel; }
    public void setNomeresponsavel(String nomeresponsavel) { this.nomeresponsavel = nomeresponsavel; }

    public String getContatoresponsavel() { return contatoresponsavel; }
    public void setContatoresponsavel(String contatoresponsavel) { this.contatoresponsavel = contatoresponsavel; }
}
