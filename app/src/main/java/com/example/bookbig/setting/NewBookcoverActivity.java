package com.example.bookbig.setting;

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
import java.util.Arrays;
import java.util.List;

public class NewBookcoverActivity extends AppCompatActivity {
    private static final String TAG = "Add Book Cover :";
    private static final int PICK_IMAGE = 100;
    private Spinner mSpinner;
    private ImageView mImageView;
    private Button mDone, mAddPhoto;
    private EditText mName, mDescription,mBookcoverId;
    private Uri imageUri;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreOperation firestoreOperation;
    private String bookcoverId = "None";
    final List<String> bookcoverType = Arrays.asList
            ("Adventure","Sci fi","Romantic","Fantasy","Mystery","Horror","Comedy","Historical");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bookcover);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int weight = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(weight*.8),(int)(height*.6));
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x=0;
        params.y=-20;
        params.dimAmount = 0.3f;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        getWindow().setAttributes(params);



        firestoreOperation = new FirestoreOperation();
        mImageView = findViewById(R.id.image);
        mDone = findViewById(R.id.done);
        mAddPhoto = findViewById(R.id.addPhoto);
        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);

        try{
            bookcoverId = getIntent().getExtras().getString("bookcoverId");
        } catch (NullPointerException e){
            System.out.println(e);
        }

        if(!bookcoverId.equals("None")){
            Log.d(TAG,bookcoverId);
            getBookCover();
        }


        mName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mName.getWindowToken(), 0);
                }
                return false;
            }
        });

        mDescription.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mDescription.getWindowToken(), 0);
                }
                return false;
            }
        });

        mDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        //Add book cover type

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(NewBookcoverActivity.this,android.R.layout.simple_spinner_item, bookcoverType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner = findViewById(R.id.type);
        mSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Log.d(TAG,"HIII");
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mSpinner.setSelection(0);

        mAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        mDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = mName.getText().toString();
                final String description = mDescription.getText().toString();
                final String type = mSpinner.getSelectedItem().toString();
                if(!bookcoverId.equals("None")){
                    firestoreOperation.updateBookcover(name,description,imageUri,type,bookcoverId);
                }else {
                    firestoreOperation.setBookcover(name,description,imageUri,type);
                }
                Intent intent = new Intent(NewBookcoverActivity.this, BookcoverActivity.class);
                startActivity(intent);
//                finish();
            }
        });
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            mImageView.setImageURI(imageUri);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

//    private interface BookcoverCallback{
//        void onCallBack(Bookcover bookcover);
//    }

//    public void getBookCover(final BookcoverCallback bookcoverCallback) {
//        firestoreOperation.getBookcoverRef()
//                .whereEqualTo("bookcoverId",bookcoverId)
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot value,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e != null) {
//                            Log.w(TAG, "Listen failed.", e);
//                            return;
//                        }
//                        for (QueryDocumentSnapshot document : value) {
//                            if (document.exists()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                                String bookcoverId = document.getData().get("bookcoverId").toString();
//                                String name = document.getData().get("name").toString();
//                                String description = document.getData().get("description").toString();
//                                String userId = document.getData().get("userId").toString();
//                                String photoUrl = document.getData().get("photoUrl").toString();
//                                Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId);
//                            }
//                        }
//
//
//                    }
//                });
//    }

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
                                    String type = document.getData().get("bookcoverType").toString();
                                    Bookcover bookcover = new Bookcover(name, description, photoUrl, userId, bookcoverId,type);
                                    mName.setText(bookcover.getName());
                                    mDescription.setText(bookcover.getDescription());
                                    Glide.with(getBaseContext()).load(bookcover.getPhotoUrl()).into(mImageView);
                                    int index = bookcoverType.indexOf(type);
                                    mSpinner.setSelection(index);

                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
}
