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


import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("ViewConstructor")
public class CandidatureLayout extends LinearLayout {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean addContactOption;
    public CandidatureLayout(Context context, Candidature candidature, boolean addContactOption) {
        super(context);
        this.addContactOption=addContactOption;
        
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
    private void setField(Candidature candidature, String metiercible) {
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(Color.WHITE);

        TextView metier = new TextView(CandidatureLayout.this.getContext());
        metier.setLayoutParams(layoutParam);
        metier.setText("Offre : "+metiercible);
        metier.setId(View.generateViewId());

        String candidatText = "Profil du candidat "+
                "\nNom : "+candidature.getNom()
                +" "+candidature.getNom()
                +"\nAge : "+ candidature.getAge()
                +"\nVille : "+candidature.getVille();

        TextView candidate = new TextView(CandidatureLayout.this.getContext());
        candidate.setBackground(gradientDrawable);
        candidate.setLayoutParams(layoutParam);
        candidate.setPadding(10,10,10,10);
        candidate.setId(View.generateViewId());

        this.addView(metier);this.addView(candidate);

        if (this.addContactOption){
            candidate.setText(candidatText);
            addContactSection(String.valueOf(candidature.getNumero()),candidature.getEmail());
        }
        else {
            candidate.setText(candidatText+"\nStatus : "+candidature.getStatus());
        }
    }

    private void addContactSection(String phoneNumber,String email){
        ButtonSetLayout bs = new ButtonSetLayout(this.getContext(),"#EFE7E7");
        ButtonSetLayout.ButtonParam numero = new ButtonSetLayout.ButtonParam("Appeler","#9D8F8F", v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
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
                Toast.makeText(this.getContext(), "Aucune application de messagerie n'est disponible", Toast.LENGTH_SHORT).show();
            }
        });

        bs.addButtonsSection(new ButtonSetLayout.ButtonParam[]{numero,mail});
        this.addView(bs);
    }

    private void initLayout(Candidature candidature){
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
}
