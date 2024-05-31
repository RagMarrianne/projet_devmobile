package com.example.projet_devmobile.classesUtilitaires;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;

public class Offre {
    public static final String IDEMPLOYEUR = "idemployeur";
    public static final String METIERCIBLE = "metiercible";
    public static final String DESCRIPTION = "description";
    public static final String REMUNERATION = "remuneration";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String DATE_DEBUT = "datedebut";
    public static final String DATE_FIN = "datefin";
    private DocumentReference idemployeur;
    private String metiercible;
    private String description;
    private int remuneration;
    private double latitude;
    private double longitude;
    private Date datedebut;
    private Date datefin;

    public Offre() {
    }

    public Offre(DocumentReference pseudoemployeur, String metiercible, String description, int remuneration, double latitude, double longitude, Date datedebut, Date datefin) {
        this.idemployeur = pseudoemployeur;
        this.metiercible = metiercible;
        this.description = description;
        this.remuneration = remuneration;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datedebut = datedebut;
        this.datefin = datefin;
    }

    public DocumentReference getIdemployeur() {
        return idemployeur;
    }

    public String getMetiercible() {
        return metiercible;
    }

    public String getDescription() {
        return description;
    }

    public int getRemuneration() {
        return remuneration;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public Date getDatefin() {
        return datefin;
    }
}
