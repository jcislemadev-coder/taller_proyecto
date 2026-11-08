package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Tarea;
import com.krakedev.proyectos.repositories.TareaRepository;

@Service
public class TareaService {
	private TareaRepository repository;
	
	public TareaService(TareaRepository repository) {
		this.repository = repository;
	}
	
	public Tarea crear(Tarea tarea) {
		return repository.save(tarea);
	}
	
	public List<Tarea> listar(){
		return repository.findAll();
	}
	
	public Tarea listarPorId(int id) {
		Optional<Tarea> resultado = repository.findById(id);
		return resultado.orElse(null);
	}
	
	public Tarea actualizar(int id , Tarea dato) {
		Tarea tarea = listarPorId(id);
		
		if (tarea == null) {
			return null;
		}
		
		tarea.setDescripcion(dato.getDescripcion());
		tarea.setFechalimite(dato.getFechalimite());
		tarea.setCostoEstimado(dato.getCostoEstimado());
		tarea.setProyecto(dato.getProyecto());
		tarea.setEmpleados(dato.getEmpleados());
		return repository.save(tarea);
	}
	
	public boolean eliminar(int id) {
		Tarea tarea = listarPorId(id);
		
		if(tarea == null) {
			return false;
		}
		
		repository.deleteById(id);
		return true;
	}
	

}
