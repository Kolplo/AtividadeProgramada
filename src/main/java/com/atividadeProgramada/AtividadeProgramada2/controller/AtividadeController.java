package com.atividadeProgramada.AtividadeProgramada2.controller;

import com.atividadeProgramada.AtividadeProgramada2.entity.Atividade;
import com.atividadeProgramada.AtividadeProgramada2.entity.Aluno;
import com.atividadeProgramada.AtividadeProgramada2.entity.Usuario;
import com.atividadeProgramada.AtividadeProgramada2.repository.AtividadeRepository;
import com.atividadeProgramada.AtividadeProgramada2.repository.AlunoRepository;
import com.atividadeProgramada.AtividadeProgramada2.service.AtividadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Controller
@RequestMapping("/atividade")
@RequiredArgsConstructor
public class AtividadeController {
    private static final Logger logger = LoggerFactory.getLogger(AtividadeController.class);
    
    private final AtividadeService atividadeService;
    private final AlunoRepository alunoRepository;
    private final AtividadeRepository atividadeRepository;
    // In-memory cache of atividades per usuario (thread-safe)
    private final Map<Long, CopyOnWriteArrayList<Atividade>> atividadesCache = new ConcurrentHashMap<>();
    
    @GetMapping
    public String listarAtividades(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        List<Aluno> alunos = alunoRepository.findByUsuario(usuario);
        // Load from in-memory cache if present, otherwise initialize from DB
        CopyOnWriteArrayList<Atividade> atividades = atividadesCache.computeIfAbsent(
            usuario.getId(), id -> new CopyOnWriteArrayList<>(atividadeRepository.findByUsuario(usuario))
        );
        logger.debug("[listarAtividades] usuario.id={} alunos.count={} atividades.count={}",
                usuario != null ? usuario.getId() : null,
                alunos != null ? alunos.size() : 0,
                atividades != null ? atividades.size() : 0);
        if (atividades != null) {
            atividades.forEach(a -> logger.debug("[listarAtividades] atividade id={} aluno_id={} usuario_id={}", a.getId(), a.getAluno() != null ? a.getAluno().getId() : null, a.getUsuario() != null ? a.getUsuario().getId() : null));
        }
        model.addAttribute("alunos", alunos);
        model.addAttribute("atividades", atividades);
        // counts for dashboard
        int total = atividades != null ? atividades.size() : 0;
        long concluidas = 0;
        if (atividades != null) {
            concluidas = atividades.stream()
                    .filter(a -> a.getStatus() != null && a.getStatus().toLowerCase().contains("concl"))
                    .count();
        }
        model.addAttribute("totalAtividades", total);
        model.addAttribute("concluidas", concluidas);
        return "atividade/atividade_lista";
    }

    // Debug endpoint: returns atividades for current user as JSON
    @GetMapping("/debug/json")
    @ResponseBody
    public ResponseEntity<?> atividadesJson(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        var atividades = atividadeRepository.findByUsuario(usuario);
        return ResponseEntity.ok(atividades);
    }
    
