package com.krakedev.proyectos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.krakedev.proyectos.entidades.Proyecto;
import com.krakedev.proyectos.repositories.ProyectoRepository;

@Service
public class ProyectoService {
	
	private final ProyectoRepository repository;
	
	public ProyectoService(ProyectoRepository repository) {
		this.repository = repository;
	}
	
	public Proyecto guardar(Proyecto proyecto) {
		return repository.save(proyecto);
	}
	
	public List<Proyecto> listar(){
		return repository.findAll();
	}
	
	public Proyecto listarPorId(int id) {
		Optional<Proyecto> resultado =  repository.findById(id);
		return resultado.orElse(null);
	}
	
	public Proyecto actualizar(int id, Proyecto dato) {
		Proyecto proyecto = listarPorId(id);
		
		if(proyecto == null) {
			return null;
		}
		
		proyecto.setDescripcion(dato.getDescripcion());
		proyecto.setFechaInicio(dato.getFechaInicio());
		proyecto.setId(dato.getId());
		proyecto.setNombre(dato.getNombre());
		proyecto.setTareas(dato.getTareas());
		
		return repository.save(proyecto);
	}
	
	public boolean eliminar(int id) {
		Proyecto proyecto = listarPorId(id);
		
		if(proyecto == null) {
			return false;
		}
		repository.deleteById(id);
		return true;
	}

}
