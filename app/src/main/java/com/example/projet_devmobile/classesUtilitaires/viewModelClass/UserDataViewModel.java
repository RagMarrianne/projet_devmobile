package com.example.projet_devmobile.classesUtilitaires.viewModelClass;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserDataViewModel extends ViewModel {

    private MutableLiveData<String> userId;
    private MutableLiveData<String> password;

    public LiveData<String> getUserId() {
        if (userId == null){
            userId = new MutableLiveData<>();
            userId.setValue("");
        }
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.setValue(userId);
    }

    public LiveData<String> getPassword() {
        if (password == null){
            password = new MutableLiveData<>();
            password.setValue("");
        }
        return password;
    }
    public void setPassword(String password) {
        this.password.setValue(password);
    }

}