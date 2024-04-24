package com.example.projet_devmobile.layouts_utilitaires;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

@SuppressLint("ViewConstructor")
public class CandidatureLayout extends LinearLayout {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CandidatureLayout(Context context, Candidature candidature) {
        super(context);
        
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

        TextView candidat = new TextView(CandidatureLayout.this.getContext());
        candidat.setBackground(gradientDrawable);
        candidat.setLayoutParams(layoutParam);
        candidat.setText("Nom : "+candidature.getNom()
                +" Prénom : "+candidature.getNom()
                +"\nAge : "+ candidature.getAge()
                +"\nVille : "+candidature.getVille());
        candidat.setId(View.generateViewId());

        this.addView(metier);this.addView(candidat);
    }

    private void initLayout(Candidature candidature){
        db.collection("offres").document(candidature.getIdoffre())
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
