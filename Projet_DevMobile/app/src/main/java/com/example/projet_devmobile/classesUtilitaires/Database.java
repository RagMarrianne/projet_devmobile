package com.example.projet_devmobile.classesUtilitaires;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Database {
    // nous permet d'avoir acces à des fichiers
    private static final FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");
    // nous donnera acces aux donnees
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void enregistrerDonneeInscription(String id, Object data){
        if (data instanceof Candidat){
            StorageReference storageRef = storage.getReference().child("Candidats/username/cv");
            Uri uri = Uri.parse(((Candidat) data).getCv());
            // Téléversez le fichier sur Firebase Storage
            UploadTask uploadTask = storageRef.putFile(uri);

            // Gérez le succès ou l'échec de l'opération de téléversement
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                // Le fichier a été téléversé avec succès
            }).addOnFailureListener(exception -> {
                // Le téléversement du fichier a échoué
            });
            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            firestore.collection("Candidats").document(id).set(data);
        } else if (data instanceof Employeur) {

        }
    }
}
