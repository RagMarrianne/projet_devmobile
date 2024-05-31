package com.example.projet_devmobile.layouts_utilitaires;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.ScreenUtils;
import com.example.projet_devmobile.fragment.commun.ProfilSettingFragment;

public class NavigationLayout extends ConstraintLayout {

    private ImageButton accountButton = new ImageButton(this.getContext());
    private ImageButton searchButton = new ImageButton((this.getContext()));
    private final GradientDrawable shape = new GradientDrawable();
    public static final int CANDIDATE = 0;
    public static final int EMPLOYER = 1;
    public static final int ANONYMOUS = 2;


    public NavigationLayout(@NonNull Context context) {
        super(context);

        int screenWidth = ScreenUtils.getScreenWidth(this.getContext());

        // Define the design of our layout
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable.setCornerRadius(100);
        gradientDrawable.setColor(Color.parseColor("#5CE98C"));

        // Application of out design to the layout
        this.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));
        this.setBackground(gradientDrawable);
        this.setId(View.generateViewId());

        // Define the shape our button
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor("#5CE98C"));

        initAccountButton(screenWidth);
        this.addView(accountButton);

        initSearchButton(screenWidth);
        this.addView(searchButton);

        // Dispose the different buttons to our layout
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        constraintSet.connect(accountButton.getId(),ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 10);
        constraintSet.connect(searchButton.getId(),ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 10);
        constraintSet.applyTo(this);

    }

    // Init the design of the search button
    private void initSearchButton(int dimension) {
        searchButton.setLayoutParams(new ConstraintLayout.LayoutParams((int) (dimension * 0.2), LayoutParams.MATCH_PARENT));
        searchButton.setBackground(shape);
        searchButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        searchButton.setId(View.generateViewId());
        searchButton.setImageResource(R.drawable.search);
    }

    // Set function to the search button
    public void setFunctionToSearchButton(OnClickListener buttonFunction){
        this.searchButton.setOnClickListener(buttonFunction);
    }

    // Init the design of the account button
    private void initAccountButton(int dimension){
        accountButton.setLayoutParams(new ConstraintLayout.LayoutParams((int) (dimension * 0.2), LayoutParams.MATCH_PARENT));
        accountButton.setBackground(shape);
        accountButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
        accountButton.setId(View.generateViewId());
        accountButton.setImageResource(R.drawable.account);
    }

    // Init the function of the account button depending to the type of user
    public void setFunctionToAccountButton(int fragmentCase, FragmentManager fragmentManager){
        this.accountButton.setOnClickListener(v -> {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            Fragment fragment = null;
            switch (fragmentCase){
                case ANONYMOUS:
                    fragment = ProfilSettingFragment.newInstance(ANONYMOUS);
                    transaction.replace(R.id.mainMenu, fragment);
                    transaction.commit();
                    break;
                case EMPLOYER:
                    fragment = ProfilSettingFragment.newInstance(EMPLOYER);
                    transaction.replace(R.id.mainMenu, fragment);
                    transaction.commit();
                    break;
                case CANDIDATE:
                    fragment = ProfilSettingFragment.newInstance(CANDIDATE);
                    transaction.replace(R.id.mainMenu, fragment);
                    transaction.commit();
                    break;
                default: break;
            }
        });
    }

    public void setSearchButtonVisible(){
        searchButton.setVisibility(View.VISIBLE);
    }
    public void setSearchButtonInvisible(){
        searchButton.setVisibility(View.INVISIBLE);
    }
}
