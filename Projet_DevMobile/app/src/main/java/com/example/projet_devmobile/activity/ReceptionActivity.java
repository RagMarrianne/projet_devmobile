package com.example.projet_devmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.fragment.general.SignInFragment;
import com.example.projet_devmobile.fragment.general.LogInFragment;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;

public class ReceptionActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int SIGN_IN_CANDIDAT = 1;
    private static final int SIGN_IN_EMPLOYEUR = 2;
    private static final int LOG_IN = 3;
    private static final int LOG_IN_GUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        LinearLayout signInLayout = findViewById(R.id.signInSection);
        LinearLayout logInLayout = findViewById(R.id.logSection);

        ButtonSetLayout signInSection = new ButtonSetLayout(this,"#FFFFFF");
        ButtonSetLayout.ButtonParam candidatButton = new ButtonSetLayout.ButtonParam("Candidat","#FFFFFF", new View.OnClickListener() { //TODO
            @Override
            public void onClick(View v) {
                changerFragment(SIGN_IN_CANDIDAT);
            }
        });
        ButtonSetLayout.ButtonParam employeurButton = new ButtonSetLayout.ButtonParam("Employeur","#FFFFFF", new View.OnClickListener() { //TODO
            @Override
            public void onClick(View v) {
                changerFragment(SIGN_IN_EMPLOYEUR);
            }
        });
        ButtonSetLayout.ButtonParam[] signInButtonSection = {candidatButton,employeurButton};
        signInSection.addButtonsSection(signInButtonSection);
        signInLayout.addView(signInSection);


        ButtonSetLayout logInSection = new ButtonSetLayout(this,"#5CE98C");
        ButtonSetLayout.ButtonParam guestButton = new ButtonSetLayout.ButtonParam("Invité","#43B96B", new View.OnClickListener() { //TODO
            @Override
            public void onClick(View v) {
                changerFragment(LOG_IN_GUEST);            }
        });
        ButtonSetLayout.ButtonParam logInButton = new ButtonSetLayout.ButtonParam("Log In","#43B96B", new View.OnClickListener() { //TODO
            @Override
            public void onClick(View v) {
                changerFragment(LOG_IN);
            }
        });
        ButtonSetLayout.ButtonParam[] logInButtonSection = {guestButton};
        ButtonSetLayout.ButtonParam[] logInButtonSection2 = {logInButton};
        logInSection.addButtonsSection(logInButtonSection);
        logInSection.addButtonsSection(logInButtonSection2);
        logInLayout.addView(logInSection);
    }

    public void changerFragment(int numFragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        switch (numFragment){
            case SIGN_IN_CANDIDAT:
                fragment = SignInFragment.newInstance(0);
                transaction.replace(R.id.mainLayout, fragment);
                transaction.commit();
                break;

            case SIGN_IN_EMPLOYEUR:
                fragment = SignInFragment.newInstance(1);
                transaction.replace(R.id.mainLayout, fragment);
                transaction.commit();
                break;
            case LOG_IN_GUEST:
                //TODO
                break;
            case LOG_IN:
                fragment = new LogInFragment();
                transaction.replace(R.id.mainLayout, fragment);
                transaction.commit();
                break;
            default: break;
        }
    }
}