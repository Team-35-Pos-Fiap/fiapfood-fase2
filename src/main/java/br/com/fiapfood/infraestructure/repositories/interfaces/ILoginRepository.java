package br.com.fiapfood.infraestructure.repositories.interfaces;

import java.util.UUID;

import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.infraestructure.entities.LoginEntity;

public interface ILoginRepository {
    void salvar(LoginEntity login);
    LoginDto buscarPorMatricula(String matricula);
    LoginDto buscarPorMatriculaSenha(String matricula, String senha);
	LoginDto buscarPorId(UUID id);
	boolean matriculaJaCadastrada(String matricula);
}