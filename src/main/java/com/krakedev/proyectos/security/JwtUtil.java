package com.krakedev.proyectos.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET_KEY = "KrakeDev_Secret_Key_Proyectos_2026_Segura!";
    private static final long EXPIRATION_TIME = 3600000; // 1 hora en milisegundos

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SECRET_KEY);
    }

    public String generarToken(String username, String rol) {
        return JWT.create()
                .withSubject(username)
                .withClaim("rol", rol)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(getAlgorithm());
    }

    public DecodedJWT validarToken(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token);
    }

    public String extraerUsername(String token) {
        return validarToken(token).getSubject();
    }

    public String extraerRol(String token) {
        return validarToken(token).getClaim("rol").asString();
    }
}