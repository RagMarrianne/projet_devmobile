package com.example.projet_devmobile.classesUtilitaires;

public class Lienpublique {
    private String idemployeur;
    private String lien;

    public Lienpublique() {
    }

    public Lienpublique(String idemployeur, String lien) {
        this.idemployeur = idemployeur;
        this.lien = lien;
    }

    public String getIdemployeur() {
        return idemployeur;
    }

    public String getLien() {
        return lien;
    }
}
