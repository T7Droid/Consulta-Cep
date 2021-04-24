package com.t7droid.enderecosdobrasil.model;

public class Cidade {
    String codigo_ibge;
    String nome;
    String latitude;
    String longidude;
    String capital;
    String codigo_uf;

    public Cidade() {
    }

    public Cidade(String codigo_ibge, String nome, String latitude, String longidude, String capital, String codigo_uf) {
        this.codigo_ibge = codigo_ibge;
        this.nome = nome;
        this.latitude = latitude;
        this.longidude = longidude;
        this.capital = capital;
        this.codigo_uf = codigo_uf;
    }

    public String getCodigo_ibge() {
        return codigo_ibge;
    }

    public void setCodigo_ibge(String codigo_ibge) {
        this.codigo_ibge = codigo_ibge;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongidude() {
        return longidude;
    }

    public void setLongidude(String longidude) {
        this.longidude = longidude;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getCodigo_uf() {
        return codigo_uf;
    }

    public void setCodigo_uf(String codigo_uf) {
        this.codigo_uf = codigo_uf;
    }
}
