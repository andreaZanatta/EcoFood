package com.ecolution.ecofood.model;

import java.util.Date;

public class ItemModel {
    String nome;
    String categoria;
    double prezzo;
    String descrizione;
    String image;
    Date scadenza;
    SellerModel venditore;

    public ItemModel() { }

    public ItemModel(String nome, String categoria, double prezzo, String descrizione, String url, Date scadenza, SellerModel seller){
        this.nome = nome;
        this.categoria = categoria;
        this.prezzo = prezzo;
        this.descrizione = descrizione;
        this.image = url;
        this.scadenza = scadenza;
        this.venditore = seller;
    }

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

    public SellerModel getVenditore() { return venditore; }
}
