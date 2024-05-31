package com.example.projet_devmobile.fragment.employeur;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.CandidatureLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class ListeCandidatureEFragment extends Fragment {
    private static final String ETAT = "etat";
    private static final String IDENTIFIANT = "identifiant";
    private String idEmployer;
    private String etat;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams offrelayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private LinearLayout listeCandidaturesMenuContent;

    public static ListeCandidatureEFragment newInstance(String state, String idEmployer) {
        Bundle args = new Bundle();
        ListeCandidatureEFragment fragment = new ListeCandidatureEFragment();
        args.putString(ETAT, state);
        args.putString(IDENTIFIANT,idEmployer);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liste_entities, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        assert getArguments() != null;
        idEmployer = getArguments().getString(IDENTIFIANT);
        etat = getArguments().getString(ETAT);

        listeCandidaturesMenuContent = view.findViewById(R.id.listeEntitiesMenuContent);

        initMenuContent();

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back});

        LinearLayout bs = view.findViewById(R.id.listeEntitiesBS);
        bs.addView(buttonSetLayout);

    }

    private void addCandidatureView(String idCandidature, Candidature candidature){
        LinearLayout candidatureLayout = null;

        // If the candidature is already accepted, we add contacting options
        if (etat.equals(Candidature.TRAITEE)){
            candidatureLayout = new CandidatureLayout(this.getContext(), candidature, idCandidature, CandidatureLayout.CONTACTER);
        }
        // if not, it means that it still have to be validate. So on click, run a new fragment which will allow the user to do it
        else {
            candidatureLayout = new CandidatureLayout(this.getContext(), candidature, idCandidature, CandidatureLayout.NO_OPTION);
            candidatureLayout.setOnClickListener(onClickListener -> requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .addToBackStack(null)
                    .replace(R.id.menuContent, AffichageCandidatureFragment.newInstance(idCandidature))
                    .commit());
        }
        listeCandidaturesMenuContent.addView(candidatureLayout);
    }

    private void initMenuContent(){
        layoutParam.setMargins(0,20,0,20);
        offrelayoutParam.setMargins(20,20,20,20);

        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        DocumentReference idemployeur = db.collection("employeurs").document(idEmployer);

        // Collect all the candidature received according to the status searched (ongoing,accepted)
        db.collection("candidatures")
                .whereEqualTo("idemployeur",idemployeur)
                .whereEqualTo("etat",etat)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Candidature candidature = document.toObject(Candidature.class);
                            String idCandidature = document.getId();
                            addCandidatureView(idCandidature,candidature);
                            Log.d(TAG, "Un document récupéré "+idCandidature);
                        }
                        Log.d(TAG, "Documents récupérés");

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}