package com.example.projet_devmobile.fragment.employeur;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.EntitieLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class AffichageCandidatureFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String CANDIDATURE = "candidatures";
    private String idCandidature;

    public static AffichageCandidatureFragment newInstance(String idCandidature) {
        AffichageCandidatureFragment fragment = new AffichageCandidatureFragment();
        Bundle args = new Bundle();
        args.putString(CANDIDATURE,idCandidature);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            idCandidature = getArguments().getString(CANDIDATURE);
        }
        db.collection(CANDIDATURE).document(idCandidature)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Candidature candidature = document.toObject(Candidature.class);
                            initLayout(candidature);
                        } else {
                            Log.d(TAG,"OFFRE non éxistante");
                        }
                    } else {
                        Log.d(TAG,"IMPOSSIBLE DE RÉCUPÉRER L'OFFRE");
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_affichage_entitie, container, false);
    }

    private void initLayout(Candidature candidature){
        LinearLayout infoOffre = requireView().findViewById(R.id.entitieInfo);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EntitieLayout candidatureLayout = new EntitieLayout(this.getContext());
        candidatureLayout.setLayoutParams(layoutParams);
        candidatureLayout.addTextSection(new LinkedHashMap<String,Object>(){{
            put("Nom",candidature.getNom());
            put("Prénom",candidature.getPrenom());
            put("Âge",candidature.getAge());
            put("Nationalité",candidature.getNationalite());
            put("Ville",candidature.getVille());
            put("Email",candidature.getEmail());
        }
        });
        candidatureLayout.addPDFSection("CV",candidature.getCv(),R.id.mainMenu);
        candidatureLayout.addPDFSection("Lettre de motivation",candidature.getLettredemotivation(),R.id.mainMenu);

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);

        ButtonSetLayout.ButtonParam  accepterCandidatureBouton = new ButtonSetLayout.ButtonParam("Accepter","#5CE98C", v -> {

            db.collection("candidatures").document(this.idCandidature)
                    .update("status",Candidature.ACCEPTE)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            requireActivity().getSupportFragmentManager().popBackStack();

        });

        ButtonSetLayout.ButtonParam  refuserCandidatureBouton = new ButtonSetLayout.ButtonParam("Refuser","#FF0000", v -> {
            db.collection("candidatures").document(this.idCandidature)
                    .update("status",Candidature.REFUS)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully written!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            requireActivity().getSupportFragmentManager().popBackStack();
        });
        ButtonSetLayout.ButtonParam appelerCandidatBouton = new ButtonSetLayout.ButtonParam("Appeler le candidat","#9D8F8F", v -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + String.valueOf(candidature.getNumero())));
            try {
                this.getContext().startActivity(callIntent);
            } catch (SecurityException e) {
                e.printStackTrace();
                Toast.makeText(this.getContext(), "Erreur : permission refusée", Toast.LENGTH_SHORT).show();
            }
        });

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,refuserCandidatureBouton,accepterCandidatureBouton});
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{appelerCandidatBouton});

        infoOffre.addView(candidatureLayout);
        infoOffre.addView(buttonSetLayout);
    }
}