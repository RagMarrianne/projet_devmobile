package com.example.projet_devmobile.activity;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.projet_devmobile.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    private FirebaseStorage storage = FirebaseStorage.getInstance("gs://devmobile-9cf53.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pdfviewer);
        PDFView pdfView = findViewById(R.id.pdfView);


        StorageReference test = storage.getReference();
        test.child("TEST").child("placeholder").putBytes(new byte[]{});
        test.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        for (StorageReference prefix : listResult.getPrefixes()) {
                            // All the prefixes under listRef.
                            // You may call listAll() recursively on them.
                            Log.d(TAG,"======================="+prefix.getName());

                        }

                        for (StorageReference item : listResult.getItems()) {
                            // All the items under listRef.
                            Log.d(TAG,"======================="+item.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
        Log.d(TAG,"=======================");

        /*StorageReference cvref = storage.getReference().child("Candidat/ragmarrianne/cv/CV_ANTOINETTE-LAURENT.pdf");

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
                            .scrollHandle(new DefaultScrollHandle(TestActivity.this))
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
        // Chemin du fichier PDF

        // Chargez le fichier PDF*/

    }
}