    @PostMapping
    public ResponseEntity<?> criarAtividade(@RequestBody AtividadeRequest request, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        logger.debug("[criarAtividade] criando atividade para usuario.id={}", usuario != null ? usuario.getId() : null);

        Aluno aluno = alunoRepository.findById(Long.parseLong(request.alunoId))
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Atividade atividade = new Atividade();
        atividade.setAluno(aluno);
        atividade.setDescricao(request.descricao);

        // Parse date string safely: accept ISO_LOCAL_DATE (yyyy-MM-dd) or ISO_LOCAL_DATE_TIME
        try {
            if (request.data == null || request.data.isBlank()) {
                atividade.setData(LocalDateTime.now());
            } else {
                // try parse as LocalDateTime first
                try {
                    atividade.setData(LocalDateTime.parse(request.data));
                } catch (Exception e) {
                    // fallback to parse as LocalDate and set start of day
                    java.time.LocalDate ld = java.time.LocalDate.parse(request.data);
                    atividade.setData(ld.atStartOfDay());
                }
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Formato de data inválido: " + request.data);
        }

        atividade.setStatus(request.status != null ? request.status : "Pendente");
        atividade.setUsuario(usuario);

        Atividade saved = atividadeService.createAtividade(atividade);
        logger.debug("[criarAtividade] atividade salva id={} usuarioId={}", saved != null ? saved.getId() : null, usuario != null ? usuario.getId() : null);
        // add to in-memory cache
        if (saved != null && usuario != null && usuario.getId() != null) {
            atividadesCache.computeIfAbsent(usuario.getId(), id -> new CopyOnWriteArrayList<>()).add(saved);
        }
        return ResponseEntity.ok().build();
    }

    // Accept regular form submissions from the activity form (application/x-www-form-urlencoded)
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String criarAtividadeForm(@RequestParam String alunoId,
                                     @RequestParam String descricao,
                                     @RequestParam(required = false) String data,
                                     @RequestParam(required = false) String status,
                                     Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();

        Aluno aluno = alunoRepository.findById(Long.parseLong(alunoId))
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        Atividade atividade = new Atividade();
        atividade.setAluno(aluno);
        atividade.setDescricao(descricao);
        try {
            if (data == null || data.isBlank()) {
                atividade.setData(LocalDateTime.now());
            } else {
                try {
                    atividade.setData(LocalDateTime.parse(data));
                } catch (Exception e) {
                    java.time.LocalDate ld = java.time.LocalDate.parse(data);
                    atividade.setData(ld.atStartOfDay());
                }
            }
        } catch (Exception ex) {
            return "redirect:/atividade?error=invalid_date";
        }
        atividade.setStatus(status != null ? status : "Pendente");
        atividade.setUsuario(usuario);
        Atividade saved = atividadeService.createAtividade(atividade);
        if (saved != null && usuario != null && usuario.getId() != null) {
            atividadesCache.computeIfAbsent(usuario.getId(), id -> new CopyOnWriteArrayList<>()).add(saved);
        }
        return "redirect:/atividade";
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarAtividade(@PathVariable Long id) {
        // attempt to remove from cache for the owning user
        try {
            Atividade existing = atividadeService.getAtividadeById(id);
            Long usuarioId = existing != null && existing.getUsuario() != null ? existing.getUsuario().getId() : null;
            atividadeService.deleteAtividade(id);
            if (usuarioId != null) {
                var list = atividadesCache.get(usuarioId);
                if (list != null) {
                    list.removeIf(a -> a.getId() != null && a.getId().equals(id));
                }
            }
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity.status(500).body("Erro ao deletar atividade: " + ex.getMessage());
        }
    }

    // Temporary test endpoint to create a sample atividade for the authenticated user
    @GetMapping("/test-create")
    public ResponseEntity<?> testarCriacao(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        // try to find any aluno for this user
        var alunos = alunoRepository.findByUsuario(usuario);
        if (alunos == null || alunos.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não possui alunos cadastrados para vincular a atividade");
        }
        Aluno aluno = alunos.get(0);
        Atividade atividade = new Atividade();
        atividade.setAluno(aluno);
        atividade.setDescricao("Atividade de teste criada em " + java.time.LocalDateTime.now());
        atividade.setData(java.time.LocalDateTime.now());
        atividade.setStatus("Pendente");
        atividade.setUsuario(usuario);
        atividadeService.createAtividade(atividade);
        return ResponseEntity.ok("Atividade de teste criada com sucesso");
    }

    // Diagnostic endpoint: raw count and list of atividades for authenticated user
    @GetMapping("/diag")
    public ResponseEntity<?> diagnostico(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Usuário não autenticado");
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        java.util.Map<String, Object> diag = new java.util.HashMap<>();
        diag.put("usuario_id", usuario.getId());
        diag.put("usuario_email", usuario.getEmail());
        
        // Try to count all atividades in DB (without user filter)
        long totalAtividades = atividadeRepository.count();
        diag.put("total_atividades_db", totalAtividades);
        
        // Try findByUsuario
        var atividadesPorUsuario = atividadeRepository.findByUsuario(usuario);
        diag.put("atividades_por_usuario_count", atividadesPorUsuario != null ? atividadesPorUsuario.size() : 0);
        diag.put("atividades_por_usuario", atividadesPorUsuario);
        
        return ResponseEntity.ok(diag);
    }

    @GetMapping("/editar/{id}")
    public String editarAtividade(@PathVariable Long id, Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth";
        }
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Atividade atividade = atividadeService.getAtividadeById(id);
        if (!atividade.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/atividade?error=forbidden";
        }

        List<Aluno> alunos = alunoRepository.findByUsuario(usuario);
        model.addAttribute("atividade", atividade);
        model.addAttribute("alunos", alunos);
        return "atividade/atividade_editar";
    }

    @PutMapping("/{id}")
    public String atualizarAtividade(@PathVariable Long id, @RequestBody AtividadeRequest request, Authentication authentication) {
        Usuario usuario = (Usuario) authentication.getPrincipal();
        Atividade existing = atividadeService.getAtividadeById(id);
        if (!existing.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/atividade?error=forbidden";
        }

        Aluno aluno = alunoRepository.findById(Long.parseLong(request.alunoId))
            .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        existing.setAluno(aluno);
        existing.setDescricao(request.descricao);
        try {
            if (request.data == null || request.data.isBlank()) {
                existing.setData(LocalDateTime.now());
            } else {
                try {
                    existing.setData(LocalDateTime.parse(request.data));
                } catch (Exception e) {
                    java.time.LocalDate ld = java.time.LocalDate.parse(request.data);
                    existing.setData(ld.atStartOfDay());
                }
            }
        } catch (Exception ex) {
            return "redirect:/atividade?error=invalid_date";
        }
        existing.setStatus(request.status != null ? request.status : existing.getStatus());
        atividadeService.updateAtividade(existing);
        // update in cache
        try {
            Long uId = existing.getUsuario() != null ? existing.getUsuario().getId() : null;
            if (uId != null) {
                var list = atividadesCache.get(uId);
                if (list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        Atividade a = list.get(i);
                        if (a.getId() != null && a.getId().equals(existing.getId())) {
                            list.set(i, existing);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("[atualizarAtividade] não foi possível atualizar cache: {}", e.getMessage());
        }
        return "redirect:/atividade";
    }

    @PostMapping("/{id}")
    public String atualizarAtividadePost(@PathVariable Long id, @ModelAttribute("atividade") AtividadeRequest request, Authentication authentication) {
        // delegate to PUT handler by converting AtividadeRequest to same structure
        return atualizarAtividade(id, request, authentication);
    }
    
    // Request DTO
    public static class AtividadeRequest {
        public String alunoId;
        public String descricao;
        public String data;
        public String status;
    }
}
