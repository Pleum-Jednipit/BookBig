package com.example.bookbig.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Add Name to FireStore:";
    private Button mContinue;
    private EditText mName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_registration);

        final String phoneNumber = getIntent().getExtras().getString("phoneNumber");
        mName = (EditText) findViewById(R.id.name);
        mContinue = (Button) findViewById(R.id.next);
        mAuth = FirebaseAuth.getInstance();

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new user with a first and last name
                String name = mName.getText().toString();
                Intent intent = new Intent(UserProfileRegistrationActivity.this, AgeRegistrationActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                intent.putExtra("name",name);
                startActivity(intent);
                finish();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
