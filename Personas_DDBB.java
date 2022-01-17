package com.example.ushare;

import java.io.Serializable;

public class Personas_DDBB implements Serializable {

    public String uri,nombre, apellido, municipio, edat, id, actividad, cobles, texto, coche;


    public Personas_DDBB() {
    }

    public Personas_DDBB(String uri, String nombre, String apellido, String municipio, String edat, String id, String actividad, String cobles, String texto, String coche) {
        this.uri = uri;
        this.nombre = nombre;
        this.apellido = apellido;
        this.municipio = municipio;
        this.edat = edat;
        this.id = id;
        this.actividad = actividad;
        this.cobles = cobles;
        this.texto = texto;
        this.coche = coche;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
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
}
