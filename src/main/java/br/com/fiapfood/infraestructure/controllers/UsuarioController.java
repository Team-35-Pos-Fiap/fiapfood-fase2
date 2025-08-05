package br.com.fiapfood.infraestructure.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiapfood.core.controllers.interfaces.IUsuarioCoreController;
import br.com.fiapfood.infraestructure.controllers.request.endereco.DadosEnderecoDto;
import br.com.fiapfood.infraestructure.controllers.request.login.LoginDto;
import br.com.fiapfood.infraestructure.controllers.request.login.MatriculaDto;
import br.com.fiapfood.infraestructure.controllers.request.login.SenhaDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.CadastrarUsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosEmailDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosNomeDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.DadosPerfilDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioDto;
import br.com.fiapfood.infraestructure.controllers.request.usuario.UsuarioPaginacaoDto;
import br.com.fiapfood.infraestructure.controllers.response.MensagemResponse;
import br.com.fiapfood.infraestructure.controllers.response.SucessoResponse;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@Slf4j
public class UsuarioController {

	private final IUsuarioCoreController usuarioCoreController;
	
	public UsuarioController(IUsuarioCoreController usuarioCoreController) {
		this.usuarioCoreController = usuarioCoreController;
	}

	@PostMapping
	public ResponseEntity<Void> cadastrar(@Valid @RequestBody @NotNull final CadastrarUsuarioDto usuario) {
		log.info("cadastrar():dados do usuário {}", usuario);
		
		usuarioCoreController.cadastrar(usuario);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{id}/status/inativa")
	public ResponseEntity<MensagemResponse> inativar(@Valid @PathVariable @NotNull final UUID id) {
		log.info("inativar():id {}", id);

		usuarioCoreController.inativar(id);

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_INATIVACAO_USUARIO));
		
		return ResponseEntity.ok(sucessoResponse);
	}
	
	@PatchMapping("/{id}/status/reativa")
	public ResponseEntity<MensagemResponse> reativar(@Valid @PathVariable @NotNull final UUID id) {
		log.info("reativar():id {}", id);

		usuarioCoreController.reativar(id);
		
		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_REATIVACAO_USUARIO));
		
		return ResponseEntity.ok(sucessoResponse);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> buscarUsuarioPorId(@PathVariable @NotNull @Valid final UUID id) {
		log.info("buscarUsuarioPorId():id {}", id);
		
		return ResponseEntity.ok().body(usuarioCoreController.buscarUsuarioPorId(id)); 
	}
	
	@GetMapping
	public ResponseEntity<UsuarioPaginacaoDto> buscarUsuarios(@RequestParam(defaultValue = "1") @Valid @Positive(message = "O parâmetro página precisa ser maior do que 0.") final Integer pagina) {
		log.info("buscarUsuarios() - pagina {}", pagina);

		return ResponseEntity.ok().body(usuarioCoreController.buscarTodos(pagina));
	}

	@PatchMapping("/{id}/perfil")
	public ResponseEntity<Void> atualizarPerfil(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final DadosPerfilDto dadosPerfil) {
		usuarioCoreController.atualizarPerfil(id, dadosPerfil.idPerfil());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}

	@PatchMapping("/{id}/endereco")
	public ResponseEntity<Void> atualizarEndereco(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final DadosEnderecoDto dadosEndereco) {
		usuarioCoreController.atualizarDadosEndereco(id, dadosEndereco);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}

	@PatchMapping("/{id}/nome")
	public ResponseEntity<Void> atualizarNome(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final DadosNomeDto dados) {
		usuarioCoreController.atualizarNome(id, dados.nome());

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}

	@PatchMapping("/{id}/email")
	public ResponseEntity<Void> atualizarEmail(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final DadosEmailDto dados) {
		usuarioCoreController.atualizarEmail(id, dados.email());
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
	}

	@PatchMapping("/{id}/senha")
	public ResponseEntity<MensagemResponse> atualizarSenha(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final SenhaDto dados) {
		log.info("trocar senha():id {} - senha {}", id, dados.senha());
		
		usuarioCoreController.atualizarSenha(id, dados.senha());

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO));
	
		return ResponseEntity.ok(sucessoResponse);
	}

	@PatchMapping("/{id}/matricula")
	public ResponseEntity<MensagemResponse> atualizarMatricula(@Valid @PathVariable @NotNull final UUID id, @Valid @RequestBody @NotNull final MatriculaDto dados) {
		log.info("trocar senha():id {} - senha {}", id, dados.matricula());
		
		usuarioCoreController.atualizarMatricula(id, dados.matricula());

		MensagemResponse sucessoResponse = new SucessoResponse(MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_MATRICULA_USUARIO));
	
		return ResponseEntity.ok(sucessoResponse);
	}
	
	@PostMapping("/valida-acesso")
	public ResponseEntity<MensagemResponse> validarAcesso(@RequestBody @Valid @NotNull final LoginDto dados) {
		log.info("validar acesso():dados do login {}", dados);

		MensagemResponse sucessoResponse = new SucessoResponse(usuarioCoreController.validarAcesso(dados.matricula(), dados.senha()));

		return ResponseEntity.ok().body(sucessoResponse);
	}
}