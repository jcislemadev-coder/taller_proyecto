package com.krakedev.proyectos.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.services.ProyectoService;

@RestController
@RequestMapping("/api/proyectos")

public class ProyectoController {
	
	private final ProyectoService service;
	
	public ProyectoController(ProyectoService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<?> guardar(@RequestBody Proyecto proyecto){
	Proyecto proyectoGuardado;
	try {
		proyectoGuardado = service.guardar(proyecto);
		return ResponseEntity.status(HttpStatus.CREATED).body(proyectoGuardado);
				}catch(Exception e) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+e.getMessage());						
				}
	}
	
	@GetMapping
	public ResponseEntity<?> listar(){
		try {
			return ResponseEntity.ok(service.listar());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar: "+e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> listarPorId(@PathVariable int id){
		Proyecto proyecto;	
		try {
			proyecto = service.listarPorId(id);
			if(proyecto == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(proyecto);
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar por ID: "+e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Proyecto dato){
		Proyecto proyectoActualizado;
		try {
			proyectoActualizado = service.actualizar(id, dato);
			
			if(proyectoActualizado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			
			return ResponseEntity.ok(proyectoActualizado);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar por ID; " + e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar (@PathVariable int id){
		boolean eliminado;
		try {
			eliminado = service.eliminar(id);
			if(eliminado) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.notFound().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar "+e.getMessage());
		}
	}

}
