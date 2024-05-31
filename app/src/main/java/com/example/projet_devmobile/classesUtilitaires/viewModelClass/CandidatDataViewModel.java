package com.example.projet_devmobile.classesUtilitaires.viewModelClass;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.projet_devmobile.classesUtilitaires.Candidature;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CandidatDataViewModel extends UserDataViewModel{
    private MutableLiveData<Double> latitude;
    private MutableLiveData<Double> longitude;

    public LiveData<Double> getLatitude() {
        if (latitude == null){
            latitude = new MutableLiveData<>();
            latitude.setValue(0.0);
        }
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.setValue(latitude);
    }

    public LiveData<Double> getLongitude() {
        if (longitude == null){
            longitude = new MutableLiveData<>();
            longitude.setValue(0.0);
        }
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.setValue(longitude);
    }

}
