package com.example.projet_devmobile.fragment.employeur;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class NouvelleOffreFragment extends Fragment {
    private static final String PSEUDO = "pseudo";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static NouvelleOffreFragment newInstance(String pseudo) {
        NouvelleOffreFragment fragment = new NouvelleOffreFragment();
        Bundle args = new Bundle();
        args.putString(PSEUDO, pseudo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_affichage_entitie, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String employeurPseudo = requireArguments().getString(PSEUDO);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        FormLayout offreForm = new FormLayout(this.requireContext());
        offreForm.setLayoutParams(layoutParams);
        offreForm.addTextSection(new LinkedHashMap<String, Class<?>>() {{
            put("metiercible", String.class);
            put("datedebut", Date.class);
            put("datefin", Date.class);
            put("remuneration", Integer.class);
            put("longitude", Double.class);
            put("latitude", Double.class);
            put("description", String.class);
        }});
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);

        ButtonSetLayout.ButtonParam  deposerOffreBouton = new ButtonSetLayout.ButtonParam("Déposer","#5CE98C", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = null;
                try {
                    data = offreForm.getData();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                data.put("idemployeur",db.collection("employeurs").document(employeurPseudo));

                db.collection("offres").document()
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,deposerOffreBouton});

        LinearLayout offreInfo = view.findViewById(R.id.entitieInfo);
        offreInfo.addView(offreForm);
        offreInfo.addView(buttonSetLayout);

    }
}