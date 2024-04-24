package com.example.projet_devmobile.layouts_utilitaires;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.graphics.drawable.GradientDrawable;


import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashMap;
import java.util.Map;

@SuppressLint("ViewConstructor")
public class ButtonSetLayout extends LinearLayout {
    public static class ButtonParam{
         public int resource;
         public String label;
         public OnClickListener buttonFunction;
         public String fontHexacode;
         public boolean isAImageButton = false;
        public ButtonParam(int resource, String label,  String fontHexacode, OnClickListener buttonFunction){
            this.resource = resource;
            this.label = label;
            this.buttonFunction = buttonFunction;
            this.fontHexacode = fontHexacode;
            this.isAImageButton = true;
        }
        public ButtonParam(String label, String fontHexacode, OnClickListener buttonFunction){
            this.label = label;
            this.buttonFunction = buttonFunction;
            this.fontHexacode = fontHexacode;
        }
    }
    private LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
    );
    private LinearLayout.LayoutParams sectionlayoutParams = new LinearLayout.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
    );
    private LinearLayout.LayoutParams buttonlayoutParams = new LinearLayout.LayoutParams(
            0,
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            1
    );

    private Map<String, View> buttonMap = new HashMap<>();

    public ButtonSetLayout(Context context, String fontHexaCode) {
        super(context);

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(40);
        gradientDrawable.setColor(Color.parseColor(fontHexaCode));

        layoutParams.setMargins(0, 20, 0, 20);
        sectionlayoutParams.setMargins(0, 20, 0, 20);
        buttonlayoutParams.setMargins(20, 0, 20, 0);

        this.setId(View.generateViewId());
        this.setLayoutParams(layoutParams);
        this.setBackground(gradientDrawable);
        this.setOrientation(LinearLayout.VERTICAL);
    }

    private void addTextButton(ButtonParam buttonParam){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);
        gradientDrawable.setColor(Color.parseColor(buttonParam.fontHexacode));

        Button button = new Button(this.getContext());
        button.setId(View.generateViewId());
        button.setLayoutParams(buttonlayoutParams);
        button.setText(buttonParam.label);
        button.setOnClickListener(buttonParam.buttonFunction);
        button.setBackground(gradientDrawable);
        buttonMap.put(buttonParam.label,button);
    }

    private void addImageButton(ButtonParam buttonParam){
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(70);
        gradientDrawable.setColor(Color.parseColor(buttonParam.fontHexacode));

        ImageButton button = new ImageButton(this.getContext());
        button.setId(View.generateViewId());
        button.setLayoutParams(new LayoutParams(
                150,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        button.setBackground(gradientDrawable);
        button.setScaleType(ImageView.ScaleType.FIT_CENTER);
        button.setImageResource(buttonParam.resource);
        button.setOnClickListener(buttonParam.buttonFunction);
        buttonMap.put(buttonParam.label,button);
    }

    public void addButtonsSection(ButtonParam[] buttonsParam){
        LinearLayout section = new LinearLayout(this.getContext());
        section.setLayoutParams(sectionlayoutParams);
        section.setOrientation(LinearLayout.HORIZONTAL);
        section.setGravity(Gravity.CENTER_HORIZONTAL);
        section.setId(View.generateViewId());

        for (ButtonParam buttonParam : buttonsParam){
            if (buttonParam.isAImageButton)
                addImageButton(buttonParam);
            else addTextButton(buttonParam);
            section.addView(buttonMap.get(buttonParam.label));
        }
        this.addView(section);
    }
}
