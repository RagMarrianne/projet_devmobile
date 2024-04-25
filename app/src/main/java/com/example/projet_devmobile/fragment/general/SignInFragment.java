package com.example.projet_devmobile.fragment.general;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.annotation.NonNull;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activitesUtilitaires.AbstractFormFragment;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.activity.ReceptionActivity;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignInFragment extends AbstractFormFragment {
    private FormLayout signInForm;
    private ButtonSetLayout signInButtons;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static final int CANDIDAT = 0;
    public static final int EMPLOYEUR = 1;
    private static final String TYPE_CASE = "type inscription";


    public static SignInFragment newInstance(int typeCase) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_CASE, typeCase);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signInForm = new FormLayout(this.requireContext());
        signInButtons = new ButtonSetLayout(this.getContext(),"#FFFFFF");

        switch (requireArguments().getInt(TYPE_CASE)){
            case CANDIDAT:
                inscriptionCandidat();
                break;
            case EMPLOYEUR:
                inscriptionEmployeur();
                break;
        }

        LinearLayout fragmentLayout = view.findViewById(R.id.mainSignInLayout);
        fragmentLayout.addView(signInForm);
        fragmentLayout.addView(signInButtons);
    }

    private void inscriptionCandidat(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 20, 100, 20);

        signInForm.setLayoutParams(layoutParams);
        signInForm.addTextSection(new LinkedHashMap<String,Class<?>>() {{
            put("nom", String.class);
            put("prenom", String.class);
            put("age", Integer.class);
            put("ville", String.class);
            put("email", String.class);
            put("pseudo", String.class);
            put("motdepasse", String.class);
        }});
        signInForm.addImportSection("cv", createFunctionToButton("cv", new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri o) {
                signInForm.modifyFilePathView("cv",o.toString());
            }
        }));

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(100,20,100,20);

        signInButtons.setLayoutParams(layoutParams2);
        ButtonSetLayout.ButtonParam  inscription = new ButtonSetLayout.ButtonParam("S'inscrire","#5CE98C", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = null;
                try {
                    data = signInForm.getData();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                String pseudo = (String) data.remove("pseudo");
                data.put("cv", importerCV((String) data.get("cv"),pseudo));

                db.collection("candidats").document(pseudo)
                        .set(data)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            Toast.makeText(SignInFragment.this.getContext(),"Inscription REUSSIE",Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Error writing document", e);
                            Toast.makeText(SignInFragment.this.getContext(),"Inscription ECHOUEE",Toast.LENGTH_LONG).show();

                        });
                //TODO lancer l'activiter candidat main menu
            }
        });
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(SignInFragment.this).commit();
            }
        });

        signInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,inscription});
    }

    public void inscriptionEmployeur(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(100, 20, 100, 20);

        signInForm.setLayoutParams(layoutParams);
        signInForm.addTextSection(new LinkedHashMap<String, Class<?>>() {{
            put("nom", String.class);
            put("adresse", String.class);
            put("numero", Integer.class);
            put("email", String.class);
            put("pseudo", String.class);
            put("motdepasse", String.class);
        }});

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(100,20,100,20);

        signInButtons.setLayoutParams(layoutParams2);
        ButtonSetLayout.ButtonParam  inscription = new ButtonSetLayout.ButtonParam("S'inscrire","#5CE98C", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = null;
                try {
                    data = signInForm.getData();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
                String pseudo = (String) data.remove("pseudo");
                db.collection("employeurs").document(pseudo)
                        .set(data)
                        .addOnSuccessListener(aVoid -> {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                            requireActivity().finish();
                            Intent intent = new Intent(getActivity(), EmployeurActivity.class);
                            intent.putExtra("identifiant", pseudo);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Log.w(TAG, "Error writing document", e));
            }
        });
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(SignInFragment.this).commit();
            }
        });

        signInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,inscription});
    }

    private String importerCV(String cheminVersCV, String idUser){
        if (!cheminVersCV.equals("")){
            StorageReference storageRef = storage.getReference().child("Candidats/"+idUser+"/cv");
            File file = new File(cheminVersCV);
            Uri uri = Uri.fromFile(file);

            storageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG,"Fichier ajouter à la base de données");
                    })
                    .addOnFailureListener(exception -> {
                        Log.d(TAG,"ERREUR importation de fichier vers la base de données");
                    });
            return "Candidats/"+idUser+"/cv/"+cheminVersCV.substring(cheminVersCV.lastIndexOf(File.separator) + 1);
        } else return "";
    }


    // TODO créer une fonction permettant de vérifier si les identifiants n'existerait pas déjà
}
