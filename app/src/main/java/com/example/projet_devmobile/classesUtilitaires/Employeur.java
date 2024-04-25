package com.example.projet_devmobile.classesUtilitaires;

public class Employeur {
    private String nom;
    private String adresse;
    private String email;
    private String motdepasse;
    private double numero;

    public Employeur(){}
    public Employeur(String nom, String email, String adresse, String motdepasse, double numero) {
        this.nom = nom;
        this.adresse = adresse;
        this.motdepasse = motdepasse;
        this.numero = numero;
        this.email = email;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public double getNumero() {
        return numero;
    }
}
