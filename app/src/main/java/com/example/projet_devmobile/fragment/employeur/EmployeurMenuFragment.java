package com.example.projet_devmobile.fragment.employeur;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.UserDataViewModel;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class EmployeurMenuFragment extends Fragment {
    private LinearLayout menuLayout;
    private UserDataViewModel userDataViewModel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_menu, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userDataViewModel = new ViewModelProvider(this.getActivity()).get(UserDataViewModel.class);

        menuLayout = view.findViewById(R.id.usermenucontent);
        initMenuLayout();

    }

    private void initMenuLayout(){
        Map<String, Fragment> options = new LinkedHashMap<>();
        options.put("Déposer une offre", NouvelleOffreFragment.newInstance(userDataViewModel.getUserId().getValue()));
        options.put("Consulter les offres",ListeOffresFragment.newInstance(userDataViewModel.getUserId().getValue()));
        options.put("Gérer les candidatures en cours", ListeCandidatureEFragment.newInstance(Candidature.EN_COURS, userDataViewModel.getUserId().getValue()));
        options.put("Gérer les candidatures traitées", ListeCandidatureEFragment.newInstance(Candidature.TRAITEE, userDataViewModel.getUserId().getValue()));
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