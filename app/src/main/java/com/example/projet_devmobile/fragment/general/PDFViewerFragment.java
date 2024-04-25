package com.example.projet_devmobile.fragment.general;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.projet_devmobile.R;
import com.example.projet_devmobile.layouts_utilitaires.ButtonSetLayout;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class PDFViewerFragment extends Fragment {

    private static final String ARG_FILE_PATH = "file_path";
    private final FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");

    public static PDFViewerFragment newInstance(String filePath) {
        PDFViewerFragment fragment = new PDFViewerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_pdfviewer, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String filePath = getArguments().getString(ARG_FILE_PATH);

            PDFView pdfView = view.findViewById(R.id.pdfView);

            StorageReference cvref = storage.getReference().child(filePath);
            try {
                File finalFile = File.createTempFile("cvfile", "pdf");
                cvref.getFile(finalFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("TAG", "File cv telecharge " + finalFile.getName());
                        pdfView.fromFile(finalFile)
                                .defaultPage(0)
                                .enableSwipe(true)
                                .swipeHorizontal(false)
                                .enableAnnotationRendering(true)
                                .scrollHandle(new DefaultScrollHandle(PDFViewerFragment.this.getContext()))
                                .load();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.d("TAG", "File cv NON telecharge");
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(100,20,100,20);

            ButtonSetLayout singInButtons = new ButtonSetLayout(this.getContext(),"#FFFFFF");
            singInButtons.setLayoutParams(layoutParams2);
            ButtonSetLayout.ButtonParam back = new ButtonSetLayout.ButtonParam(R.drawable.back,"Back","#FFFFFF", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                }
            });

            singInButtons.addButtonsSection(new ButtonSetLayout.ButtonParam[]{back});
            LinearLayout buttonsSection = view.findViewById(R.id.buttonsSection);
            buttonsSection.addView(singInButtons);

        }
    }
}