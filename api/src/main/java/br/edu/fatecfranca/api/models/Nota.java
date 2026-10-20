package br.edu.fatecfranca.api.models;

public class Nota {
    
    private String avaliacao;
    private String valor;

    public Nota() {
    }
    
    public Nota(
            String avaliacao, 
            String valor) {
        this.avaliacao = avaliacao;
        this.valor = valor;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    
}
