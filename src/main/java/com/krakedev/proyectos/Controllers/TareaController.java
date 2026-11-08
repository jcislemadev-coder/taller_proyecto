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

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.services.TareaService;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {
	
	private final TareaService service;
	
	public TareaController(TareaService service) {
		this.service = service;
	}
	
	@PostMapping
	public ResponseEntity<?> crear(@RequestBody Tarea tarea){
		Tarea tareaGuardada;
		try {
			tareaGuardada = service.crear(tarea);
			return ResponseEntity.status(HttpStatus.CREATED).body(tareaGuardada);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR AL GUARDAR: "+e.getMessage());
		}
		
	}
	
	@GetMapping
	public ResponseEntity<?> listar(){
		try {
			return ResponseEntity.ok(service.listar());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR AL LISTAR" + e.getMessage());
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?>  listarPorId(@PathVariable int id){
		Tarea tarea;
		try {
			tarea = service.listarPorId(id);
			if(tarea == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(tarea);
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ERROR AL BUSCAR POR ID: " + e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Tarea dato){
		Tarea tareaActualizada;
		try {
			tareaActualizada = service.actualizar(id, dato);
			if(tareaActualizada == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(tareaActualizada);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al Actualizar" + e.getMessage());
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id){
		boolean eliminado;
		try {
			eliminado = service.eliminar(id);
			if(eliminado) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.notFound().build();
		}catch(Exception e){
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar: "+e.getMessage());
		}
	}

}
