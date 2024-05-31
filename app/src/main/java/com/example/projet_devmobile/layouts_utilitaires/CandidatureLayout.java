package com.example.projet_devmobile.layouts_utilitaires;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("ViewConstructor")
public class CandidatureLayout extends LinearLayout {

    public static final int CONTACTER = 1;
    public static final int NO_OPTION = 0;
    public static final int VALIDER = 2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final int options;
    private final String idCandidature;

    public CandidatureLayout(Context context, Candidature candidature, String idCandidature, int options) {
        super(context);
        this.options = options;
        this.idCandidature = idCandidature;
        
        GradientDrawable mainLayoutgradientDrawable = new GradientDrawable();
        mainLayoutgradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mainLayoutgradientDrawable.setCornerRadius(30);
        mainLayoutgradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20,20,20,20);

        this.setBackground(mainLayoutgradientDrawable);
        this.setPadding(30,30,30,30);
        this.setOrientation(VERTICAL);
        this.setLayoutParams(layoutParam);
        this.setId(View.generateViewId());

        initLayout(candidature);
    }

    @SuppressLint("SetTextI18n")
    private void setField(Candidature candidature, String job) {

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(Color.WHITE);

        TextView metier = new TextView(CandidatureLayout.this.getContext());
        metier.setLayoutParams(layoutParam);
        metier.setText("Offre : "+job);
        metier.setId(View.generateViewId());

        String candidatText = "Profil du candidat "+
                "\nNom : "   +candidature.getNom()+" "+candidature.getPrenom()
                +"\nAge : "  + candidature.getAge()
                +"\nVille : "+candidature.getVille()
                +"\nEtat : " +candidature.getEtat();

        TextView candidate = new TextView(CandidatureLayout.this.getContext());
        candidate.setBackground(gradientDrawable);
        candidate.setLayoutParams(layoutParam);
        candidate.setPadding(10,10,10,10);
        candidate.setId(View.generateViewId());

        this.addView(metier);this.addView(candidate);

        switch (this.options){
            case NO_OPTION:
                if (!candidature.getStatus().equals("")){
                    candidatText+="\nStatus : "+candidature.getStatus();
                }
                candidate.setText(candidatText);
                break;
            case CONTACTER:
                candidate.setText(candidatText + ("\nStatus : " + candidature.getStatus()));
                if (!candidature.getStatus().equals(Candidature.ANNULE))
                    addContactSection(String.valueOf(candidature.getNumero()),candidature.getEmail());
                break;
            case VALIDER:
                candidate.setText(candidatText);
                addConfirmSection();
                break;
        }
    }

    private void addContactSection(String phoneNumber,String email){
        ButtonSetLayout bs = new ButtonSetLayout(this.getContext(),"#EFE7E7");
        ButtonSetLayout.ButtonParam number = new ButtonSetLayout.ButtonParam("Appeler","#9D8F8F", v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + "0"+phoneNumber));
            try {
                this.getContext().startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), "Erreur : permission refusée", Toast.LENGTH_SHORT).show();
            }
        });
        ButtonSetLayout.ButtonParam mail = new ButtonSetLayout.ButtonParam("Envoyer un email","#9D8F8F", v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            if (intent.resolveActivity(this.getContext().getPackageManager()) != null) {
                this.getContext().startActivity(intent);
            } else {
                Toast.makeText(this.getContext(), "Aucune application d'envoi d'email n'est disponible", Toast.LENGTH_SHORT).show();
            }
        });

        bs.addButtonsSection(new ButtonSetLayout.ButtonParam[]{number,mail});
        this.addView(bs);
    }
    private void addConfirmSection(){
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(Color.WHITE);

        TextView resultValidationText = new TextView(CandidatureLayout.this.getContext());
        resultValidationText.setBackground(gradientDrawable);
        resultValidationText.setLayoutParams(layoutParam);
        resultValidationText.setPadding(10,10,10,10);
        resultValidationText.setId(View.generateViewId());

        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");

        ButtonSetLayout.ButtonParam  confirmer = new ButtonSetLayout.ButtonParam("Confirmer","#5CE98C", v -> {
            modifyStateCandidature(Candidature.VALIDE);
            CandidatureLayout.this.removeView(buttonSetLayout);
            resultValidationText.setText("Bienvenu camarade !");
            CandidatureLayout.this.addView(resultValidationText);
        });

        ButtonSetLayout.ButtonParam  annuler = new ButtonSetLayout.ButtonParam("Annuler","#FF0000", v -> {
            modifyStateCandidature(Candidature.ANNULE);
            CandidatureLayout.this.removeView(buttonSetLayout);
            resultValidationText.setText("Annulation Confirmé !");
            CandidatureLayout.this.addView(resultValidationText);

        });

        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{confirmer,annuler});
        this.addView(buttonSetLayout);
    }

    private void initLayout(Candidature candidature){
        // must get the offer form fireStore to show the job name 
        candidature.getIdoffre()
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            setField(candidature, document.getString("metiercible"));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                });
    }

    private void modifyStateCandidature(String newState){
        db.collection("candidatures").document(idCandidature)
                .update("status",newState)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully updated!"))
                .addOnFailureListener(e -> Log.w(TAG, "Error updating document", e));
    }
}
