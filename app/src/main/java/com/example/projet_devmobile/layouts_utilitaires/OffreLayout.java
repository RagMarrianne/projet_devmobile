package com.example.projet_devmobile.layouts_utilitaires;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projet_devmobile.classesUtilitaires.Offre;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("ViewConstructor")
public class OffreLayout extends LinearLayout {

    public OffreLayout(Context context, Offre offre) {
        super(context);

        GradientDrawable mainLayoutgradientDrawable = new GradientDrawable();
        mainLayoutgradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mainLayoutgradientDrawable.setCornerRadius(30);
        mainLayoutgradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20,20,20,20);

        this.setBackground(mainLayoutgradientDrawable);
        this.setPadding(30,30,30,30);
        this.setOrientation(VERTICAL);
        this.setLayoutParams(layoutParam);
        this.setId(View.generateViewId());

        initLayout(offre);
    }

    @SuppressLint("SetTextI18n")
    private void initLayout(Offre offre){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(Color.WHITE);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        TextView metier = new TextView(this.getContext());
        metier.setLayoutParams(layoutParam);
        metier.setText("Poste : "+offre.getMetiercible());
        metier.setId(View.generateViewId());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TextView date = new TextView(this.getContext());
        date.setLayoutParams(layoutParam);
        date.setText("Du "+dateFormat.format(offre.getDatedebut())+" au "+dateFormat.format(offre.getDatefin()));
        date.setId(View.generateViewId());

        TextView remuneratioin = new TextView(this.getContext());
        remuneratioin.setLayoutParams(layoutParam);
        remuneratioin.setText("Rémunéré au hauteur de "+String.valueOf(offre.getRemuneration())+"€");
        remuneratioin.setId(View.generateViewId());

        TextView description = new TextView(this.getContext());
        description.setPadding(10,10,10,10);
        description.setBackground(gradientDrawable);
        description.setLayoutParams(layoutParam);
        description.setText(offre.getDescription());
        description.setId(View.generateViewId());

        this.addView(metier);
        this.addView(date);
        this.addView(remuneratioin);
        this.addView(description);
    }

}
