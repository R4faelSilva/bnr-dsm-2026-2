package br.edu.fatecfranca.api.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class Nota {
    
    @NotBlank(message = "A identificação da avaliação é obrigatória.")
    private String avaliacao;

    @NotBlank(message = "O valor da nota é obrigatório.")
    @Positive(message = "A nota deve ser maior que zero.")
    private Double valor;

    public Nota() {
    }
    
    public Nota(
            String avaliacao, 
            Double valor) {
        this.avaliacao = avaliacao;
        this.valor = valor;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    
}
