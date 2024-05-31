package com.example.projet_devmobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.fragment.commun.SignInFragment;
import com.example.projet_devmobile.fragment.commun.LogInFragment;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;

public class ReceptionActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private static final int SIGN_IN_CANDIDATE = 1;
    private static final int SIGN_IN_EMPLOYER = 2;
    private static final int LOG_IN = 3;
    private static final int LOG_IN_GUEST = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reception);

        LinearLayout signInLayout = findViewById(R.id.signInSection);
        LinearLayout logInLayout = findViewById(R.id.logSection);

        // Add Sign In option as Candidate or Employer
        ButtonSetLayout signInSection = new ButtonSetLayout(this,"#FFFFFF");
        ButtonSetLayout.ButtonParam candidateButton = new ButtonSetLayout.ButtonParam("Candidat","#FFFFFF", v -> changerFragment(SIGN_IN_CANDIDATE));
        ButtonSetLayout.ButtonParam employerButton = new ButtonSetLayout.ButtonParam("Employeur","#FFFFFF", v -> changerFragment(SIGN_IN_EMPLOYER));
        signInSection.addButtonsSection(new ButtonSetLayout.ButtonParam[]{candidateButton,employerButton});
        signInLayout.addView(signInSection);

        // Add Log In option as Guest or Registered User
        ButtonSetLayout logInSection = new ButtonSetLayout(this,"#5CE98C");
        ButtonSetLayout.ButtonParam guestButton = new ButtonSetLayout.ButtonParam("Invité","#43B96B", v -> changerFragment(LOG_IN_GUEST));
        ButtonSetLayout.ButtonParam logInButton = new ButtonSetLayout.ButtonParam("Log In","#43B96B", v -> changerFragment(LOG_IN));
        logInSection.addButtonsSection(new ButtonSetLayout.ButtonParam[]{guestButton});
        logInSection.addButtonsSection(new ButtonSetLayout.ButtonParam[]{logInButton});
        logInLayout.addView(logInSection);
    }

    public void changerFragment(int numFragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment fragment = null;

        switch (numFragment){
            case SIGN_IN_CANDIDATE:
                fragment = SignInFragment.newInstance(0,false);
                transaction.replace(R.id.mainLayout, fragment);
                transaction.commit();
                break;

            case SIGN_IN_EMPLOYER:
                fragment = SignInFragment.newInstance(1,false);
                transaction.replace(R.id.mainLayout, fragment);
                transaction.commit();
                break;
            case LOG_IN_GUEST:
                finish();
                Intent intent = new Intent(this, AnonymousCandidateActivity.class);
                startActivity(intent);
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