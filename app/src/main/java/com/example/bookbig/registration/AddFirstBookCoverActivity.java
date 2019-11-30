package com.example.bookbig.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.MainActivity;
import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddFirstBookCoverActivity extends AppCompatActivity {
    private static final String TAG = "Add Book Cover :";
    private static final int PICK_IMAGE = 100;
    private Spinner mSpinner;
    private ImageView mImageView;
    private Button mDone, mAddPhoto;
    private EditText mName, mDescription;
    private Uri imageUri;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirestoreOperation firestoreOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_bookcover);
        firestoreOperation = new FirestoreOperation();
        mImageView = findViewById(R.id.image);
        mDone = findViewById(R.id.done);
        mAddPhoto = findViewById(R.id.addPhoto);
        mName = findViewById(R.id.name);
        mDescription = findViewById(R.id.description);

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


        final List<String> bookcoverType = new ArrayList<>();
        //Add book cover type
        bookcoverType.add("Romantice");
        bookcoverType.add("Sci fi");
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(AddFirstBookCoverActivity.this,android.R.layout.simple_spinner_item, bookcoverType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner = findViewById(R.id.type);
        mSpinner.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tutorialsName = parent.getItemAtPosition(position).toString();

                Log.d(TAG,tutorialsName);
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
                Log.d(TAG,"HIII");
            }
        });


        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
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
                firestoreOperation.setBookcover(name,description,imageUri,type);
                Intent intent = new Intent(AddFirstBookCoverActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
//        firestoreOperation.getBookcoverType(new FirestoreOperation.BookcoverTypeCallback() {
//            @Override
//            public void onCallback(List<BookcoverType> list) {
//        Log.d(TAG,list.toString());
//        List <String> arraylist = new ArrayList<>();
//        for(BookcoverType b : list){
//            arraylist.add(b.getName());
//        }
//            }
//        });
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
}