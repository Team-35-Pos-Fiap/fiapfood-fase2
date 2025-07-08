package br.com.fiapfood.infraestructure.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiapfood.core.controllers.interfaces.ILoginCoreController;
import br.com.fiapfood.core.entities.dto.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.MatriculaDto;
import br.com.fiapfood.infraestructure.controllers.request.SenhaDto;
import br.com.fiapfood.infraestructure.controllers.response.MensagemResponse;
import br.com.fiapfood.infraestructure.controllers.response.SucessoResponse;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {

	private final ILoginCoreController loginCoreController;

	public LoginController(ILoginCoreController loginCoreController) {
		this.loginCoreController = loginCoreController;
	}

	@PostMapping
	public ResponseEntity<MensagemResponse> validar(@RequestBody @Valid @NotNull final  LoginDto dados) {
		log.info("realizaLogin():dados do login {}", dados);

		MensagemResponse sucessoResponse = new SucessoResponse(loginCoreController.validar(dados));

		return ResponseEntity.ok().body(sucessoResponse);
	}

	@PatchMapping("/{matricula}/senha")
	public ResponseEntity<MensagemResponse> atualizarSenha(@PathVariable @Valid @NotNull final  String matricula, @Valid @RequestBody @NotNull final SenhaDto dados) {
		log.info("trocar senha():id {} - senha {}", matricula, dados.senha());
		
		loginCoreController.atualizarSenha(matricula, dados.senha());

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO));
	
		return ResponseEntity.ok(sucessoResponse);
	}


	@PatchMapping("/{matricula}/matricula")
	public ResponseEntity<MensagemResponse> atualizarMatricula(@PathVariable @Valid @NotNull final  String matricula, @Valid @RequestBody @NotNull final MatriculaDto dados) {
		log.info("trocar senha():id {} - senha {}", matricula, dados.matricula());
		
		loginCoreController.atualizarMatricula(matricula, dados.matricula());

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO));
	
		return ResponseEntity.ok(sucessoResponse);
	}
}