package com.atividadeProgramada.AtividadeProgramada2.security;

import java.security.Key;
import java.util.Date;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


public class JwtUtil {
  
      private static final Key Key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
      private static final long EXPIRATION_TIME =86400000;
      
      public static String generateToken(String username){
        return Jwts.builder()
        .setSubject(username)
        .setExpiration(new Date(System.currentTimeMillis()+ EXPIRATION_TIME))
        .signWith(Key, SignatureAlgorithm.HS256)
        .compact();
      }

      public static String extractUsername(String token){
        return Jwts.parserBuilder().setSigningKey(Key).build().parseClaimsJws(token).getBody().getSubject();
        
      }

      public static boolean validateToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(Key).build().parseClaimsJws(token);
            return true;
        }
        catch(JwtException e) {
            return false;
        }
      }

}
