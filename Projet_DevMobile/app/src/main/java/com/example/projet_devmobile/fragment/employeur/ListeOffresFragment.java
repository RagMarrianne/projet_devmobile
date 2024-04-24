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
import android.widget.TextView;
import android.widget.Toast;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.OffreLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ListeOffresFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams offrelayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private LinearLayout listeOffresMenuContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liste_offres, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        listeOffresMenuContent = view.findViewById(R.id.listeOffreMenuContent);
        initMenuContent();

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v ->
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(ListeOffresFragment.this)
                        .replace(R.id.menuContent, new EmployeurMenuFragment())
                        .commit());
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back});

        LinearLayout bs = view.findViewById(R.id.listeOffreBS);
        bs.addView(buttonSetLayout);

    }

    private void addOffreView(String idOffre, Offre offre){
        LinearLayout offreLayout = new OffreLayout(this.getContext(), offre);
        offreLayout.setOnClickListener(onClickListener -> requireActivity().getSupportFragmentManager().beginTransaction()
                .remove(this)
                .replace(R.id.menuContent, ModifierOffreFragment.newInstance("crous",idOffre))
                .commit());

        listeOffresMenuContent.addView(offreLayout);
    }

    private void initMenuContent(){
        layoutParam.setMargins(0,20,0,20);
        offrelayoutParam.setMargins(20,20,20,20);

        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        DocumentReference idemployeur = db.collection("employeurs").document("crous");

        db.collection("offres").whereEqualTo("pseudoemployeur",idemployeur)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Offre offre = document.toObject(Offre.class);
                            String idOffre = document.getId();
                            addOffreView(idOffre,offre);
                            Log.d(TAG, "Un document récupéré");
                        }
                        Log.d(TAG, "Documents récupérés");

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }
}