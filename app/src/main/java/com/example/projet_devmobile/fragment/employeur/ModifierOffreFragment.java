package com.example.projet_devmobile.fragment.employeur;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ModifierOffreFragment extends Fragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String ID_OFFRE = "idOffre";
    private static final String PSEUDO_EMPLOYEUR = "pseudo_employeur";
    private String idOffer;
    private String idEmployer;

    public static ModifierOffreFragment newInstance(String pseudo_employeur, String idOffre) {
        ModifierOffreFragment fragment = new ModifierOffreFragment();
        Bundle args = new Bundle();
        args.putString(ID_OFFRE, idOffre);
        args.putString(PSEUDO_EMPLOYEUR, pseudo_employeur);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assert getArguments() != null;
        idOffer = getArguments().getString(ID_OFFRE);
        idEmployer = getArguments().getString(PSEUDO_EMPLOYEUR);

        // Get the offer from our database and when the document is received, init the form
        db.collection("offres").document(idOffer)
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_affichage_entitie, container, false);
    }

    private void initLayout(Offre offre){
        LinearLayout infoOffre = requireView().findViewById(R.id.entitieInfo);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Init the form
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

        // Pre-complete the form with the old values of the offer
        offreForm.putValuesToEditAreas(new HashMap<String,Object>(){{
            put("metiercible", offre.getMetiercible());
            put("datedebut", offre.getDatedebut());
            put("datefin", offre.getDatefin());
            put("remuneration", offre.getRemuneration());
            put("longitude", offre.getLongitude());
            put("latitude", offre.getLatitude());
            put("description", offre.getDescription());
        }});

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);

        ButtonSetLayout.ButtonParam  modifierOffreBouton = new ButtonSetLayout.ButtonParam("Modifier","#5CE98C", v -> {
            // Collect the data written on the form
            Map<String, Object> data = null;
            try {
                data = offreForm.getData();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            data.put("idemployeur",db.collection("employeurs").document(idEmployer));

            // Send it to the database
            db.collection("offres").document(idOffer)
                    .update(data)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully modify!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error modification document", e));
            requireActivity().getSupportFragmentManager().popBackStack();

        });

        ButtonSetLayout.ButtonParam  deleteOfferBouton = new ButtonSetLayout.ButtonParam("Supprimer","#FF0000", v -> {
            // Delete the offer
            db.collection("offres").document(idOffer).delete()
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "DocumentSnapshot successfully deleted!"))
                    .addOnFailureListener(e -> Log.w(TAG, "Error document not deleted", e));
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
        buttonSetLayout.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,deleteOfferBouton,modifierOffreBouton});

        infoOffre.addView(offreForm);
        infoOffre.addView(buttonSetLayout);

    }

}