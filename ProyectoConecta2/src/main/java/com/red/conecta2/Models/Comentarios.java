package com.red.conecta2.Models;

public class Comentarios {
    private Long com_id;
    private String contenido;
    private Long pub_id;
    private Long user_id;
    private String fecha;
    private String nombreUser;

    public String getNombreUser() {
        return nombreUser;
    }

    public void setNombreUser(String nombreUser) {
        this.nombreUser = nombreUser;
    }

    public Long getCom_id() {
        return com_id;
    }

    public void setCom_id(Long com_id) {
        this.com_id = com_id;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Long getPub_id() {
        return pub_id;
    }

    public void setPub_id(Long pub_id) {
        this.pub_id = pub_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
