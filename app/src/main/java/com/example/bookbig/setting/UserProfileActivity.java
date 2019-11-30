package com.example.bookbig.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bumptech.glide.Glide;
import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.Profile;
import com.example.bookbig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserProfileActivity extends AppCompatActivity {
    private static final String TAG = "UserProfileActivity: ";
    private EditText mName,mAge;
    private RadioGroup mGender;
    private ImageView mImageView;
    private Button mSave;
    private RadioButton mRadioMale,mRadioFemale;
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


        Profile profile =  getIntent().getParcelableExtra("Profile");
        Log.d(TAG,profile.getName());
        firestoreOperation = new FirestoreOperation();
        mName = findViewById(R.id.name);
        mAge = findViewById(R.id.age);
        mSave = findViewById(R.id.save);
        mImageView = findViewById(R.id.profilePicture);
        mGender = findViewById(R.id.gender);
        mRadioMale = findViewById(R.id.male);
        mRadioFemale = findViewById(R.id.female);
        mName.setText(profile.getName());
        mAge.setText(profile.getAge());
        Glide.with(getBaseContext()).load(profile.getProfilePicture()).into(mImageView);
        if(profile.getGender().equals("Male")){
            mGender.check(mRadioMale.getId());
        } else {
            mGender.check(mRadioFemale.getId());
        }

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

        mName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        mAge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

