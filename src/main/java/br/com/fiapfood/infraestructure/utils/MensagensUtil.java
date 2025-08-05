package br.com.fiapfood.infraestructure.utils;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MensagensUtil {
	private static MessageSource messageSource;
	
	private static Locale locale = Locale.of("pt", "BR");
	
	public static final String ERRO_INTERNAL_SERVER_ERROR = "exception.internal_server_error";
	public static final String ERRO_USUARIO_NAO_ENCONTRADO = "exception.usuario_nao_encontrado";
	public static final String ERRO_USUARIOS_NAO_ENCONTRADOS = "exception.usuarios_nao_encontrados";
	public static final String ERRO_PERFIL_NAO_ENCONTRADO = "exception.perfil_nao_encontrado";
	public static final String ERRO_LOGIN_NAO_ENCONTRADO = "exception.login_nao_encontrado";
	public static final String ERRO_LOGIN_SEM_PERMISSAO = "exception.login_sem_permissao_acesso";
	public static final String ERRO_PARAMETRO_INVALIDO = "exception.erro_parametro_invalido";
	public static final String ERRO_PAGINA_INVALIDA = "exception.pagina_invalida";
	public static final String ERRO_EMAIL_DUPLICADO = "exception.email_duplicado";
	public static final String ERRO_CARDAPIO_NAO_ENCONTRADO = "exception.cardapios_nao_encontrados";
	public static final String ERRO_RESTAURANTES_NAO_ENCONTRADOS = "exception.restaurantes_nao_encontrados";

	public static final String SUCESSO_INATIVACAO_USUARIO = "sucesso.inativacao_usuario";
	public static final String SUCESSO_REATIVACAO_USUARIO = "sucesso.reativacao_usuario";
	public static final String SUCESSO_TROCA_SENHA_USUARIO = "sucesso.troca_senha_usuario";
	public static final String SUCESSO_TROCA_MATRICULA_USUARIO = "sucesso.troca_matricula_usuario";
	public static final String SUCESSO_RECUPERACAO_SENHA_USUARIO = "sucesso.recuperacao_senha_usuario";
	public static final String SUCESSO_INATIVACAO_RESTAURANTE = "sucesso.inativacao_restaurante";
	public static final String SUCESSO_REATIVACAO_RESTAURANTE = "sucesso.reativacao_restaurante";
	public static final String SUCESSO_DELECAO_RESTAURANTE = "sucesso.delecao_restaurante";
	public static final String SUCESSO_ATUALIZACAO_CARDAPIO = "sucesso.atualizacao_cardapio";
	public static final String SUCESSO_DELECAO_CARDAPIO = "sucesso.delecao_cardapio";


	@Autowired
	public void setMessageSource(MessageSource messageSource) {
		MensagensUtil.messageSource = messageSource;
	}
	
	public static String recuperarMensagem(String mensagem, Object ... parametros) {
		return messageSource.getMessage(mensagem, parametros, locale);
	}
}