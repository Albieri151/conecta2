package com.red.conecta2.Models;

import java.time.LocalDate;
import java.time.Period;

public class Usuario {

    private Long id;
    private String nombre;
    private String apellido;
    private String usuario;
    private String genero;
    private String email;
    private String password;
    private String preguntaDeSeguridad;
    private String respuestaDeSeguridad;
    private String phone;
    private String fechaDeNacimiento;

    public Usuario(){};

    public Usuario(Long id, String nombre, String apellido, String usuario, String genero, String email, String password,
                   String preguntaDeSeguridad, String respuestaDeSeguridad, String phone, String fechaDeNacimiento) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.usuario = usuario;
        this.genero = genero;
        this.email = email;
        this.password = password;
        this.preguntaDeSeguridad = preguntaDeSeguridad;
        this.respuestaDeSeguridad = respuestaDeSeguridad;
        this.phone = phone;
        this.fechaDeNacimiento = fechaDeNacimiento;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreguntaDeSeguridad() {
        return preguntaDeSeguridad;
    }

    public void setPreguntaDeSeguridad(String preguntaDeSeguridad) {
        this.preguntaDeSeguridad = preguntaDeSeguridad;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getRespuestaDeSeguridad() {
        return respuestaDeSeguridad;
    }

    public void setRespuestaDeSeguridad(String respuestaDeSeguridad) {
        this.respuestaDeSeguridad = respuestaDeSeguridad;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer calcularEdad(LocalDate fechaDeNacimiento){
        LocalDate fechaActual = LocalDate.now();
        Period periodoEdad = Period.between(fechaDeNacimiento,fechaActual);
        return periodoEdad.getYears();
    };
}
