package com.example.projet_devmobile.fragment.general;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.activity.EmployeurActivity;
import com.example.projet_devmobile.activity.ReceptionActivity;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;

import java.util.HashMap;
import java.util.Map;

public class ProfilSettingFragment extends Fragment {
    private static final String TYPE_CASE = "type utilisateur";
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
        Map<String,Object> options = new HashMap<>();
        options.put("Sign In as Candidat", SignInFragment.newInstance(SignInFragment.CANDIDAT));
        options.put("Sign In as Employeur", SignInFragment.newInstance(SignInFragment.EMPLOYEUR));
        addOptions(options);
    }
    private void menuCandidat(){
        Map<String,Object> options = new HashMap<>();
        //options.put("Candidatures en cours", new ListeCandidaturesCFragment());
        //options.put("Candidatures traitées", new ListeCandidaturesTFragment());
        //options.put("Back to main menu", CandidatActivity.class);
        options.put("Log Out", ReceptionActivity.class);

        addOptions(options);

    }
    private void menuEmployeur(){
        Map<String,Object> options = new HashMap<>();
        options.put("Log Out", ReceptionActivity.class);
        options.put("Back to main menu", EmployeurActivity.class);
        addOptions(options);

    }

    private void addOptions(Map<String,Object> options){
        for (Map.Entry<String, Object> option : options.entrySet()){

            LinearLayout.LayoutParams labelLayoutParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            labelLayoutParam.gravity = Gravity.CENTER;
            labelLayoutParam.setMargins(20,20,20,20);

            TextView label = new TextView(this.getContext());
            label.setLayoutParams(labelLayoutParam);
            label.setTextSize(TypedValue.COMPLEX_UNIT_PX, 70);
            label.setTextColor(getResources().getColor(R.color.black));
            label.setPaintFlags(label.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            label.setId(View.generateViewId());
            label.setText(option.getKey());

            if (option.getValue() instanceof Class<?>){
                label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requireActivity().finish();
                        startActivity(new Intent(getActivity(), (Class<?>) option.getValue()));
                    }
                });
            } else {
                label.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        requireActivity().getSupportFragmentManager().beginTransaction()
                                .remove(ProfilSettingFragment.this)
                                .replace(R.id.mainMenu,(Fragment) option.getValue())
                                .commit();
                    }
                });
            }
            menuContentLayout.addView(label);
        }
    }

}
