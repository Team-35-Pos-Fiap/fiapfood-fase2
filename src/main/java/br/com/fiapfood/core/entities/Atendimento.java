package br.com.fiapfood.core.entities;

import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Atendimento {
	private UUID id;
	private String dia;
	private LocalTime inicioAtendimento;
	private LocalTime terminoAtendimento;
	
	public Atendimento(UUID id, String dia, LocalTime inicioAtendimento, LocalTime terminoAtendimento) {
		this.id = id;
		this.dia = dia;
		this.inicioAtendimento = inicioAtendimento;
		this.terminoAtendimento = terminoAtendimento;
	}
	
	public static Atendimento criar(UUID id, String dia, LocalTime inicioAtendimento, LocalTime terminoAtendimento) {
		return new Atendimento(id, dia, inicioAtendimento, terminoAtendimento);
	}
	
	public void atualizarDados(String dia, LocalTime inicioAtendimento, LocalTime terminoAtendimento) {
		this.dia = dia;
		this.inicioAtendimento = inicioAtendimento;
		this.terminoAtendimento = terminoAtendimento;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		
		if (obj == null)
			return false;
		
		if (getClass() != obj.getClass())
			return false;
		
		Atendimento other = (Atendimento) obj;
		
		return Objects.equals(id, other.id);
	}
}