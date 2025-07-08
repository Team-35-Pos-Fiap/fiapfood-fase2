package br.com.fiapfood.infraestructure.controllers.response;

public abstract class MensagemResponse {

    private String mensagem;

    public MensagemResponse(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMensagem() {
        return mensagem;
    }
}
