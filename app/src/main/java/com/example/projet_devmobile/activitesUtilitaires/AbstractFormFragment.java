package com.example.projet_devmobile.activitesUtilitaires;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractFormFragment extends Fragment {
    protected Map<String, ActivityResultLauncher<String>> fileSelectors = new HashMap<>();
    protected Map<String, ActivityResultCallback<Uri>> fileSelectorsCallbacks = new HashMap<>();

    public ActivityResultLauncher<String> createFunctionToButton(String label, ActivityResultCallback<Uri> buttonFunction) {
        fileSelectorsCallbacks.put(label, buttonFunction);
        fileSelectors.put(label,registerForActivityResult(new ActivityResultContracts.GetContent(),
                fileSelectorsCallbacks.get(label)));
        return fileSelectors.get(label);
    }
}
