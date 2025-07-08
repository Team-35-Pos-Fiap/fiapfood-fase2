package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiapfood.infraestructure.entities.LoginEntity;

public interface ILoginJpaRepository extends JpaRepository<LoginEntity, UUID>{
	Optional<LoginEntity> findByMatriculaAndSenha(String matricula, String senha);
	Optional<LoginEntity> findByMatricula(String matricula);
	boolean existsByMatricula(String matricula);
}