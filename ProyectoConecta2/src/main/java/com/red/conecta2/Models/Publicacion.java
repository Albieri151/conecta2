package com.red.conecta2.Models;

import java.util.List;

public class Publicacion {
    private String contenidoPublicacion;
    private String creadorPublicacion;
    private String fechaPublicacion;
    private Long id;
    private Long cantidadMeGusta;
    private Long cantidadComentarios;
    private List<Comentarios> comentarios;

    public List<Comentarios> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentarios> comentarios) {
        this.comentarios = comentarios;
    }

    public Long getCantidadMeGusta() {
        return cantidadMeGusta;
    }

    public void setCantidadMeGusta(Long cantidadMeGusta) {
        this.cantidadMeGusta = cantidadMeGusta;
    }

    public Long getCantidadComentarios() {
        return cantidadComentarios;
    }

    public void setCantidadComentarios(Long cantidadComentarios) {
        this.cantidadComentarios = cantidadComentarios;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenidoPublicacion() {
        return contenidoPublicacion;
    }

    public void setContenidoPublicacion(String contenidoPublicacion) {
        this.contenidoPublicacion = contenidoPublicacion;
    }

    public String getCreadorPublicacion() {
        return creadorPublicacion;
    }

    public void setCreadorPublicacion(String creadorPublicacion) {
        this.creadorPublicacion = creadorPublicacion;
    }

    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(String fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
}
