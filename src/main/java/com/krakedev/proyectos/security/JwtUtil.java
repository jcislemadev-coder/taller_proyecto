package com.krakedev.proyectos.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtUtil {
	private final Algorithm algorithm;

	public JwtUtil(@Value("${jwt.secret}") String secret) {
		this.algorithm = Algorithm.HMAC256(secret);
	}
	
	public String generarToken(String username) {
		
		return JWT.create().withSubject(username).withIssuedAt(new Date()).withExpiresAt(new Date(System.currentTimeMillis()+3600000)).sign(algorithm);
		
	}
	
	public boolean validarToken(String token) {
		try {
			JWT.require(algorithm).build().verify(token);
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
}
