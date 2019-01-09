package com.jeanjnap.chat.Models;

public class Message {
    String mensagem;
    String tipo;

    public Message() {
    }

    public Message(String mensagem, String tipo) {
        this.mensagem = mensagem;
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
