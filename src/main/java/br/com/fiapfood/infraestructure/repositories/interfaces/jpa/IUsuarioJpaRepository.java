package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiapfood.infraestructure.entities.UsuarioEntity;

public interface IUsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {
	@Query("SELECT u FROM UsuarioEntity u INNER JOIN u.perfil p WHERE p.id = :id")
	List<UsuarioEntity> findAllByIdPerfil(@Param("id") Integer idPerfil);
	
	boolean existsByEmail(String email);

	@Query("SELECT u FROM UsuarioEntity u INNER JOIN u.dadosLogin dl WHERE dl.matricula = :matricula AND dl.senha = :senha")
	Optional<UsuarioEntity> findByMatriculaSenha(String matricula, String senha);

	@Query("SELECT u FROM UsuarioEntity u INNER JOIN u.dadosLogin dl WHERE dl.matricula = :matricula")
	Optional<UsuarioEntity> findByMatricula(String matricula);
}