package br.com.fiapfood.infraestructure.controllers.exceptions;

import br.com.fiapfood.core.exceptions.atendimento.AdicionarAtendimentoRestauranteNaoPermitidoException;
import br.com.fiapfood.core.exceptions.atendimento.AtendimentoRestauranteNaoEncontradoException;
import br.com.fiapfood.core.exceptions.atendimento.DiaAtendimentoRestauranteInvalidoException;
import br.com.fiapfood.core.exceptions.atendimento.ExclusaoAtendimentoRestauranteNaoPermitidoException;
import br.com.fiapfood.core.exceptions.item.*;
import br.com.fiapfood.core.exceptions.perfil.*;
import br.com.fiapfood.core.exceptions.restaurante.*;
import br.com.fiapfood.core.exceptions.tipo_culinaria.NomeTipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaInvalidoException;
import br.com.fiapfood.core.exceptions.tipo_culinaria.TipoCulinariaNaoEncontradoException;
import br.com.fiapfood.core.exceptions.usuario.*;
import br.com.fiapfood.infraestructure.controllers.response.ErroResponse;
import br.com.fiapfood.infraestructure.controllers.response.MensagemResponse;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<MensagemResponse> trataNullPointerException(NullPointerException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<MensagemResponse> trataValidacaoCamposException(ValidationException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(InternalServerError.class)
	public ResponseEntity<MensagemResponse> trataInternalErrorException(InternalServerError e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(UsuarioNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataUsuarioNaoEncontradoException(UsuarioNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<MensagemResponse> trataIllegalArgumentException(IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<MensagemResponse> trataHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
	}

	@ExceptionHandler(NoSuchMessageException.class)
	public ResponseEntity<MensagemResponse> trataNoSuchMessageException(NoSuchMessageException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
	}

	@ExceptionHandler(EmailDuplicadoException.class)
	public ResponseEntity<MensagemResponse> trataEmailDuplicadoException(EmailDuplicadoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(AtualizacaoStatusUsuarioNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoStatusUsuarioNaoPermitidaException(AtualizacaoStatusUsuarioNaoPermitidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(EmailUsuarioInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataEmailUsuarioInvalidoException(EmailUsuarioInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(MatriculaDuplicadaException.class)
	public ResponseEntity<MensagemResponse> trataMatriculaDuplicadaException(MatriculaDuplicadaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(MatriculaInvalidaException.class)
	public ResponseEntity<MensagemResponse> trataMatriculaInvalidaException(MatriculaInvalidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(NomePerfilInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataNomePerfilInvalidoException(NomePerfilInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(NomeUsuarioInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataNomeUsuarioInvalidoException(NomeUsuarioInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(PerfilInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataPerfilInvalidoException(PerfilInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(PerfilNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataPerfilNaoEncontradoException(PerfilNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(SenhaUsuarioInvalidaException.class)
	public ResponseEntity<MensagemResponse> trataSenhaInvalidaException(SenhaUsuarioInvalidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(UsuarioInativoException.class)
	public ResponseEntity<MensagemResponse> trataUsuarioInativoException(UsuarioInativoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(UsuarioSemAcessoException.class)
	public ResponseEntity<MensagemResponse> trataUsuarioSemAcessoException(UsuarioSemAcessoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(RestauranteNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataRestauranteNaoEncontradoException(RestauranteNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(NomePerfilDuplicadoException.class)
	public ResponseEntity<MensagemResponse> trataNomePerfilDuplicadoException(NomePerfilDuplicadoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(TipoCulinariaNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataTipoCulinariaNaoEncontradoException(TipoCulinariaNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(TipoCulinariaInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataTipoCulinariaInvalidoException(TipoCulinariaInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ExclusaoPerfilNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataExclusaoPerfilNaoPermitidaException(ExclusaoPerfilNaoPermitidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtendimentoRestauranteNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataAtendimentoRestauranteNaoEncontradoException(AtendimentoRestauranteNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(DiaAtendimentoRestauranteInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataDiaAtendimentoRestauranteInvalidoException(DiaAtendimentoRestauranteInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(AtualizacaoNomeItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoNomeItemNaoPermitidaException(AtualizacaoNomeItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoPrecoItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoPrecoItemNaoPermitidaException(AtualizacaoPrecoItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ImagemItemInvalidaException.class)
	public ResponseEntity<MensagemResponse> trataImagemItemInvalidaException(ImagemItemInvalidaException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ItemNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataItemNaoEncontradoException(ItemNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(NomeImagemItemInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataNomeImagemItemInvalidoException(NomeImagemItemInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(TamanhoNomeImagemItemInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataTamanhoNomeImagemItemInvalidoException(TamanhoNomeImagemItemInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(TipoImagemItemInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataTipoImagemItemInvalidoException(TipoImagemItemInvalidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ValorItemInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataValorItemInvalidoException(ValorItemInvalidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(AtualizacaoDonoRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoDonoRestauranteNaoPermitidaException(AtualizacaoDonoRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoEnderecoRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoEnderecoRestauranteNaoPermitidaException(AtualizacaoEnderecoRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoNomeRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoNomeRestauranteNaoPermitidaException(AtualizacaoNomeRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoStatusRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoStatusRestauranteNaoPermitidaException(AtualizacaoStatusRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoTipoCulinariaRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoTipoCulinariaRestauranteNaoPermitidaException(AtualizacaoTipoCulinariaRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(CadastrarRestauranteNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataCadastrarRestauranteNaoPermitidoException(CadastrarRestauranteNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(DonoRestauranteInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataDonoRestauranteInvalidoException(DonoRestauranteInvalidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(NomeRestauranteInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataNomeRestauranteInvalidoException(NomeRestauranteInvalidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(NomeTipoCulinariaInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataNomeTipoCulinariaInvalidoException(NomeTipoCulinariaInvalidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoEmailUsuarioNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoEmailUsuarioNaoPermitidoException(AtualizacaoEmailUsuarioNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoNomeUsuarioNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoNomeUsuarioNaoPermitidoException(AtualizacaoNomeUsuarioNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ExclusaoAtendimentoRestauranteNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataExclusaoAtendimentoRestauranteNaoPermitidoException(ExclusaoAtendimentoRestauranteNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AdicionarAtendimentoRestauranteNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataAdicionarAtendimentoRestauranteNaoPermitidoException(AdicionarAtendimentoRestauranteNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoDescricaoItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoDescricaoItemNaoPermitidaException(AtualizacaoDescricaoItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoDisponibilidadeConsumoPresencialItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoDisponibilidadeConsumoPresencialItemNaoPermitidaException(AtualizacaoDisponibilidadeConsumoPresencialItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoDisponibilidadeItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoDisponibilidadeItemNaoPermitidaException(AtualizacaoDisponibilidadeItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(AtualizacaoImagemItemNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoImagemItemNaoPermitidaException(AtualizacaoImagemItemNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(CadastrarItemNaoPermitidoException.class)
	public ResponseEntity<MensagemResponse> trataCadastrarItemNaoPermitidoException(CadastrarItemNaoPermitidoException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	
	@ExceptionHandler(InativacaoRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataInativacaoRestauranteNaoPermitidaException(InativacaoRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(ReativacaoRestauranteNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataReativacaoRestauranteNaoPermitidaException(ReativacaoRestauranteNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(AtualizacaoPerfilNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoPerfilNaoPermitidaException(AtualizacaoPerfilNaoPermitidaException e) {
		log.error(e.getMessage(), e);	
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<MensagemResponse> trataMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.error(e.getMessage(), e);

		return getResponse(HttpStatus.BAD_REQUEST, MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PARAMETRO_INVALIDO, e.getName(), e.getRequiredType().getSimpleName()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return getResponse(HttpStatus.BAD_REQUEST, errors);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
		Map<String, String> errors = new HashMap<>();
		
		ex.getAllErrors().forEach(error -> {
			String field = error instanceof FieldError fe ? fe.getField() : "objeto";
			errors.put(field, error.getDefaultMessage());
		});
		
		return getResponse(HttpStatus.BAD_REQUEST, errors);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> handleAll(Exception ex) {
		Map<String, String> error = new HashMap<>();
		
		error.put("mensagem", ex.getMessage());
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, error);
	}
	
	protected ResponseEntity<MensagemResponse> getResponse(HttpStatus status, String mensagem) {
		MensagemResponse erroResponse = new ErroResponse(mensagem);
		
		return ResponseEntity.status(status).body(erroResponse);
	}
	
	protected ResponseEntity<Map<String, String>> getResponse(HttpStatus status, Map<String, String> errors) {
		return ResponseEntity.status(status).body(errors);
	}
}