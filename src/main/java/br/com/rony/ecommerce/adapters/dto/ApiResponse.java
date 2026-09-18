package br.com.rony.ecommerce.adapters.dto;

public class ApiResponse<T> {
    private boolean sucesso;
    private String mensagem;
    private T data;

    public ApiResponse() {}
    public ApiResponse(boolean sucesso, String mensagem, T data) {
        this.sucesso = sucesso;
        this.mensagem = mensagem;
        this.data = data;
    }

    public boolean isSucesso() { return sucesso; }
    public String getMensagem() { return mensagem; }
    public T getData() { return data; }
    public void setSucesso(boolean sucesso) { this.sucesso = sucesso; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public void setData(T data) { this.data = data; }
}