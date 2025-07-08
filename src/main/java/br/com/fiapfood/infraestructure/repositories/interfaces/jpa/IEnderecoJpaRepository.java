package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiapfood.infraestructure.entities.EnderecoEntity;

public interface IEnderecoJpaRepository extends JpaRepository<EnderecoEntity, UUID> {

}