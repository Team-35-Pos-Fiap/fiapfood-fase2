package br.com.fiapfood.infraestructure.controllers.exceptions;

import java.util.HashMap;
import java.util.Map;

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

import br.com.fiapfood.core.exceptions.AtualizacaoPerfilUsuarioNaoPermitidaException;
import br.com.fiapfood.core.exceptions.AtualizacaoStatusUsuarioNaoPermitidaException;
import br.com.fiapfood.core.exceptions.EmailDuplicadoException;
import br.com.fiapfood.core.exceptions.EmailUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.EnderecoUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.LoginInvalidoException;
import br.com.fiapfood.core.exceptions.LoginNaoEncontradoException;
import br.com.fiapfood.core.exceptions.MatriculaDuplicadaException;
import br.com.fiapfood.core.exceptions.MatriculaInvalidaException;
import br.com.fiapfood.core.exceptions.NomePerfilInvalidoException;
import br.com.fiapfood.core.exceptions.NomeUsuarioInvalidoException;
import br.com.fiapfood.core.exceptions.PerfilInvalidoException;
import br.com.fiapfood.core.exceptions.PerfilNaoEncontradoException;
import br.com.fiapfood.core.exceptions.SenhaInvalidaException;
import br.com.fiapfood.core.exceptions.UsuarioInativoException;
import br.com.fiapfood.core.exceptions.UsuarioNaoEncontradoException;
import br.com.fiapfood.core.exceptions.UsuarioSemAcessoException;
import br.com.fiapfood.infraestructure.controllers.response.ErroResponse;
import br.com.fiapfood.infraestructure.controllers.response.MensagemResponse;
import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

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

	@ExceptionHandler(LoginNaoEncontradoException.class)
	public ResponseEntity<MensagemResponse> trataLoginNaoEncontradoException(LoginNaoEncontradoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.NOT_FOUND, e.getMessage());
	}
	
	@ExceptionHandler(EmailDuplicadoException.class)
	public ResponseEntity<MensagemResponse> trataEmailDuplicadoException(EmailDuplicadoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}

	@ExceptionHandler(AtualizacaoPerfilUsuarioNaoPermitidaException.class)
	public ResponseEntity<MensagemResponse> trataAtualizacaoPerfilUsuarioNaoPermitidaException(AtualizacaoPerfilUsuarioNaoPermitidaException e) {
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

	@ExceptionHandler(EnderecoUsuarioInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataEnderecoUsuarioInvalidoException(EnderecoUsuarioInvalidoException e) {
		log.error(e.getMessage(), e);
		
		return getResponse(HttpStatus.BAD_REQUEST, e.getMessage());
	}
	
	@ExceptionHandler(LoginInvalidoException.class)
	public ResponseEntity<MensagemResponse> trataLoginInvalidoException(LoginInvalidoException e) {
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
	
	@ExceptionHandler(SenhaInvalidaException.class)
	public ResponseEntity<MensagemResponse> trataSenhaInvalidaException(SenhaInvalidaException e) {
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

		return ResponseEntity.badRequest().body(errors);
	}

	@ExceptionHandler(HandlerMethodValidationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Map<String, String>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
		Map<String, String> errors = new HashMap<>();
		
		ex.getAllErrors().forEach(error -> {
			String field = error instanceof FieldError fe ? fe.getField() : "objeto";
			errors.put(field, error.getDefaultMessage());
		});
		
		return getResponse(HttpStatus.INTERNAL_SERVER_ERROR, errors);
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