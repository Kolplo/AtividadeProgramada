package com.atividadeProgramada.AtividadeProgramada2.dto.Aluno;

public class CreateAlunoRequestDTO {
    private String nome;
    private String idade;
    private String serie;
    private String nomeresponsavel;
    private String contatoresponsavel;

    public CreateAlunoRequestDTO() {}

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
