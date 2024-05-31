package com.example.projet_devmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.LinearLayout;
import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.UserDataViewModel;
import com.example.projet_devmobile.fragment.employeur.EmployeurMenuFragment;
import com.example.projet_devmobile.layouts_utilitaires.NavigationLayout;

public class EmployeurActivity extends AppCompatActivity {
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private UserDataViewModel userDataViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialization of UserDataViewModel
        userDataViewModel = new ViewModelProvider(this).get(UserDataViewModel.class);
        userDataViewModel.getUserId();

        // Set values to UserDataViewModel fields
        userDataViewModel.setUserId(getIntent().getStringExtra("identifiant"));

        // Initialization of the navigation bare
        NavigationLayout navigationBare = new NavigationLayout(this);
        navigationBare.setFunctionToAccountButton(NavigationLayout.EMPLOYER,fragmentManager);
        navigationBare.setSearchButtonInvisible();

        LinearLayout navigationLayout = findViewById(R.id.navagationLayout);
        navigationLayout.addView(navigationBare);

        // Lunch the menu for employer
        fragmentManager.beginTransaction()
                .replace(R.id.menuContent, new EmployeurMenuFragment())
                .commit();
    }

}