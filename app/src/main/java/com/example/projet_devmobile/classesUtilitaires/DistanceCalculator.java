package com.example.projet_devmobile.classesUtilitaires;

import java.lang.Math;

public class DistanceCalculator {
    public static final double RADIUS_OF_EARTH_KM = 6371; // Rayon moyen de la Terre en kilomètres

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Conversion des latitudes et longitudes de degrés en radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Différences de latitudes et de longitudes
        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        // Formule de Haversine
        double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.pow(Math.sin(deltaLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance en kilomètres

        return RADIUS_OF_EARTH_KM * c;
    }
}

