package br.com.fiapfood.infraestructure.entities;

import java.time.LocalTime;
import java.util.UUID;

import br.com.fiapfood.infraestructure.enums.Dia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "atendimento")
public class AtendimentoEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(name = "id_atendimento")
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private Dia dia;
	
	@Column(name = "inicio_atendimento")
	private LocalTime inicioAtendimento;
	
	@Column(name = "termino_atendimento")
	private LocalTime terminoAtendimento;
}