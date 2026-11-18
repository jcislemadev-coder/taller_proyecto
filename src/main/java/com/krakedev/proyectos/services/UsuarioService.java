package com.krakedev.proyectos.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Usuario;
import com.krakedev.proyectos.repositories.UsuarioRepository;

@Service
public class UsuarioService {
	private final UsuarioRepository usuarioRepository;
	
	
	//Inyeccion de dependencias por constructor
	public UsuarioService(UsuarioRepository usuarioRepository) {
		super();
		this.usuarioRepository = usuarioRepository;
	}
	
	//Metodo para guadar usuario
	
	public Usuario guardar (Usuario usuario) {
		
		String passwordEncriptada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(10));
		
		usuario.setPassword(passwordEncriptada);
		
		return usuarioRepository.save(usuario);
	}
	
	//Metodo para autenticar un usuario
	
	public boolean autenticar(String username, String password) {
		Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
		
		if(usuarioOptional.isEmpty()) {
			return false;
		}
		
		Usuario usuario = usuarioOptional.get();
		return BCrypt.checkpw(password, usuario.getPassword());
	}
	
	
}
