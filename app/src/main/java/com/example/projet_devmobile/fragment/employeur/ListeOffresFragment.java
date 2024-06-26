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
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.OffreLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ListeOffresFragment extends Fragment {
    private static final String IDENTIFIANT = "identifiant";
    private String idEmployer;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams offrelayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout listeOffresMenuContent;

    public static ListeOffresFragment newInstance(String identifiant) {
        Bundle args = new Bundle();
        args.putString(IDENTIFIANT,identifiant);
        ListeOffresFragment fragment = new ListeOffresFragment();
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

        listeOffresMenuContent = view.findViewById(R.id.listeEntitiesMenuContent);

        initMenuContent();

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v ->
                requireActivity().getSupportFragmentManager()
                .popBackStack());

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back});

        LinearLayout bs = view.findViewById(R.id.listeEntitiesBS);
        bs.addView(buttonSetLayout);
    }

    private void addOfferView(String idOffer, Offre offre){
        LinearLayout offerLayout = new OffreLayout(this.getContext(), offre);

        // On click, run a new fragment which will allow the user the modify the offer
        offerLayout.setOnClickListener(onClickListener -> requireActivity().getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(R.id.menuContent, ModifierOffreFragment.newInstance(idEmployer,idOffer))
                .commit());

        listeOffresMenuContent.addView(offerLayout);
    }

    private void initMenuContent(){
        layoutParam.setMargins(0,20,0,20);
        offrelayoutParam.setMargins(20,20,20,20);

        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        DocumentReference docRefEmployer = db.collection("employeurs").document(idEmployer);

        // Collect all the offer which was submit by the employer
        db.collection("offres").whereEqualTo("idemployeur",docRefEmployer)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Offre offre = document.toObject(Offre.class);
                            String idOffre = document.getId();
                            addOfferView(idOffre,offre); // For each offer, add an OfferLayout
                            Log.d(TAG, "Un document récupéré");
                        }
                        Log.d(TAG, "Documents récupérés");

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}