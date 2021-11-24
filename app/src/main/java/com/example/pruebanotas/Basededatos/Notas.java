package com.example.pruebanotas.Basededatos;

import java.io.Serializable;

// Clase de la tabla Notas de la BD
public class Notas implements Serializable {
    private Integer id_notas;
    private String titulo;
    private String contenido;
    private Integer encode;
    // Aqui iria id_usuario gg

    // Constructor vacio
    public Notas(){}

    // Constructor
    public Notas(Integer id_notas, String titulo, String contenido, Integer encode) { // Usuario id_usuario
        this.id_notas = id_notas;
        this.titulo = titulo;
        this.contenido = contenido;
        this.encode = encode;
        // this.id_usuario = id_usuario;
    }

    // Propiedades
    public Integer getId_notas() {
        return id_notas;
    }

    public void setId_notas(Integer id_notas) {
        this.id_notas = id_notas;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Integer getEncode() {
        return encode;
    }

    public void setEncode(Integer encode) {
        this.encode = encode;
    }

    @Override
    public String toString(){
        return "Notas{" +
                "id_notas = " + id_notas +
                ", titulo = " + titulo + '\'' +
                ", contenido = " + contenido + '\'' +
                ", encode = " + encode +
                "}";
    }
}
