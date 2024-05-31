package com.example.projet_devmobile.fragment.commun;

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

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activity.CandidateActivity;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.fragment.candidat.CandidatAnonymeMenuFragment;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.util.LinkedHashMap;
import java.util.Map;

public class SignInFragment extends Fragment {
    private FormLayout signInForm;
    private ButtonSetLayout signInButtons;
    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static final int CANDIDATE = 0;
    public static final int EMPLOYER = 1;
    public static final String FROM_ANONYMOUS_MENU = "anonyme_menu";
    private static final String TYPE_CASE = "type inscription";


    public static SignInFragment newInstance(int typeCase, boolean fam) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_CASE, typeCase);
        args.putBoolean(FROM_ANONYMOUS_MENU, fam);
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
            case CANDIDATE:
                inscriptionCandidat();
                break;
            case EMPLOYER:
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

        // Init sign in Form
        signInForm.setLayoutParams(layoutParams);
        signInForm.addTextSection(new LinkedHashMap<String,Class<?>>() {{
            put("nom", String.class);
            put("prenom", String.class);
            put("age", Integer.class);
            put("ville", String.class);
            put("email", String.class);
            put("numero", Integer.class);
            put("pseudo", String.class);
            put("motdepasse", String.class);
        }});
        signInForm.addImportSection("cv", initFilePicker());

        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams2.setMargins(100,20,100,20);

        signInButtons.setLayoutParams(layoutParams2);
        ButtonSetLayout.ButtonParam inscription = new ButtonSetLayout.ButtonParam("S'inscrire","#5CE98C", v -> {

            // Collect all the given data
            Map<String, Object> data = null;
            try {
                data = signInForm.getData();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            String pseudo = (String) data.remove("pseudo");
            data.put("cv", importerCV((String) data.get("cv"),pseudo));

            // Create a new document with the given information and with the pseudo as key
            db.collection("candidats").document(pseudo)
                    .set(data)
                    .addOnSuccessListener(aVoid -> {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        requireActivity().finish();
                        Intent intent = new Intent(getActivity(), CandidateActivity.class);
                        intent.putExtra("identifiant", pseudo);
                        startActivity(intent); // If the creation of the new account is a success, run the candidate activity
                    })
                    .addOnFailureListener(e -> {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(SignInFragment.this.getContext(),"Inscription ECHOUEE",Toast.LENGTH_LONG).show();
                    });
        });
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v -> {
            if (getArguments().getBoolean(FROM_ANONYMOUS_MENU)){
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(SignInFragment.this)
                        .replace(R.id.menuContent, new CandidatAnonymeMenuFragment())
                        .commit();
            } else {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(SignInFragment.this).commit();
            }
        });

        signInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,inscription});
    }

    // Same thing for the employer
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
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", v -> {
            if (getArguments().getBoolean(FROM_ANONYMOUS_MENU)){
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .remove(SignInFragment.this)
                        .replace(R.id.menuContent, new CandidatAnonymeMenuFragment())
                        .commit();
            } else {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(SignInFragment.this).commit();
            }
        });

        signInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back,inscription});
    }

    // A function to upload the cv file to the database firebase Storage
    private String importerCV(String filePath, String idUser){
        if (!filePath.equals("")){
            StorageReference storageRef = storage.getReference().child("Candidats/"+idUser+"/cv");
            File file = new File(filePath);
            Uri uri = Uri.fromFile(file);

            storageRef.putFile(uri).addOnSuccessListener(taskSnapshot -> {
                        Log.d(TAG,"Fichier ajouter à la base de données");
                    })
                    .addOnFailureListener(exception -> {
                        Log.d(TAG,"ERREUR importation de fichier vers la base de données");
                    });
            return "Candidats/"+idUser+"/cv/"+filePath.substring(filePath.lastIndexOf(File.separator) + 1);
        } else return "";
    }

    // A function to select a file from your Phone
    private ActivityResultLauncher<String> initFilePicker(){
        return registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        Log.d(TAG, "URI sélectionnée : " + uri);
                        signInForm.modifyFilePathView("cv",uri.toString());
                    } else {
                        Log.d(TAG,"Aucun fichier selectionné");
                    }
                });
    }
}
