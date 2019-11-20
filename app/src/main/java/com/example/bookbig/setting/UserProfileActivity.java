package com.example.bookbig.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity: ";
    private EditText mName,mAge;
    private RadioGroup mGender;
    private Button mSave;
    private FirestoreOperation firestoreOperation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mSave = findViewById(R.id.save);
        mGender = findViewById(R.id.gender);
        firestoreOperation.getCurrentUserAccountRef()
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                String name = document.getData().get("name").toString();
                                String age = document.getData().get("age").toString();
                                String gender = document.getData().get("gender").toString();
                                String phoneNumber = document.getData().get("phoneNumber").toString();

                                mName.setText(name);
                                mAge.setText(age);

                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getText().toString();
                String age = mAge.getText().toString();
                int selectedId = mGender.getCheckedRadioButtonId();
                final RadioButton gender =  findViewById(selectedId);

                firestoreOperation.updateUserAccount(name,age,gender.getText().toString());
                Intent intent = new Intent(UserProfileActivity.this, UserInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

