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

import java.util.HashMap;
import java.util.Map;


public class EmployeurMenuFragment extends Fragment {
    private LinearLayout menuLayout;

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

    private void initMenuLayout(){ // TODO
        Map<String, Fragment> options = new HashMap<>();
        options.put("Déposer une offre", NouvelleOffreFragment.newInstance("crous"));
        options.put("Consulter les offres",new ListeOffresFragment());
        options.put("Gérer les candidatures en cours",new Fragment());
        options.put("Gérer les candidatures acceptées",new Fragment());
        addOptions(options, menuLayout);

    }

    private void addOptions(Map<String,Fragment> options, LinearLayout parentLayout){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);
        gradientDrawable.setColor(Color.parseColor("#9D8F8F"));

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
                    .remove(this)
                    .replace(R.id.menuContent, option.getValue())
                    .commit());
            parentLayout.addView(button);
        }
    }
}