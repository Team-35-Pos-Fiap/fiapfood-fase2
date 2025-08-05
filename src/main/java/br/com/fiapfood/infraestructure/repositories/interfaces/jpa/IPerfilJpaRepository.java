package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiapfood.infraestructure.entities.PerfilEntity;

public interface IPerfilJpaRepository extends JpaRepository<PerfilEntity, Integer> {
	boolean existsByNomeIgnoreCase(String nome); 
}