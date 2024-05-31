package com.example.projet_devmobile.fragment.commun;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activity.CandidateActivity;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


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
        ButtonSetLayout.ButtonParam  connexion = new ButtonSetLayout.ButtonParam("Connexion","#5CE98C", v -> {
            String identifiant = Objects.requireNonNull(logInForm.getEditsViews().get("Identifiant")).getText().toString();
            String motdepasse = Objects.requireNonNull(logInForm.getEditsViews().get("Mot de passe")).getText().toString();

            connexion(identifiant,motdepasse, v);

        });

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v ->
                requireActivity().getSupportFragmentManager().beginTransaction().remove(LogInFragment.this).commit());

        logInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,connexion});

        LinearLayout fragmentLayout = view.findViewById(R.id.logInMainLayout);
        fragmentLayout.addView(logInForm);
        fragmentLayout.addView(logInButtons);

    }

    private void connexion(String identifiant, String motdepasse, View v){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);

        Map<String,Integer> typeLogIn = new HashMap<String,Integer>(){{
            put("candidats",CANDIDAT);
            put("employeurs",EMPLOYEUR);
        }};

        if (!identifiant.equals("")){

            AtomicInteger logFail = new AtomicInteger(0);

            for (Map.Entry<String, Integer> entry : typeLogIn.entrySet()){

                logFail.set(logFail.get()+1);

                DocumentReference docRef = db.collection(entry.getKey()).document(identifiant);
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d(TAG, "Identifiant OK");
                            String password = (String) document.get("motdepasse");
                            assert password != null;
                            if (password.equals(motdepasse)){
                                Log.d(TAG, "mot de passe OK");

                                gradientDrawable.setColor(Color.GREEN);
                                v.setBackground(gradientDrawable);
                                ((Button) v).setText("CONNEXION en cours ...");

                                lancerActivity(identifiant,entry.getValue());
                            } else if (logFail.get()==2) {
                                gradientDrawable.setColor(Color.RED);
                                v.setBackground(gradientDrawable);
                                ((Button) v).setText("ERREUR id ou mdp");
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                        Toast.makeText(LogInFragment.this.getContext(),"Erreur de connexion à firestore",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void lancerActivity(String identifiant, int typeUser){
        Intent intent = null;
        requireActivity().finish();
        switch (typeUser){
            case CANDIDAT:
                intent = new Intent(getActivity(), CandidateActivity.class);
                break;
            case EMPLOYEUR:
                intent = new Intent(getActivity(), EmployeurActivity.class);
                break;
        }
        intent.putExtra("identifiant", identifiant);
        startActivity(intent);
    }
}