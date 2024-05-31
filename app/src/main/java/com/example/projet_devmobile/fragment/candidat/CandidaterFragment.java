package com.example.projet_devmobile.fragment.candidat;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidat;
import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.CandidatDataViewModel;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class CandidaterFragment extends Fragment {
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    ActivityResultLauncher<String> cvFilePicker;
    ActivityResultLauncher<String> ldmFilePicker;
    private CandidatDataViewModel candidatDataViewModel;
    private static final String OFFRE = "offres";
    private static final String EMPLOYEUR = "employeurs";
    private String idOffre;
    private String idEmployeur;
    private FormLayout candidatureInForm;

    public static CandidaterFragment newInstance(String idOffre, String idEmployeur) {
        Bundle args = new Bundle();
        CandidaterFragment fragment = new CandidaterFragment();
        args.putString(OFFRE,idOffre);
        args.putString(EMPLOYEUR,idEmployeur);
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
            idEmployeur = getArguments().getString(EMPLOYEUR);
        }
        candidatDataViewModel = new ViewModelProvider(requireActivity()).get(CandidatDataViewModel.class);

        candidatureInForm = new FormLayout(this.requireContext());
        cvFilePicker = initFilePicker("cv");
        ldmFilePicker = initFilePicker("lettredemotivation");

        ButtonSetLayout bs = new ButtonSetLayout(this.requireContext(), "#FFFFFF");

        db.collection("candidats").document(candidatDataViewModel.getUserId().getValue())
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Candidat candidat = document.toObject(Candidat.class);
                            initLayout(candidat, candidatureInForm, bs);
                        } else {
                            Log.d(TAG,"OFFRE non éxistante");
                        }
                    } else {
                        Log.d(TAG,"IMPOSSIBLE DE RÉCUPÉRER L'OFFRE");
                    }
                });
    }

    private void initLayout(Candidat candidat, FormLayout candidatureInForm, ButtonSetLayout bs) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(20, 20, 20, 20);

        candidatureInForm.setLayoutParams(layoutParams);
        candidatureInForm.addTextSection(new LinkedHashMap<String,Class<?>>() {{
            put("nom", String.class);
            put("prenom", String.class);
            put("age", Integer.class);
            put("ville", String.class);
            put("email", String.class);
            put("numero", Integer.class);
            put("nationalite", String.class);
        }});

        candidatureInForm.addImportSection("cv", cvFilePicker);
        candidatureInForm.addImportSection("lettredemotivation", ldmFilePicker);


        candidatureInForm.putValuesToEditAreas(new HashMap<String,Object>(){{
            put("nom", candidat.getNom());
            put("prenom", candidat.getPrenom());
            put("age", candidat.getAge());
            put("ville", candidat.getVille());
            put("email", candidat.getEmail());
            put("numero", "0"+candidat.getNumero().toString());
            put("nationalite", candidat.getNationalite());
        }});
        candidatureInForm.modifyFilePathView("cv",candidat.getCv());

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(100,20,100,20);

        bs.setLayoutParams(layoutParams2);
        ButtonSetLayout.ButtonParam  valider = new ButtonSetLayout.ButtonParam("Valider","#5CE98C", v -> {

            Map<String, Object> data;
            try {
                data = candidatureInForm.getData();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            String pseudo = candidatDataViewModel.getUserId().getValue();
            data.put("etat", Candidature.EN_COURS);
            data.put("status", "");
            data.put("idcandidat",db.collection("candidats").document(pseudo));
            data.put("idoffre",db.collection("offres").document(idOffre));
            data.put("idemployeur",db.collection("employeurs").document(idEmployeur));

            if ( !Objects.equals(data.get("cv"), candidat.getCv())){
                data.put("cv", importerFichier((String) data.get("cv"),pseudo,"cv"));
            }
            data.put("lettredemotivation", importerFichier((String) data.get("lettredemotivation"),pseudo,"lettredemotivation"));

            db.collection("candidatures").document()
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                    });

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .remove(this).commit();
            requireActivity().getSupportFragmentManager()
                    .popBackStack("main menu", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        });
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v -> requireActivity().getSupportFragmentManager().popBackStack());

        bs.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,valider});

        LinearLayout mainLayout = requireView().findViewById(R.id.entitieInfo);
        mainLayout.addView(candidatureInForm);
        mainLayout.addView(bs);
    }

    private String importerFichier(String fileUri, String idUser, String label){
        if (!fileUri.equals("")){
            String storePath = "Candidat/"+idUser+"/"+label+"/"+label+idOffre;

            StorageReference storageRef = storage.getReference().child(storePath);

            Uri uri = Uri.parse(fileUri);
            storageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                Log.d(TAG,"Fichier ajouter à la base de données");
            }).addOnFailureListener(exception -> {
                Log.d(TAG,"ERREUR importation de fichier vers la base de données");
            });
            return storePath;
        } else return "";
    }

    private ActivityResultLauncher<String> initFilePicker(String label){
        return registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Log.d(TAG, "URI sélectionnée : " + uri.toString());
                        candidatureInForm.modifyFilePathView(label,uri.toString());
                    } else {
                        Log.d(TAG,"Aucun fichier selectionné");
                    }
                });
    }

}

