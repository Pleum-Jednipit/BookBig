package com.example.bookbig.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AgeRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Add Name to FireStore:";
    private Button mContinue;
    private EditText mAge;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_registration);

        final String phoneNumber = getIntent().getExtras().getString("phoneNumber");
        final String name = getIntent().getExtras().getString("name");
        mAge = (EditText) findViewById(R.id.age);
        mContinue = (Button) findViewById(R.id.next);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();


        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String age = mAge.getText().toString();
                Intent intent = new Intent(AgeRegistrationActivity.this, GenderRegistrationActivity.class);
                intent.putExtra("phoneNumber",phoneNumber);
                intent.putExtra("name",name);
                intent.putExtra("age",age);
                startActivity(intent);
                finish();
            }
        });
    }
}
