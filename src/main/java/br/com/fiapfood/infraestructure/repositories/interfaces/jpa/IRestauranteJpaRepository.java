package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiapfood.infraestructure.entities.RestauranteEntity;

@Repository
public interface IRestauranteJpaRepository extends JpaRepository<RestauranteEntity, UUID> {
}