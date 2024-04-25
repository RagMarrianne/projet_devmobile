package com.example.projet_devmobile.fragment.general;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.activity.ReceptionActivity;
import com.example.projet_devmobile.fragment.employeur.EmployeurMenuFragment;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfilSettingFragment extends Fragment {
    private static final String TYPE_CASE = "type utilisateur";
    private static final String IDENTIFIANT = "identifiant";
    private static final int CANDIDAT = 0;
    private static final int EMPLOYEUR = 1;
    private static final int ANONYME = 2;

    private LinearLayout menuContentLayout;
    public static ProfilSettingFragment newInstance(int typeCase) {
        ProfilSettingFragment fragment = new ProfilSettingFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_CASE, typeCase);
        fragment.setArguments(args);
        return fragment;
    }
    public static ProfilSettingFragment newInstance(int typeCase, String identifiant) {
        ProfilSettingFragment fragment = new ProfilSettingFragment();
        Bundle args = new Bundle();
        args.putInt(TYPE_CASE,typeCase);
        args.putString(IDENTIFIANT,identifiant);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_profilsetting, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        menuContentLayout = view.findViewById(R.id.profilSettingContent);

        switch (requireArguments().getInt(TYPE_CASE)){
            case ANONYME:
                menuAnonyme();
                break;
            case CANDIDAT:
                menuCandidat();
                break;
            case EMPLOYEUR:
                menuEmployeur();
                break;
        }
        ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().getSupportFragmentManager().beginTransaction().remove(ProfilSettingFragment.this).commit();
            }
        });
        ButtonSetLayout bs = new ButtonSetLayout(ProfilSettingFragment.this.getContext(),"#D8FFE5");
        bs.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back});
        LinearLayout buttonSection = view.findViewById(R.id.backbutton);
        buttonSection.addView(bs);}


    private void menuAnonyme(){
        addSignIn();
    }
    private void menuCandidat(){
        /*Map<String,Object> options = new HashMap<>();
        //options.put("Candidatures en cours", new ListeCandidaturesCFragment());
        //options.put("Candidatures traitées", new ListeCandidaturesTFragment());
        //options.put("Back to main menu", CandidatActivity.class);
        options.put("Log Out", ReceptionActivity.class);

        addOptions(options);*/

    }
    private void menuEmployeur(){
        addBackToMainMenu();
        addLogOut();
    }

    private void addBackToMainMenu(){
        Button button = initButton("Revenir au menu principal");
        button.setOnClickListener(onClickListener -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .remove(this).commit();
            requireActivity().getSupportFragmentManager()
                    .popBackStack("main menu", FragmentManager.POP_BACK_STACK_INCLUSIVE);});
        menuContentLayout.addView(button);
    }

    private void addLogOut(){
        Button button = initButton("Log Out");
        button.setOnClickListener(v -> {
            requireActivity().finish();
            startActivity(new Intent(getActivity(), ReceptionActivity.class));
        });
        menuContentLayout.addView(button);
    }

    private void addSignIn(){
        Button candidat = initButton("S'inscrire comme candidat");
        candidat.setOnClickListener(onClickListener -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .replace(R.id.mainLayout,SignInFragment.newInstance(SignInFragment.CANDIDAT))
                .commit());
        menuContentLayout.addView(candidat);
        Button employeur = initButton("S'inscrire comme employeur");
        employeur.setOnClickListener(onClickListener -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(this)
                .replace(R.id.mainLayout,SignInFragment.newInstance(SignInFragment.EMPLOYEUR))
                .commit());
        menuContentLayout.addView(employeur);
    }

    private Button initButton(String text){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);
        gradientDrawable.setColor(Color.parseColor("#FFFFFF"));

        LinearLayout.LayoutParams buttonLayoutParam = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonLayoutParam.gravity = Gravity.CENTER;
        buttonLayoutParam.setMargins(20,20,20,20);

        Button button = new Button(this.getContext());
        button.setLayoutParams(buttonLayoutParam);
        button.setBackground(gradientDrawable);
        button.setId(View.generateViewId());
        button.setText(text);

        return button;
    }

}
