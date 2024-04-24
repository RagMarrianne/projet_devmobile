package com.example.projet_devmobile.classesUtilitaires;

public class Candidature {
    private String idcandidat;
    private String idoffre;
    private String nom;
    private String prenom;
    private double age;
    private String email;
    private String nationalite;
    private String ville;
    private String cv;
    private String lettredemotivation;
    private double numero;

    public Candidature(String idcandidat) {
        this.idcandidat = idcandidat;
    }

    public Candidature(String idcandidat, String idoffre, String nom, String prenom, double age, String email, String nationalite, String ville, String cv, String lettredemotivation, double numero) {
        this.idcandidat = idcandidat;
        this.idoffre = idoffre;
        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.email = email;
        this.nationalite = nationalite;
        this.ville = ville;
        this.cv = cv;
        this.lettredemotivation = lettredemotivation;
        this.numero = numero;
    }

    public String getIdcandidat() {
        return idcandidat;
    }

    public String getIdoffre() {
        return idoffre;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public double getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getVille() {
        return ville;
    }

    public String getCv() {
        return cv;
    }

    public String getLettredemotivation() {
        return lettredemotivation;
    }

    public double getNumero() {
        return numero;
    }
}
