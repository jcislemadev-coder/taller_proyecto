package com.krakedev.proyectos.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.security.JwtUtil;
import com.krakedev.proyectos.services.TokenBlacklistService;
import com.krakedev.proyectos.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	private final UsuarioService usuarioService;
	private final JwtUtil jwtUtil;
	private final TokenBlacklistService tokenBlackListService;
	
	
	public AuthController(UsuarioService usuarioService,  TokenBlacklistService tokenBlackListService, JwtUtil jwtUtil) {
		super();
		this.usuarioService = usuarioService;
		this.jwtUtil = jwtUtil;
		this.tokenBlackListService = tokenBlackListService;
	}
	
	@PostMapping("/registrar")
	public ResponseEntity<?> registrar(@RequestBody Usuario usuario){
		try {
			Usuario usuarioNuevo = usuarioService.guardar(usuario);
			return ResponseEntity.status(HttpStatus.CREATED).body(usuarioNuevo);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al registrar usuario" + e.getMessage());
		}
	}
	
	@PostMapping("/login")
	
	public ResponseEntity<?> login(@RequestBody Usuario usuario){
		try {
			boolean autenticado = usuarioService.autenticar(usuario.getUsername(), usuario.getPassword());
			
			if(!autenticado) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o Contraseña incorrectos");
			}
			String token  = jwtUtil.generarToken(usuario.getUsername());
			return ResponseEntity.ok(Map.of("mensaje","Inicio de sesion existoso","token",token));
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al iniciar sesion"+e.getMessage());
		}
	}
	
	@PostMapping("/logout")
	
	public ResponseEntity<?> logout(@RequestBody Map<String,String>request){
		
		try {
			String token = request.get("token");
			if(token == null || token.isBlank()) {
				return ResponseEntity.badRequest().body("Debe propocionar un token");
			}
			
			if(!jwtUtil.validarToken(token)) {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido o expirado");
				
			}
			
			tokenBlackListService.invalidarToken(token);
			
			return ResponseEntity.ok(Map.of("mensaje","Sesion cerrada correctamente"));
		
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al cerrar sesion" + e.getMessage());
		}
	}
	
	@GetMapping("/perfil")
	public ResponseEntity<?> perfil() {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	String username = auth.getName();
	return ResponseEntity.ok(Map.of(
	        "mensaje", "Bienvenido al sistema protegido",
	        "usuario", username
	));
	}

}
