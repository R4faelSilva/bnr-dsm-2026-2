package br.edu.fatecfranca.api.models;

import jakarta.validation.constraints.NotBlank;

public class Modulo {

    @NotBlank(message = "O titulo é obrigatório.")
    private String titulo;

    @NotBlank(message = "O conteudo é obrigatório.")
    private String conteudo;

    @NotBlank(message = "A carga horária é obrigatório.")
    private String cargaHoraria;

    public Modulo() {
    };

    public Modulo(
            String titulo,
            String conteudo,
            String cargaHoraria) {
        this.titulo = titulo;
        this.conteudo = conteudo;
        this.cargaHoraria = cargaHoraria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(String cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    

}