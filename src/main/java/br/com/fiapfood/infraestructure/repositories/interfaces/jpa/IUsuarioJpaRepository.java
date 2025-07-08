package br.com.fiapfood.infraestructure.repositories.interfaces.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiapfood.infraestructure.entities.UsuarioEntity;

public interface IUsuarioJpaRepository extends JpaRepository<UsuarioEntity, UUID> {
	Optional<UsuarioEntity> findByIdAndIsAtivoTrue(UUID id);

	Optional<UsuarioEntity> findByIdAndIsAtivoFalse(UUID id);

	@Query("SELECT u FROM UsuarioEntity u INNER JOIN u.dadosLogin dl WHERE dl.id = :id")
	Optional<UsuarioEntity> findByIdLogin(@Param("id") UUID loginId);

	boolean existsByEmail(String email);
}