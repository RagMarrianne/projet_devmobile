package com.example.projet_devmobile.classesUtilitaires;

import com.google.firebase.firestore.DocumentReference;

public class Candidature {

    public static String EN_COURS = "en cours";
    public static String TRAITEE = "traitee";
    public static String REFUS = "non acceptee";
    public static String ACCEPTE = "acceptee";
    public static String VALIDE = "valide";
    public static String ANNULE = "annule";
    private DocumentReference idcandidat;
    private DocumentReference idemployeur;
    private DocumentReference idoffre;
    private String nom;
    private String prenom;
    private int age;
    private String email;
    private String nationalite;
    private String ville;
    private String cv;
    private String lettredemotivation;
    private Integer numero;
    private String status;
    private String etat;

    public Candidature(){}

    public Candidature(DocumentReference idcandidat, DocumentReference idemployeur, DocumentReference idoffre, String nom, String prenom, int age, String email, String nationalite, String ville, String cv, String lettredemotivation, Integer numero, String status, String etat) {
        this.idcandidat = idcandidat;
        this.idemployeur = idemployeur;
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
        this.status = status;
        this.etat = etat;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public DocumentReference getIdcandidat() {
        return idcandidat;
    }

    public DocumentReference getIdemployeur() {
        return idemployeur;
    }

    public DocumentReference getIdoffre() {
        return idoffre;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public int getAge() {
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

    public Integer getNumero() {
        return numero;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
