package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiapfood.infraestructure.entities.TipoCulinariaEntity;

public interface ITipoCulinariaJpaRepository extends JpaRepository<TipoCulinariaEntity, Integer> {
	boolean existsByNomeIgnoreCase(String nome); 
}