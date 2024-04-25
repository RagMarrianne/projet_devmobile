package com.example.projet_devmobile.layouts_utilitaires;


import android.annotation.SuppressLint;
import android.content.Context;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FormLayout extends LinearLayout {

    private Map<String,EditText> editsViews = new HashMap<>();
    private Map<String, Button> buttonsViews = new HashMap<>();
    private Map<String, TextView> filesPathsViews = new HashMap<>();
    private Map<String, Class<?>> typeData;
    private final GradientDrawable gradientDrawable = new GradientDrawable();


    public FormLayout(@NonNull Context context) {
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

    /**METHODES**/
    public void addTextSection(String label){

        LayoutParams labelLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        labelLayoutParam.setMargins(40,0,30,0);

        TextView sectionView = new TextView(this.getContext());
        sectionView.setLayoutParams(labelLayoutParam);
        sectionView.setId(View.generateViewId());
        sectionView.setText(label);
        this.addView(sectionView);

        LayoutParams editLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        editLayoutParam.setMargins(30,20,30,20);

        EditText editView = new EditText(this.getContext());
        editView.setLayoutParams(editLayoutParam);
        editView.setPadding(10,30,10, 30);
        editView.setBackground(gradientDrawable);
        editView.setId((View.generateViewId()));
        editsViews.put(label,editView);
        this.addView(editView);
    }
    public void addTextSection(Map<String, Class<?>> sections){
        typeData.putAll(sections);
        for (Map.Entry<String, Class<?>> entry : sections.entrySet()) {
            addTextSection(entry.getKey());
        }
    }

    public void addImportSection(String label, ActivityResultLauncher<String> buttonFunction){

        LayoutParams labelLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        labelLayoutParam.setMargins(30,0,30,0);

        TextView sectionView = new TextView(this.getContext());
        sectionView.setLayoutParams(labelLayoutParam);

        sectionView.setId(View.generateViewId());
        sectionView.setText(label);
        this.addView(sectionView);

        TextView fileselectedView = new TextView(this.getContext());
        fileselectedView.setLayoutParams(labelLayoutParam);

        fileselectedView.setId(View.generateViewId());
        filesPathsViews.put(label,fileselectedView);
        this.addView(fileselectedView);


        LayoutParams buttonLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        buttonLayoutParam.setMargins(30,20,30,20);

        Button buttonSelectFile = new Button(this.getContext());
        buttonSelectFile.setLayoutParams(buttonLayoutParam);
        buttonSelectFile.setId(View.generateViewId());
        buttonSelectFile.setText("Importer "+label);
        buttonSelectFile.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonFunction.launch("*/*");
            }
        }));
        buttonsViews.put(label,buttonSelectFile);
        this.addView(buttonSelectFile);

    }

    /**GETTERS AND SETTERS**/

    public Map<String, EditText> getEditsViews() {
        return editsViews;
    }

    public Map<String, TextView> getFilesPathsViews() {
        return filesPathsViews;
    }

    public void modifyFilePathView(String label, String text) {
        Objects.requireNonNull(this.filesPathsViews.get(label)).setText(text);
    }

    /**UTIL METHODS**/
    public Map<String,Object> getData() throws ParseException {
        Map<String, Object> data = new HashMap<>();
        for (Map.Entry<String, EditText> entry : editsViews.entrySet()) {
            data.put(entry.getKey(), convert(entry.getValue().getText().toString(), typeData.get(entry.getKey())));
        }
        for (Map.Entry<String, TextView> entry : filesPathsViews.entrySet()) {
            data.put(entry.getKey(), entry.getValue().getText().toString());
        }
        return data;
    }

    public void putValuesToEditAreas( Map<String,Object> data){
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            editsViews.get(entry.getKey()).setText(convertToString(entry.getValue()));
        }
    }

    public static Object convert(String value, Class<?> type) throws ParseException {
        if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Date.class) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String v = value.split(" ")[0];
            return dateFormat.parse(v+= "T0:00:00.000Z");
        } else {
            throw new IllegalArgumentException("Type de conversion non supporté : " + type.getSimpleName());
        }
    }

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
