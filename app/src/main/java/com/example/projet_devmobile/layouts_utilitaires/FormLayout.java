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
    private Map<String,ActivityResultLauncher<String>> filePickerLaunchers;

    private Map<String,EditText> editsViews = new HashMap<>();
    private Map<String, Button> buttonsViews = new HashMap<>();
    private Map<String, TextView> filesPathsViews = new HashMap<>();
    private Map<String, Class<?>> typeData = new HashMap<>();
    private final GradientDrawable gradientDrawable = new GradientDrawable();
    private GradientDrawable mainLayoutgradientDrawable = new GradientDrawable();

    LayoutParams labelLayoutParam;

    public FormLayout(@NonNull Context context) {
        super(context);

        // Init the design by default of our form
        mainLayoutgradientDrawable.setShape(GradientDrawable.RECTANGLE);
        mainLayoutgradientDrawable.setCornerRadius(60);
        mainLayoutgradientDrawable.setColor(Color.parseColor("#EFE7E7"));

        // Apply the design to the layout
        this.setBackground(mainLayoutgradientDrawable);
        this.setPadding(50,50,50,50);
        this.setOrientation(VERTICAL);
        this.setId(View.generateViewId());

        // Init the design of the label of our future sections
        labelLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        labelLayoutParam.setMargins(40,0,30,0);

        // Init the design of the sections which will be present in the form
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(30);
        gradientDrawable.setStroke(5, Color.BLACK);
        gradientDrawable.setColor(Color.WHITE);

    }

    /**METHODES**/
    public void addTextSection(String label){

        // Add label to the section
        this.addView(generateLabelView(label));

        LayoutParams editLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        editLayoutParam.setMargins(30,20,30,20);

        // Add text input area to the section
        EditText editView = new EditText(this.getContext());
        editView.setLayoutParams(editLayoutParam);
        editView.setPadding(10,30,10, 30);
        editView.setBackground(gradientDrawable);
        editView.setId((View.generateViewId()));

        editsViews.put(label,editView);
        this.addView(editView);
    }
    public void addTextSection(Map<String, Class<?>> sections){
        for (Map.Entry<String, Class<?>> entry : sections.entrySet()) {
            typeData.put(entry.getKey(),entry.getValue());
            addTextSection(entry.getKey());
        }
    }

    @SuppressLint("SetTextI18n")
    public void addImportSection(String label, ActivityResultLauncher<String> buttonFunction){

        // Add label to the section
        this.addView(generateLabelView(label));

        // Add a text area where the file path of the document selected will be printed
        TextView fileSelectedView = generateLabelView("");
        filesPathsViews.put(label,fileSelectedView);
        this.addView(fileSelectedView);

        LayoutParams buttonLayoutParam = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        buttonLayoutParam.setMargins(30,20,30,20);

        GradientDrawable buttonLayoutDrawable = new GradientDrawable();
        buttonLayoutDrawable.setShape(GradientDrawable.RECTANGLE);
        buttonLayoutDrawable.setCornerRadius(70);
        buttonLayoutDrawable.setColor(Color.WHITE);

        // Add the button which will allow us to select a file present in our phone
        Button buttonSelectFile = new Button(this.getContext());
        buttonSelectFile.setBackground(buttonLayoutDrawable);
        buttonSelectFile.setLayoutParams(buttonLayoutParam);
        buttonSelectFile.setId(View.generateViewId());
        buttonSelectFile.setText("Importer "+label);
        buttonSelectFile.setOnClickListener((v -> buttonFunction.launch("application/pdf"))); // Only allow pdf files

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

    // Generate un simple textView
    private TextView generateLabelView(String text){
        TextView view = new TextView(this.getContext());
        view.setLayoutParams(labelLayoutParam);
        view.setTextColor(Color.BLACK);
        view.setId(View.generateViewId());
        view.setText(text);

        return view;
    }

    // Used to collect the given data on the form
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

    // Print on the layout the file path to the selected file
    public void putValuesToEditAreas( Map<String,Object> data){
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            editsViews.get(entry.getKey()).setText(convertToString(entry.getValue()));
        }
    }

    // Convert the string wrote to the desire type
    public static Object convert(String value, Class<?> type) throws ParseException {
        if (value.equals("")){
            return null;
        } else if (type == String.class) {
            return value;
        } else if (type == Integer.class || type == int.class) {
            return Integer.parseInt(value);
        }else if (type == Double.class || type == double.class) {
            return Double.parseDouble(value);
        } else if (type == Date.class) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(value);
        } else {
            throw new IllegalArgumentException("Type de conversion non supporté : " + type.getSimpleName());
        }
    }

    // Convert the given object to a string
    public static String convertToString(Object obj) {
        if (obj instanceof Integer) {
            return Integer.toString((int) obj);
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Double) {
            return Double.toString((Double) obj);
        }else if (obj instanceof Date) {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format((Date) obj);
        } else {
            return null;
        }
    }

    public void changeBackgroundColor(String hexFont){
        mainLayoutgradientDrawable.setColor(Color.parseColor(hexFont));
    }
}
