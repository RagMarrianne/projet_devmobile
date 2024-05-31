package com.example.projet_devmobile.fragment.candidat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.DistanceCalculator;
import com.example.projet_devmobile.classesUtilitaires.Offre;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.CandidatDataViewModel;
import com.example.projet_devmobile.fragment.commun.SignInFragment;
import com.example.projet_devmobile.layouts_utilitaires.OffreLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidatAnonymeMenuFragment extends Fragment {
    private static final String FILTRE = "filtre";
    private Map<String, Object> criteres;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CandidatDataViewModel candidatDataViewModel;
    private LinearLayout listeOffresMenuContent;


    public CandidatAnonymeMenuFragment() {
        // Required empty public constructor
    }

    public static CandidatAnonymeMenuFragment newInstance(Map<String, Object> criteres) {
        Bundle args = new Bundle();
        args.putSerializable(FILTRE, (Serializable) criteres);
        CandidatAnonymeMenuFragment fragment = new CandidatAnonymeMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            criteres = (Map<String, Object>) getArguments().getSerializable(FILTRE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liste_entities, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        candidatDataViewModel = new ViewModelProvider(requireActivity()).get(CandidatDataViewModel.class);

        listeOffresMenuContent = view.findViewById(R.id.listeEntitiesMenuContent);

        if (criteres != null){
            initMenuWithFilter();
        } else initMenuWithoutFilter();

    }

    private void addOffreView(String idOffre, Offre offre){

        LinearLayout offreLayout = new OffreLayout(this.getContext(), offre);

        offreLayout.setOnClickListener(onClickListener -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack("main menu")
                .replace(R.id.menuContent, SignInFragment.newInstance(SignInFragment.CANDIDATE,true))
                .commit());

        listeOffresMenuContent.addView(offreLayout);
    }


    private void initMenuWithFilter(){

        db.collection("offres")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Map<QueryDocumentSnapshot,Integer> offres = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            offres.put(document,0);
                        }

                        for (Map.Entry<QueryDocumentSnapshot, Integer> entry : sortByCriteres(offres)) {
                            addOffreView(entry.getKey().getId(),entry.getKey().toObject(Offre.class));
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }


    private void initMenuWithoutFilter(){
        db.collection("offres")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Map<String,Offre> offres = new HashMap<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Offre offre = document.toObject(Offre.class);
                            String idOffre = document.getId();
                            offres.put(idOffre,offre);
                        }

                        List<Map.Entry<String,Offre>> list = new ArrayList<>(offres.entrySet());
                        list.sort((o1, o2) ->
                                Double.compare(getDist(o1.getValue()), getDist(o2.getValue())));

                        for (Map.Entry<String, Offre> entry : list) {
                            addOffreView(entry.getKey(),entry.getValue());
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private List<Map.Entry<QueryDocumentSnapshot,Integer>> sortByCriteres(Map<QueryDocumentSnapshot, Integer> offres){

        for(Map.Entry<QueryDocumentSnapshot,Integer> entry: offres.entrySet()){

            Map<String,Object> offre = entry.getKey().getData();

            for (Map.Entry<String, Object> critere: this.criteres.entrySet()){
                Log.d(TAG, "CRITERE :"+critere.getKey()+critere.getValue());
                Log.d(TAG, "OFFRE :"+ offre.get(critere.getKey()));

                if (compare(critere.getValue(), offre.get(critere.getKey()))){
                    offres.put(entry.getKey(), entry.getValue()+1);
                    Log.d(TAG, "OK");
                }

            }
        }
        List<Map.Entry<QueryDocumentSnapshot,Integer>> list = new ArrayList<>(offres.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry<QueryDocumentSnapshot, Integer>::getValue).reversed());

        return list;
    }
    private double getDist(Offre o){
        return DistanceCalculator.calculateDistance(o.getLatitude(),
                o.getLongitude(),
                candidatDataViewModel.getLatitude().getValue(),
                candidatDataViewModel.getLongitude().getValue());
    }

    public static boolean compare(Object critere, Object valeur) {
        if (critere instanceof Integer && valeur instanceof Number) {
            return ((Integer) critere <= (Long) valeur) ;
        } else if (critere instanceof String && valeur instanceof String) {
            return ((String) critere).equals((String) valeur);
        }else if (critere instanceof String && valeur instanceof DocumentReference) {
            return ((String) critere).equals(((DocumentReference) valeur).getId());
        }else if (critere instanceof Date && valeur instanceof Timestamp) {
            Timestamp timestamp = new Timestamp(((Date) critere));
            return timestamp.compareTo((Timestamp) valeur) <= 0;
        } else {
            return false;
        }
    }
}