package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.EnderecoDto;
import br.com.fiapfood.infraestructure.entities.EnderecoEntity;

public interface IEnderecoRepository {
	EnderecoDto buscarPorId(UUID id);
    void salvar(EnderecoEntity endereco);
}
