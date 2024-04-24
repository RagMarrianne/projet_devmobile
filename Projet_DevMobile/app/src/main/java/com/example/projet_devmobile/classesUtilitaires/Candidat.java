package com.example.projet_devmobile.classesUtilitaires;

public class Candidat {
    private String nom;
    private String prenom;
    private String email;
    private String motdepasse;
    private double age;
    private String ville;
    private String nationalite;
    private String cv; // le chemin local vers le cv ou l'url de telechargement du cv

    public Candidat(){};
    public Candidat(String nom, String prenom, String email, String motdepasse, double age, String ville, String nationalite, String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.age = age;
        this.ville = ville;
        this.nationalite = nationalite;
        this.cv = cv;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public double getAge() {
        return age;
    }

    public String getVille() {
        return ville;
    }

    public String getNationalite() {
        return nationalite;
    }

    public String getCv() {
        return cv;
    }
}
