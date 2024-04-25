package com.example.projet_devmobile.layouts_utilitaires;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.example.projet_devmobile.fragment.general.PDFViewerFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class EntitieLayout extends LinearLayout {
    private final GradientDrawable gradientDrawable = new GradientDrawable();

    public EntitieLayout(Context context) {
        super(context);
        GradientDrawable mainLayoutgradientDrawable = new GradientDrawable();
        mainLayoutgradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mainLayoutgradientDrawable.setCornerRadius(60);
        mainLayoutgradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        this.setBackground(mainLayoutgradientDrawable);
        this.setPadding(50,50,50,50);
        this.setOrientation(VERTICAL);
        this.setId(View.generateViewId());

        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setColor(Color.WHITE);
    }

    /**METHODS**/
    @SuppressLint("SetTextI18n")
    public void addTextSection(String label, String value){

        LayoutParams labelLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        TextView sectionView = new TextView(this.getContext());
        sectionView.setLayoutParams(labelLayoutParam);
        sectionView.setPadding(10,10,10,10);
        sectionView.setId(View.generateViewId());
        sectionView.setText(label+" : "+value);
        this.addView(sectionView);
    }
    public void addTextSection(LinkedHashMap<String, Object> sections){
        for (Map.Entry<String, Object> entry : sections.entrySet()) {
            addTextSection(entry.getKey(),convertToString(entry.getValue()));
        }
    }

    public void addPDFSection(String label, String docPDFpath, int mainLayout){
        LayoutParams labelLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);

        TextView sectionView = new TextView(this.getContext());
        sectionView.setPadding(10,10,10,10);
        sectionView.setLayoutParams(labelLayoutParam);
        sectionView.setId(View.generateViewId());
        sectionView.setText(label+" :");
        this.addView(sectionView);

        LayoutParams buttonLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        buttonLayoutParam.setMargins(30,20,30,20);

        Button showDocButton = new Button(this.getContext());
        showDocButton.setLayoutParams(buttonLayoutParam);
        showDocButton.setId(View.generateViewId());
        showDocButton.setText("Afficher "+label);
        showDocButton.setOnClickListener((v -> ((FragmentActivity) EntitieLayout.this.getContext()).getSupportFragmentManager().beginTransaction()
                .addToBackStack(null)
                .replace(mainLayout, PDFViewerFragment.newInstance(docPDFpath))
                .commit()));
        this.addView(showDocButton);
    }

    /**UTIL METHODS**/
    public static String convertToString(Object obj) {
        if (obj instanceof Integer) {
            return Integer.toString((int) obj);
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Double) {
            return Double.toString((Double) obj);
        }else if (obj instanceof Date) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.format((Date) obj);
        } else {
            return null;
        }
    }
}
