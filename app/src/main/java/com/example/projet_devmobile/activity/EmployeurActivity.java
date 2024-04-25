package com.example.projet_devmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.example.projet_devmobile.R;
import com.example.projet_devmobile.fragment.employeur.EmployeurMenuFragment;
import com.example.projet_devmobile.fragment.employeur.NouvelleOffreFragment;
import com.example.projet_devmobile.layouts_utilitaires.NavigationLayout;

import java.util.HashMap;
import java.util.Map;

public class EmployeurActivity extends AppCompatActivity {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        NavigationLayout navigationBare = new NavigationLayout(this);
        navigationBare.setFunctionToAccountButton(NavigationLayout.EMPLOYEUR,fragmentManager);
        navigationBare.setSearchButtonInvisible();

        LinearLayout navigationLayout = findViewById(R.id.navagationLayout);
        navigationLayout.addView(navigationBare);

        fragmentManager.beginTransaction()
                .replace(R.id.menuContent, EmployeurMenuFragment.newInstance(getIntent().getStringExtra("identifiant")))
                //.replace(R.id.menuContent, EmployeurMenuFragment.newInstance("crous"))
                .commit();
    }

}