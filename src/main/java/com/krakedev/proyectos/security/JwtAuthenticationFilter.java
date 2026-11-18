package com.krakedev.proyectos.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;
import com.krakedev.proyectos.services.TokenBlacklistService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenBlacklistService tokenBlackListService;

	private final JwtUtil jwtUtil;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public JwtAuthenticationFilter(TokenBlacklistService tokenBlackListService, JwtUtil jwtUtil) {
		this.tokenBlackListService = tokenBlackListService;
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {

			filterChain.doFilter(request, response);
			return;
		}

		String token = authHeader.substring(7);

		if (tokenBlackListService.estaInvalidado(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		if (!jwtUtil.validarToken(token)) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}

		String username = JWT.decode(token).getSubject();

		Usuario usuario = usuarioRepository.findByUsername(username).orElse(null);

		if (usuario != null) {
		    String rol = usuario.getRol();

		    UsernamePasswordAuthenticationToken authentication =
		        new UsernamePasswordAuthenticationToken(
		            username,
		            null,
		            List.of(new SimpleGrantedAuthority("ROLE_" + rol))
		        );

		    SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		filterChain.doFilter(request, response);
	}

}
