package com.example.projet_devmobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.classesUtilitaires.viewModelClass.CandidatDataViewModel;
import com.example.projet_devmobile.fragment.candidat.CandidatAnonymeMenuFragment;
import com.example.projet_devmobile.fragment.candidat.FilterFragment;
import com.example.projet_devmobile.layouts_utilitaires.NavigationLayout;

public class AnonymousCandidateActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private CandidatDataViewModel candidatDataViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Initialization of UserDataViewModel
        candidatDataViewModel = new ViewModelProvider(this).get(CandidatDataViewModel.class);
        candidatDataViewModel.getUserId();
        candidatDataViewModel.getLatitude();
        candidatDataViewModel.getLongitude();

        // Set values to UserDataViewModel fields without userID because Anonymous
        getLocalisation();

        // Initialization of the navigation bare
        NavigationLayout navigationBare = new NavigationLayout(this);
        navigationBare.setFunctionToAccountButton(NavigationLayout.ANONYMOUS,fragmentManager);
        navigationBare.setFunctionToSearchButton(v -> fragmentManager.beginTransaction()
                .replace(R.id.menuContent, FilterFragment.newInstanceAnonyme(FilterFragment.SEARCH, null))
                .commit());

        LinearLayout navigationLayout = findViewById(R.id.navagationLayout);
        navigationLayout.addView(navigationBare);

        // Lunch the menu for anonymous candidates
        fragmentManager.beginTransaction()
                .replace(R.id.menuContent, new CandidatAnonymeMenuFragment())
                .commit();
    }

    private void getLocalisation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) {
            // Demander la permission d'accès à la localisation si elle n'est pas déjà accordée
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            updateLocationTextView(lastKnownLocation);
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                updateLocationTextView(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {}

            @Override
            public void onProviderEnabled(@NonNull String provider) {}

            @Override
            public void onProviderDisabled(@NonNull String provider) {}
        });
    }
    private void updateLocationTextView(Location location) {
        candidatDataViewModel.setLatitude(location.getLatitude());
        candidatDataViewModel.setLongitude(location.getLongitude());
    }
}