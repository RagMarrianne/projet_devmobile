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

        // Define the design of our layout
        GradientDrawable mainLayoutgradientDrawable = new GradientDrawable();
        mainLayoutgradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mainLayoutgradientDrawable.setCornerRadius(30);
        mainLayoutgradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(20,20,20,20);

        // Application of our design to the layout
        this.setBackground(mainLayoutgradientDrawable);
        this.setPadding(30,30,30,30);
        this.setOrientation(VERTICAL);
        this.setLayoutParams(layoutParam);
        this.setId(View.generateViewId());

        // Add the sections to the layout
        initLayout(offre);
    }

    @SuppressLint("SetTextI18n")
    private void initLayout(Offre offre){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.addView(generateTextView("Poste : "+offre.getMetiercible()));
        this.addView(generateTextView("Du "+dateFormat.format(offre.getDatedebut())+" au "+dateFormat.format(offre.getDatefin())));
        this.addView(generateTextView("Rémunéré au hauteur de "+ offre.getRemuneration() +"€"));
        this.addView(generateDescriptionView(offre.getDescription()));
    }

    private TextView generateTextView(String text){
        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        TextView view = new TextView(this.getContext());
        view.setLayoutParams(layoutParam);
        view.setText(text);
        view.setId(View.generateViewId());

        return view;
    }

    private TextView generateDescriptionView(String text){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(20);
        gradientDrawable.setColor(Color.WHITE);

        LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParam.setMargins(0,5,0,5);

        TextView descriptionView = new TextView(this.getContext());
        descriptionView.setPadding(10,10,10,10);
        descriptionView.setBackground(gradientDrawable);
        descriptionView.setLayoutParams(layoutParam);
        descriptionView.setText(text);
        descriptionView.setId(View.generateViewId());

        return descriptionView;
    }

}
