package br.com.fiapfood.core.entities.dto.atendimento;

import java.time.LocalTime;
import java.util.UUID;

public record AtendimentoCoreDto(UUID id, String dia, LocalTime inicioAtendimento, LocalTime terminoAtendimento) {

}
