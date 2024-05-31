package com.example.projet_devmobile.fragment.candidat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.example.projet_devmobile.classesUtilitaires.ScreenUtils;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.CandidatDataViewModel;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.CandidatureLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ListeCandidatureCFragment extends Fragment {
    private static final String FILTRE = "filtre";
    private static final String ETAT = "etat";
    private Map<String, Object> criteres;
    private String etat;

    private CandidatDataViewModel candidatDataViewModel;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private GradientDrawable gradientDrawable = new GradientDrawable();
    private LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams offrelayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    private LinearLayout listeCandidaturesMenuContent;

    public static ListeCandidatureCFragment newInstance(String status) {
        Bundle args = new Bundle();
        ListeCandidatureCFragment fragment = new ListeCandidatureCFragment();
        args.putString(ETAT, status);
        fragment.setArguments(args);
        return fragment;
    }
    public static ListeCandidatureCFragment newInstance(String status, Map<String, Object> criteres) {
        Bundle args = new Bundle();
        ListeCandidatureCFragment fragment = new ListeCandidatureCFragment();
        args.putString(ETAT, status);
        args.putSerializable(FILTRE, (Serializable) criteres);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            criteres = (Map<String, Object>) getArguments().getSerializable(FILTRE);
            etat = getArguments().getString(ETAT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_liste_entities, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        candidatDataViewModel = new ViewModelProvider(requireActivity()).get(CandidatDataViewModel.class);
        listeCandidaturesMenuContent = view.findViewById(R.id.listeEntitiesMenuContent);

        initFilterButton();
        initMenuContent();

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200);
        ButtonSetLayout buttonSetLayout = new ButtonSetLayout(this.getContext(),"#FFFFFF");
        buttonSetLayout.setLayoutParams(layoutParams2);

        LinearLayout bs = view.findViewById(R.id.listeEntitiesBS);
        bs.addView(buttonSetLayout);

    }

    private void initFilterButton(){
        int dimension = ScreenUtils.getScreenWidth(this.requireContext());

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);

        ImageButton filterButton = new ImageButton(this.getContext());
        filterButton.setLayoutParams(new ConstraintLayout.LayoutParams((int) (dimension * 0.2), (int) (dimension * 0.2)));
        filterButton.setBackground(shape);
        filterButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        filterButton.setId(View.generateViewId());
        filterButton.setImageResource(R.drawable.filter);

        filterButton.setOnClickListener(v -> requireActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.menuContent, FilterFragment.newInstance(FilterFragment.FILTER,getArguments().getString(ETAT)))
                .commit());

        listeCandidaturesMenuContent.addView(filterButton);
    }
    private void addCandidatureView(String idCandidature, Candidature candidature){
        LinearLayout candidatureLayout = null;
        if (candidature.getStatus().equals(Candidature.ACCEPTE)){
            candidatureLayout = new CandidatureLayout(this.getContext(), candidature,idCandidature, CandidatureLayout.VALIDER);
        } else {
            candidatureLayout = new CandidatureLayout(this.getContext(), candidature,idCandidature, CandidatureLayout.NO_OPTION);
        }
        listeCandidaturesMenuContent.addView(candidatureLayout);
    }

    private void initMenuContent(){
        layoutParam.setMargins(0,20,0,20);
        offrelayoutParam.setMargins(20,20,20,20);

        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        DocumentReference idCandidate = db.collection("candidats").document(candidatDataViewModel.getUserId().getValue());

        db.collection("candidatures")
                .whereEqualTo("idcandidat",idCandidate)
                .whereEqualTo("etat",etat)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        Map<QueryDocumentSnapshot,Integer> listeCandidatures = new HashMap<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            listeCandidatures.put(document,0);
                            Log.d(TAG, "Un document récupéré "+ document.getId());
                        }
                        sortCandidatures(listeCandidatures);

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
    }

    private void sortCandidatures(Map<QueryDocumentSnapshot,Integer> candidatures){
        criteres = new HashMap<>();

        AtomicInteger nbr_candidatures_traitee = new AtomicInteger();

        if (criteres != null){
            for (Map.Entry<QueryDocumentSnapshot,Integer> entry: candidatures.entrySet()) {

                Candidature candidature = entry.getKey().toObject(Candidature.class);

                candidature.getIdoffre().get().addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        Map<String,Object> offre = document.getData();

                        for (Map.Entry<String, Object> critere: this.criteres.entrySet()){

                            if (critere.getValue() != null){
                                if (compare(critere.getValue(), offre.get(critere.getKey()))){
                                    candidatures.put(entry.getKey(), candidatures.get(candidature)+1);
                                }
                            }

                        }
                        nbr_candidatures_traitee.addAndGet(1);

                        if (nbr_candidatures_traitee.get() == candidatures.size()){
                            List<Map.Entry<QueryDocumentSnapshot,Integer>> list = new ArrayList<>(candidatures.entrySet());
                            list.sort(Comparator.comparingInt(Map.Entry<QueryDocumentSnapshot, Integer>::getValue).reversed());

                            for (Map.Entry<QueryDocumentSnapshot, Integer> entry2 : list) {
                                String idC = entry2.getKey().getId();
                                Candidature c = entry2.getKey().toObject(Candidature.class);
                                addCandidatureView(idC, c);
                            }
                        }
                    }
                });
            }

        }
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
