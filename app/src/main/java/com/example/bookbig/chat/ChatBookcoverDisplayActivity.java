package com.example.bookbig.chat;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.R;
import com.example.bookbig.bookcover.Bookcover;
import com.example.bookbig.bookcover.BookcoverActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatBookcoverDisplayActivity extends AppCompatActivity {
    private static final String TAG = "Display Book Cover :";
    private ImageView mImageView;
    private TextView mName, mDescription,mType;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreOperation firestoreOperation;
    private String bookcoverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatbookcover);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int weight = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (weight * .8), (int) (height * .6));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        params.dimAmount = 0.3f;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);

        bookcoverId = getIntent().getExtras().getString("bookcoverId");
        firestoreOperation = new FirestoreOperation();
        mImageView = findViewById(R.id.image);
        mName = findViewById(R.id.bookName);
        mDescription = findViewById(R.id.description);
        mType = findViewById(R.id.type);

        getBookCover();


    }

    private void getBookCover() {
        firestoreOperation.getBookcoverRef()
                .whereEqualTo("bookcoverId",bookcoverId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                String bookcoverId = document.getData().get("bookcoverId").toString();
                                String name = document.getData().get("name").toString();
                                String description = document.getData().get("description").toString();
                                String userId = document.getData().get("userId").toString();
                                String photoUrl = document.getData().get("photoUrl").toString();
                                String bookcoverType = document.getData().get("bookcoverType").toString();
                                Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId,bookcoverType);
                                mName.setText(bookcover.getName());
                                mDescription.setText(bookcover.getDescription());
                                mType.setText(bookcover.getBookcoverType());
                                Glide.with(getBaseContext()).load(bookcover.getPhotoUrl()).into(mImageView);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }
}

