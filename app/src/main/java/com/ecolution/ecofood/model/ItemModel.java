package com.ecolution.ecofood.model;

import java.util.Date;

public class ItemModel {
    String id;
    String nome;
    String categoria;
    double prezzo;
    String descrizione;
    String image;
    Date scadenza;
    String venditoreId;

    public ItemModel() { }

    public ItemModel(String itId, String nome, String categoria, double prezzo, String descrizione, String url, Date scadenza, String id){
        this.id = itId;
        this.nome = nome;
        this.categoria = categoria;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.image = url;
        this.scadenza = scadenza;
        this.venditoreId = id;
    }
    public void setId(String id) { this.id = id; }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setImage(String url) { this.image = url; }

    public void setScadenza(Date scadenza) {
        this.scadenza = scadenza;
    }

    public String getId() { return id; }

    public String getNome() {
        return nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public double getPrezzo() { return prezzo; }

    public String getDescrizione() {
        return descrizione;
    }

    public String getImage() { return image; }

    public Date getScadenza() {
        return scadenza;
    }

    public String getVenditoreId() { return venditoreId; }
}
