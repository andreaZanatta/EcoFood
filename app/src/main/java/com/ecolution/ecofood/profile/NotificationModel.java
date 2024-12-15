package com.ecolution.ecofood.profile;

import java.time.LocalDateTime;

public class NotificationModel {
    Integer idNotifica;
    String titolo, descrizione;
    LocalDateTime dataDiCreazione;

    public NotificationModel(Integer idNotifica, String mainText, String descriptionText, LocalDateTime dataDiCreazione) {
        this.idNotifica = idNotifica;
        this.titolo = mainText;
        this.descrizione = descriptionText;
        this.dataDiCreazione = dataDiCreazione;
    }

    public Integer getIdNotifica() { return idNotifica; }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getDataDiCreazione() {
        return dataDiCreazione;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setDataDiCreazione(LocalDateTime dataDiCreazione) {
        this.dataDiCreazione = dataDiCreazione;
    }
}