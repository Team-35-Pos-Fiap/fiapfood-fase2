package br.com.fiapfood.infrastructure.utils;

import br.com.fiapfood.infraestructure.utils.MensagensUtil;
import org.springframework.context.MessageSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Locale;

public class MensagensUtilTest {

    @Mock
    private MessageSource messageSource;

    private static final Locale LOCALE_PT_BR = Locale.of("pt", "BR");

    AutoCloseable mock;

    @BeforeEach
    void setUp() throws Exception {
        mock = MockitoAnnotations.openMocks(this);
        setStaticMessageSource(messageSource);
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
        setStaticMessageSource(null);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroInternalServerError() {
        // Arrange
        String mensagemEsperada = "Ocorreu um erro inesperado. Favor tentar realizar a operação novamente.";
        when(messageSource.getMessage(MensagensUtil.ERRO_INTERNAL_SERVER_ERROR, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_INTERNAL_SERVER_ERROR);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_INTERNAL_SERVER_ERROR, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroUsuarioNaoEncontrado() {
        // Arrange
        String mensagemEsperada = "Usuário não encontrado na base de dados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_USUARIO_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_USUARIO_NAO_ENCONTRADO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_USUARIO_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroUsuariosNaoEncontrados() {
        // Arrange
        String mensagemEsperada = "Não foram encontrados usuários na base de dados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_USUARIOS_NAO_ENCONTRADOS, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_USUARIOS_NAO_ENCONTRADOS);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_USUARIOS_NAO_ENCONTRADOS, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroPerfilNaoEncontrado() {
        // Arrange
        String mensagemEsperada = "Perfil não encontrado na base de dados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_PERFIL_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PERFIL_NAO_ENCONTRADO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_PERFIL_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroLoginNaoEncontrado() {
        // Arrange
        String mensagemEsperada = "Não foi encontrado um usuário com a matrícula e senha informados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_LOGIN_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_LOGIN_NAO_ENCONTRADO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_LOGIN_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroLoginSemPermissao() {
        // Arrange
        String mensagemEsperada = "O usuário não possui permissão de acesso.";
        when(messageSource.getMessage(MensagensUtil.ERRO_LOGIN_SEM_PERMISSAO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_LOGIN_SEM_PERMISSAO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_LOGIN_SEM_PERMISSAO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroParametroInvalido() {
        // Arrange
        String mensagemEsperada = "Parâmetro ''{0}'' inválido. Deve ser do tipo {1}.";
        when(messageSource.getMessage(MensagensUtil.ERRO_PARAMETRO_INVALIDO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PARAMETRO_INVALIDO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_PARAMETRO_INVALIDO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroPaginaInvalida() {
        // Arrange
        String mensagemEsperada = "O número da página deve ser maior ou igual a 1.";
        when(messageSource.getMessage(MensagensUtil.ERRO_PAGINA_INVALIDA, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_PAGINA_INVALIDA);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_PAGINA_INVALIDA, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroEmailDuplicado() {
        // Arrange
        String mensagemEsperada = "Já existe um usuário com o email informado.";
        when(messageSource.getMessage(MensagensUtil.ERRO_EMAIL_DUPLICADO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_EMAIL_DUPLICADO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_EMAIL_DUPLICADO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroCardapioNaoEncontrado() {
        // Arrange
        String mensagemEsperada = "Não foram encontrados cardápios na base de dados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_CARDAPIO_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_CARDAPIO_NAO_ENCONTRADO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_CARDAPIO_NAO_ENCONTRADO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaErroRestaurantesNaoEncontrados() {
        // Arrange
        String mensagemEsperada = "Não foram encontrados restaurantes na base de dados.";
        when(messageSource.getMessage(MensagensUtil.ERRO_RESTAURANTES_NAO_ENCONTRADOS, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.ERRO_RESTAURANTES_NAO_ENCONTRADOS);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.ERRO_RESTAURANTES_NAO_ENCONTRADOS, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoInativacaoUsuario() {
        // Arrange
        String mensagemEsperada = "Usuário inativado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_INATIVACAO_USUARIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_INATIVACAO_USUARIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_INATIVACAO_USUARIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoReativacaoUsuario() {
        // Arrange
        String mensagemEsperada = "Usuário reativado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_REATIVACAO_USUARIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_REATIVACAO_USUARIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_REATIVACAO_USUARIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoTrocaSenhaUsuario() {
        // Arrange
        String mensagemEsperada = "Senha alterada com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_TROCA_SENHA_USUARIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoRecuperacaoSenhaUsuario() {
        // Arrange
        String mensagemEsperada = "Senha recuperada com sucesso";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_RECUPERACAO_SENHA_USUARIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_RECUPERACAO_SENHA_USUARIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_RECUPERACAO_SENHA_USUARIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoTrocaMatriculaUsuario() {
        // Arrange
        String mensagemEsperada = "Matricula alterada com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_TROCA_MATRICULA_USUARIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_TROCA_MATRICULA_USUARIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_TROCA_MATRICULA_USUARIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoInativacaoRestaurante() {
        // Arrange
        String mensagemEsperada = "Restaurante inativado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_INATIVACAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_INATIVACAO_RESTAURANTE);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_INATIVACAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoReativacaoRestaurante() {
        // Arrange
        String mensagemEsperada = "Restaurante reativado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_REATIVACAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_REATIVACAO_RESTAURANTE);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_REATIVACAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoDelecaoRestaurante() {
        // Arrange
        String mensagemEsperada = "Restaurante deletado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_DELECAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_DELECAO_RESTAURANTE);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_DELECAO_RESTAURANTE, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoAtualizacaoCardapio() {
        // Arrange
        String mensagemEsperada = "Cardápio atualizado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_ATUALIZACAO_CARDAPIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_ATUALIZACAO_CARDAPIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_ATUALIZACAO_CARDAPIO, new Object[]{}, LOCALE_PT_BR);
    }

    @Test
    void deveRecuperarMensagemCorretaParaSucessoDelecaoCardapio() {
        // Arrange
        String mensagemEsperada = "Cardápio deletado com sucesso.";
        when(messageSource.getMessage(MensagensUtil.SUCESSO_DELECAO_CARDAPIO, new Object[]{}, LOCALE_PT_BR))
                .thenReturn(mensagemEsperada);

        // Act
        String mensagemRetornada = MensagensUtil.recuperarMensagem(MensagensUtil.SUCESSO_DELECAO_CARDAPIO);

        // Assert
        assertThat(mensagemRetornada).isEqualTo(mensagemEsperada);
        verify(messageSource).getMessage(MensagensUtil.SUCESSO_DELECAO_CARDAPIO, new Object[]{}, LOCALE_PT_BR);
    }

    private void setStaticMessageSource(MessageSource messageSource) throws Exception {
        Field messageSourceField = MensagensUtil.class.getDeclaredField("messageSource");
        messageSourceField.setAccessible(true);
        messageSourceField.set(null, messageSource);
    }
}
