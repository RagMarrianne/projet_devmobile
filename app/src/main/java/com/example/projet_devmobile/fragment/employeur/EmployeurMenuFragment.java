package com.example.projet_devmobile.fragment.employeur;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidature;

import java.util.HashMap;
import java.util.Map;


public class EmployeurMenuFragment extends Fragment {
    private LinearLayout menuLayout;
    private static final String IDENTIFIANT = "identifiant";

    public static EmployeurMenuFragment newInstance(String identifiant) {

        Bundle args = new Bundle();
        args.putString(IDENTIFIANT,identifiant);
        EmployeurMenuFragment fragment = new EmployeurMenuFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_employeur_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        menuLayout = view.findViewById(R.id.employeurmenucontent);
        initMenuLayout();

    }

    private void initMenuLayout(){
        Map<String, Fragment> options = new HashMap<>();
        options.put("Déposer une offre", NouvelleOffreFragment.newInstance(getArguments().getString(IDENTIFIANT)));
        options.put("Consulter les offres",ListeOffresFragment.newInstance(getArguments().getString(IDENTIFIANT)));
        options.put("Gérer les candidatures en cours",ListeCandidatureFragment.newInstance(Candidature.EN_COURS, getArguments().getString(IDENTIFIANT)));
        options.put("Gérer les candidatures acceptées",ListeCandidatureFragment.newInstance(Candidature.ACCEPTE, getArguments().getString(IDENTIFIANT)));
        addOptions(options, menuLayout);

    }

    private void addOptions(Map<String,Fragment> options, LinearLayout parentLayout){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);
        gradientDrawable.setColor(Color.parseColor("#F4DCDC"));

        LinearLayout.LayoutParams buttonLayoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParam.gravity = Gravity.CENTER;
        buttonLayoutParam.setMargins(20,20,20,20);

        for (Map.Entry<String, Fragment> option : options.entrySet()){

            Button button = new Button(this.getContext());
            button.setLayoutParams(buttonLayoutParam);
            button.setBackground(gradientDrawable);
            button.setId(View.generateViewId());
            button.setText(option.getKey());


            button.setOnClickListener(onClickListener -> requireActivity().getSupportFragmentManager().beginTransaction()
                    .addToBackStack("main menu")
                    .replace(R.id.menuContent, option.getValue())
                    .commit());
            parentLayout.addView(button);
        }
    }
}