package com.example.projet_devmobile.fragment.candidat;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.ScreenUtils;
import com.example.projet_devmobile.layouts_utilitaires.FormLayout;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class FilterFragment extends Fragment {
    public enum TYPEDATA {
        NUMBER,
        STRING,
        DATE
    }
    public static final String TYPE_FILTER = "type";
    public static final String STATUS = "status";
    public static final String IS_ANONYME = "isAnonyme";
    public static final int SEARCH = 0;
    public static final int FILTER = 1;
    private int type;
    private String candidatureStatus;
    private boolean isAnonyme;
    private ImageButton validerButton;
    private FormLayout formLayout;


    public static FilterFragment newInstance(int typeFilter, @Nullable String statusCandidature) {
        Bundle args = new Bundle();
        FilterFragment fragment = new FilterFragment();
        args.putInt(TYPE_FILTER,typeFilter);
        args.putString(STATUS,statusCandidature);
        args.putBoolean(IS_ANONYME,false);
        fragment.setArguments(args);
        return fragment;
    }
    public static FilterFragment newInstanceAnonyme(int typeFilter, @Nullable String statusCandidature) {
        Bundle args = new Bundle();
        FilterFragment fragment = new FilterFragment();
        args.putInt(TYPE_FILTER,typeFilter);
        args.putString(STATUS,statusCandidature);
        args.putBoolean(IS_ANONYME,true);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(TYPE_FILTER);
            candidatureStatus = getArguments().getString(STATUS);
            isAnonyme = getArguments().getBoolean(IS_ANONYME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        validerButton= new ImageButton(this.getContext());
        formLayout = new FormLayout(this.requireContext());
        formLayout.changeBackgroundColor("#FFFFFF");

        LinearLayout mainLayout = view.findViewById(R.id.filterContent);
        mainLayout.addView(formLayout);
        mainLayout.addView(validerButton);

        createSection();
    }

    private void createSection(){
        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor("#FFFFFF"));

        formLayout.addTextSection(new LinkedHashMap<String,Class<?>>(){{
            put("metiercible", String.class);
            put("remuneration", Integer.class);
            put("datedebut", Date.class);
        }});

        validerButton.setLayoutParams(new LinearLayout.LayoutParams((int) (ScreenUtils.getScreenWidth(this.requireContext()) * 0.2), (int) (ScreenUtils.getScreenWidth(this.requireContext()) * 0.2)));
        validerButton.setBackground(shape);
        validerButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        validerButton.setId(View.generateViewId());

        switch (type){
            case SEARCH:
                validerButton.setImageResource(R.drawable.search);
                break;
            case FILTER:
                validerButton.setImageResource(R.drawable.filter);
                break;
        }

        validerButton.setOnClickListener(v -> changerDeFragment());
    }

    private void changerDeFragment(){
        Map<String, Object> data = null;
        try {
            data = formLayout.getData();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment = null;

        switch (type){
            case SEARCH:
                if (isAnonyme)
                    fragment = CandidatAnonymeMenuFragment.newInstance(data);
                else
                    fragment = CandidatMenuFragment.newInstance(data);
                break;
            case FILTER:
                fragment = ListeCandidatureCFragment.newInstance(candidatureStatus,data);
                break;
        }

        transaction.remove(this)
                .replace(R.id.menuContent, fragment)
                .commit();
    }
}
