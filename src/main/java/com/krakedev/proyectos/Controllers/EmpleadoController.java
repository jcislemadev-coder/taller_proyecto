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

import com.krakedev.proyectos.entidades.Empleado;
import com.krakedev.proyectos.services.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

	private final EmpleadoService service;

	public EmpleadoController(EmpleadoService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> guardar(@RequestBody Empleado empleado) {
		Empleado empleadoGuardado;
		try {
			empleadoGuardado = service.guardar(empleado);
			return ResponseEntity.status(HttpStatus.CREATED).body(empleadoGuardado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
		}
	}

	@GetMapping
	public ResponseEntity<?> listar() {
		try {
	        return ResponseEntity.ok(service.listar());
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al listar: " + e.getMessage());
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> listaPorId(@PathVariable int id) {
		Empleado empleado;
		try {
			empleado = service.listarPorId(id);
			if (empleado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(empleado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al buscar por ID: " + e.getMessage());
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> actualizar(@PathVariable int id, @RequestBody Empleado dato) {
		Empleado empleadoActualizado;
		try {
			empleadoActualizado = service.actualizar(id, dato);
			
			if (empleadoActualizado == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
			return ResponseEntity.ok(empleadoActualizado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al actualizar por ID: " + e.getMessage());
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable int id) {
		boolean eliminado;
		try {
			eliminado = service.eliminar(id);
		if (eliminado) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al eliminar: "+e.getMessage());
		}
	}

}
