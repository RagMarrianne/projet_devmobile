package com.example.projet_devmobile.fragment.candidat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.EntityLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.LinkedHashMap;


public class AfficherOffreFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String OFFRE = "offres";

    private String idOffre;

    public AfficherOffreFragment() {
    }

    public static AfficherOffreFragment newInstance(String idOffre) {
        AfficherOffreFragment fragment = new AfficherOffreFragment();
        Bundle args = new Bundle();
        args.putString(OFFRE, idOffre);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_affichage_entitie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            idOffre = getArguments().getString(OFFRE);
        }
        db.collection(OFFRE).document(idOffre)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Offre offre = document.toObject(Offre.class);
                            initLayout(offre);
                        } else {
                            Log.d(TAG,"OFFRE non éxistante");
                        }
                    } else {
                        Log.d(TAG,"IMPOSSIBLE DE RÉCUPÉRER L'OFFRE");
                    }
                });
    }

    private void initLayout(Offre offre){
        LinearLayout infoOffre = requireView().findViewById(R.id.entitieInfo);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        EntityLayout offreLayout = new EntityLayout(this.getContext());
        offreLayout.setLayoutParams(layoutParams);
        offreLayout.addTextSection(new LinkedHashMap<String,Object>(){{
            put("Métier cible",offre.getMetiercible());
            put("Description",offre.getDescription());
            put("Rémunération",offre.getRemuneration());
            put("Début du contrat",offre.getDatedebut());
            put("Fin du contrat",offre.getDatefin());
        }
        });

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);

        ButtonSetLayout.ButtonParam  candidaterOffreBouton = new ButtonSetLayout.ButtonParam("Candidater","#5CE98C", v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.menuContent, CandidaterFragment.newInstance(idOffre,offre.getIdemployeur().getId()))
                    .commit();
        });

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v -> requireActivity().getSupportFragmentManager().popBackStack());


        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,candidaterOffreBouton});

        infoOffre.addView(offreLayout);
        infoOffre.addView(buttonSetLayout);
    }
}