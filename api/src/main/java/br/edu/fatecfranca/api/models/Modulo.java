package br.edu.fatecfranca.api.models;

public class Modulo {

    private String titulo;
    private String conteudo;
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