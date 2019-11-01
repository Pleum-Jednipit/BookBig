package com.example.bookbig.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.Profile;
import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class GenderRegistrationActivity extends AppCompatActivity {
    private static final String TAG = "Add Name to FireStore:";
    private Button mContinue;
    private RadioGroup mRadioGroup;
    private FirestoreOperation firestoreOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gender_registration);

        firestoreOperation = new FirestoreOperation();

        final String phoneNumber = getIntent().getExtras().getString("phoneNumber");
        final String name = getIntent().getExtras().getString("name");
        final String age = getIntent().getExtras().getString("age");

        mRadioGroup = (RadioGroup) findViewById(R.id.gender);
        mContinue = (Button) findViewById(R.id.next);


        mContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new user with a first and last name
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                final RadioButton genderRadioButton =  findViewById(selectedId);
                String gender = genderRadioButton.getText().toString();

                Profile profile = new Profile(name,age,gender,firestoreOperation.getCurrentUserId(),phoneNumber,20);
                firestoreOperation.setUserAccount(profile);
                Intent intent = new Intent(GenderRegistrationActivity.this, AddFirstBookCoverActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
