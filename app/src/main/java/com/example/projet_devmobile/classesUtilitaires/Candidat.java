package com.example.projet_devmobile.classesUtilitaires;

public class Candidat {
    private String nom;
    private String prenom;
    private String email;
    private String motdepasse;
    private Integer age;
    private String ville;
    private String nationalite;
    private Integer numero;
    private String cv;

    public Candidat(){};
    public Candidat(String nom, String prenom, String email, Integer numero, String motdepasse, Integer age, String ville, String nationalite, String cv) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motdepasse = motdepasse;
        this.age = age;
        this.ville = ville;
        this.nationalite = nationalite;
        this.cv = cv;
        this.numero = numero;
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

    public Integer getAge() {
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

    public Integer getNumero() {
        return numero;
    }

}
