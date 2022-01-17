package com.example.ushare;

import android.widget.ImageView;

public class Usuarios_DDBB {

    private String id,usuario, contraseña, confirma_contraseña, fecha, email, estado, hora, municipio, apellido, activitats, cobles, texto, coche,edat;
    private int solicitudes,nuevoMensaje;

    private String uri;



    public Usuarios_DDBB() {
    }

    public Usuarios_DDBB(String id, String usuario, String contraseña, String confirma_contraseña, String fecha, String email, String estado, String hora, int solicitudes, int nuevoMensaje,
                          String municipio, String edat, String apellido, String activitats, String cobles, String texto, String coche,String uri) {
        this.id = id;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.confirma_contraseña = confirma_contraseña;
        this.fecha = fecha;
        this.email = email;
        this.estado = estado;
        this.hora = hora;
        this.solicitudes = solicitudes;
        this.nuevoMensaje = nuevoMensaje;
        //this.foto = foto;
        this.municipio = municipio;
        this.edat = edat;
        this.apellido = apellido;
        this.activitats = activitats;
        this.cobles = cobles;
        this.texto = texto;
        this.coche = coche;
        this.uri = uri;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(int solicitudes) {
        this.solicitudes = solicitudes;
    }

    public int getNuevoMensaje() {
        return nuevoMensaje;
    }

    public void setNuevoMensaje(int nuevoMensaje) {
        this.nuevoMensaje = nuevoMensaje;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getConfirma_contraseña() {
        return confirma_contraseña;
    }

    public void setConfirma_contraseña(String confirma_contraseña) {
        this.confirma_contraseña = confirma_contraseña;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /*public ImageView getFoto() {
        return foto;
    }

    public void setFoto(ImageView foto) {
        this.foto = foto;
    }*/

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getEdat() {
        return edat;
    }

    public void setEdat(String edat) {
        this.edat = edat;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getActivitats() {
        return activitats;
    }

    public void setActivitats(String activitats) {
        this.activitats = activitats;
    }

    public String getCobles() {
        return cobles;
    }

    public void setCobles(String cobles) {
        this.cobles = cobles;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getCoche() {
        return coche;
    }

    public void setCoche(String coche) {
        this.coche = coche;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String foto) {
        this.uri = uri;
    }
}
