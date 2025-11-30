package com.atividadeProgramada.AtividadeProgramada2.service;

import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.AlunoResponseDTO;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.CreateAlunoRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.dto.Aluno.UpdateAlunoRequestDTO;
import com.atividadeProgramada.AtividadeProgramada2.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {
    
    private final AlunoRepository alunoRepository;
    
    public AlunoResponseDTO createAluno(CreateAlunoRequestDTO dto, Usuario usuario) {
        Aluno aluno = new Aluno();
        aluno.setNome(dto.getNome());
        aluno.setIdade(dto.getIdade());
        aluno.setSerie(dto.getSerie());
        aluno.setNomeresponsavel(dto.getNomeresponsavel());
        aluno.setContatoresponsavel(dto.getContatoresponsavel());
        aluno.setUsuario(usuario);
        
        Aluno savedAluno = alunoRepository.save(aluno);
        return mapToDTO(savedAluno);
    }
    
    public AlunoResponseDTO updateAluno(UpdateAlunoRequestDTO dto, Usuario usuario) {
        Aluno aluno = alunoRepository.findByIdAndUsuario(dto.getId(), usuario)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        aluno.setNome(dto.getNome());
        aluno.setIdade(dto.getIdade());
        aluno.setSerie(dto.getSerie());
        aluno.setNomeresponsavel(dto.getNomeresponsavel());
        aluno.setContatoresponsavel(dto.getContatoresponsavel());
        
        Aluno updatedAluno = alunoRepository.save(aluno);
        return mapToDTO(updatedAluno);
    }
    
    public AlunoResponseDTO getAlunoById(Long id, Usuario usuario) {
        Aluno aluno = alunoRepository.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        return mapToDTO(aluno);
    }
    
    public List<AlunoResponseDTO> getAllAlunosByUsuario(Usuario usuario) {
        List<Aluno> alunos = alunoRepository.findByUsuario(usuario);
        return alunos.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    public void deleteAluno(Long id, Usuario usuario) {
        Aluno aluno = alunoRepository.findByIdAndUsuario(id, usuario)
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        alunoRepository.delete(aluno);
    }
    
    public List<AlunoResponseDTO> searchAlunoByNome(String nome) {
        List<Aluno> alunos = alunoRepository.findByNomeContainingIgnoreCase(nome);
        return alunos.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }
    
    private AlunoResponseDTO mapToDTO(Aluno aluno) {
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
