package com.example.ushare;

import android.media.Image;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Model implements Serializable {
    Integer id;
    String tipus;
    String dia;
    String hores;
    String nomactivitat;
    String municipi;
    String anul;
    String mesdades;

    Double latitud;
    Double longitud;
    Double latitudaprox;
    Double longitudaprox;

    /*String linkproghtml;
    String linkprogpdf;
    String lloc;

    String poblacioampliada;
    String totselsinterprets;
    String imatge;

*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHores() {
        return hores;
    }

    public void setHores(String hores) {
        this.hores = hores;
    }

    public String getNomactivitat() {
        return nomactivitat;
    }

    public void setNomactivitat(String nomactivitat) {
        this.nomactivitat = nomactivitat;
    }

    public String getMunicipi() {
        return municipi;
    }

    public void setMunicipi(String municipi) {
        this.municipi = municipi;
    }

    public String getAnul() { return anul; }

    public void setAnul(String anul) { this.anul = anul; }

    public String getMesdades() { return mesdades; }

    public void setMesdades(String mesdades) { this.mesdades = mesdades; }

    public Double getLatitud() { return latitud; }

    public void setLatitud(Double latitud) { this.latitud = latitud; }

    public Double getLongitud() { return longitud; }

    public void setLongitud(Double longitud) { this.longitud = longitud; }

    public Double getLatitudaprox() { return latitudaprox; }

    public void setLatitudaprox(Double latitudaprox) { this.latitudaprox = latitudaprox; }

    public Double getLongitudaprox() { return longitudaprox; }

    public void setLongitudaprox(Double longitudaprox) { this.longitudaprox = longitudaprox; }

    /*public String getLinkproghtml() {
        return linkproghtml;
    }

    public void setLinkproghtml(String linkproghtml) {
        this.linkproghtml = linkproghtml;
    }

    public String getLinkprogpdf() {
        return linkprogpdf;
    }

    public void setLinkprogpdf(String linkprogpdf) {
        this.linkprogpdf = linkprogpdf;
    }

    public String getLloc() {
        return lloc;
    }

    public void setLloc(String lloc) {
        this.lloc = lloc;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitudaprox() {
        return latitudaprox;
    }

    public void setLatitudaprox(String latitudaprox) {
        this.latitudaprox = latitudaprox;
    }

    public String getLongitudaprox() {
        return longitudaprox;
    }

    public void setLongitudaprox(String longitudaprox) {
        this.longitudaprox = longitudaprox;
    }

    public String getPoblacioampliada() {
        return poblacioampliada;
    }

    public void setPoblacioampliada(String poblacioampliada) {
        this.poblacioampliada = poblacioampliada;
    }

    public String getTotselsinterprets() {
        return totselsinterprets;
    }

    public void setTotselsinterprets(String totselsinterprets) {
        this.totselsinterprets = totselsinterprets;
    }

    public String getImatge() {
        return imatge;
    }

    public void setImatge(String imatge) {
        this.imatge = imatge;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", tipus='" + tipus + '\'' +
                ", dia=" + dia +
                ", hores='" + hores + '\'' +
                ", nomactivitat='" + nomactivitat + '\'' +
                ", mesdades='" + mesdades + '\'' +
                ", linkproghtml='" + linkproghtml + '\'' +
                ", linkprogpdf='" + linkprogpdf + '\'' +
                ", lloc='" + lloc + '\'' +
                ", latitud='" + latitud + '\'' +
                ", longitud='" + longitud + '\'' +
                ", latitudaprox='" + latitudaprox + '\'' +
                ", longitudaprox='" + longitudaprox + '\'' +
                ", poblacioampliada='" + poblacioampliada + '\'' +
                ", totselsinterprets='" + totselsinterprets + '\'' +
                ", imatge='" + imatge + '\'' +
                ", municipi='" + municipi + '\'' +
                ", anul='" + anul + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", tipus='" + tipus + '\'' +
                ", dia='" + dia + '\'' +
                ", hores='" + hores + '\'' +
                ", nomactivitat='" + nomactivitat + '\'' +
                ", municipi='" + municipi + '\'' +
                ", anul='" + anul + '\'' +
                ", mesdades='" + mesdades + '\'' +
                ", latitud=" + latitud +
                ", longitud=" + longitud +
                ", latitudaprox=" + latitudaprox +
                ", longitudaprox=" + longitudaprox +
                '}';
    }
}
