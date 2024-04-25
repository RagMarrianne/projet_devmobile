package com.example.projet_devmobile.fragment.general;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.classesUtilitaires.Candidat;
import com.example.projet_devmobile.classesUtilitaires.Employeur;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;
import java.util.Objects;


public class LogInFragment extends Fragment {

    private static final int CANDIDAT = 1;
    private static final int EMPLOYEUR = 2;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100,20,100,20);

        FormLayout logInForm = new FormLayout(this.getContext());
        logInForm.setLayoutParams(layoutParams);
        logInForm.addTextSection(new LinkedHashMap<String, Class<?>>() {{
            put("Identifiant", String.class);
            put("Mot de passe", String.class);
        }});

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(100,20,100,20);

        ButtonSetLayout logInButtons = new ButtonSetLayout(LogInFragment.this.getContext(),"#FFFFFF");
        logInButtons.setLayoutParams(layoutParams2);
        ButtonSetLayout.ButtonParam  connexion = new ButtonSetLayout.ButtonParam("Connexion","#5CE98C", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String identifiant = Objects.requireNonNull(logInForm.getEditsViews().get("Identifiant")).getText().toString();
                String motdepasse = Objects.requireNonNull(logInForm.getEditsViews().get("Mot de passe")).getText().toString();
                connexion(identifiant,motdepasse);
            }
        });
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(LogInFragment.this).commit();
            }
        });

        logInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,connexion});

        LinearLayout fragmentLayout = view.findViewById(R.id.logInMainLayout);
        fragmentLayout.addView(logInForm);
        fragmentLayout.addView(logInButtons);

    }

    private void connexion(String identifiant, String motdepasse){
        Log.d(TAG, identifiant +" "+motdepasse);

        DocumentReference docRef = db.collection("candidats").document(identifiant);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Identifiant candidat OK");
                    Candidat candidat = document.toObject(Candidat.class);
                    assert candidat != null;
                    if (candidat.getMotdepasse().equals(motdepasse)){
                        Log.d(TAG, "mot de passe OK");
                        lancerActivity(identifiant,CANDIDAT);

                    } else Toast.makeText(LogInFragment.this.getContext(),"Erreur d'identifiant",Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "Identifiant ERREUR chez les candidats");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                Toast.makeText(LogInFragment.this.getContext(),"Erreur de connexion à firestore",Toast.LENGTH_SHORT).show();
            }
        });

        DocumentReference docRef2 = db.collection("employeurs").document(identifiant);
        docRef2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "Identifiant employeur OK ");
                    Employeur employeur = document.toObject(Employeur.class);
                    assert employeur != null;
                    if (employeur.getMotdepasse().equals(motdepasse)){
                        Log.d(TAG, "mot de passe OK");
                        lancerActivity(identifiant,EMPLOYEUR);
                    } else Toast.makeText(LogInFragment.this.getContext(),"Erreur d'identifiant",Toast.LENGTH_SHORT).show();

                } else {
                    Log.d(TAG, "Identifiant ERREUR chez les employeurs");
                    Toast.makeText(LogInFragment.this.getContext(),"Erreur d'identifiant",Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
                Toast.makeText(LogInFragment.this.getContext(),"Erreur de connexion à firestore",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void lancerActivity(String identifiant, int typeUser){
        Intent intent = null;
        requireActivity().finish();
        switch (typeUser){
            case CANDIDAT:
                //TODO
                break;
            case EMPLOYEUR:
                intent = new Intent(getActivity(), EmployeurActivity.class);
                break;
        }
        intent.putExtra("identifiant", identifiant);
        startActivity(intent);
    }

}