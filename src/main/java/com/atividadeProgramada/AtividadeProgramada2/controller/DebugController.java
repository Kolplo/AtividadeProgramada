package com.atividadeProgramada.AtividadeProgramada2.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atividadeProgramada.AtividadeProgramada2.infra.security.TokenService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final TokenService tokenService;

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> authDebug(HttpServletRequest request) {
        Map<String, Object> out = new HashMap<>();

   
        String token = null;
        if (request.getCookies() != null) {
            for (Cookie c : request.getCookies()) {
                if ("jwt".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }
        out.put("cookiePresent", token != null);
        out.put("tokenLength", token != null ? token.length() : 0);


        String email = null;
        if (token != null) {
            try {
                email = tokenService.vaidateToken(token);
                out.put("tokenValid", email != null);
                out.put("validatedEmail", email);
            } catch (Exception e) {
                out.put("tokenValid", false);
                out.put("validationError", e.getMessage());
            }
        } else {
            out.put("tokenValid", false);
        }

  
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        out.put("securityContextAuthPresent", auth != null && auth.isAuthenticated());
        if (auth != null && auth.getPrincipal() != null) {
            out.put("principalClass", auth.getPrincipal().getClass().getName());
            out.put("principalToString", auth.getPrincipal().toString());
        }

        return ResponseEntity.ok(out);
    }
}
