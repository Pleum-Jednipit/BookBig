package com.example.bookbig.setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.MainActivity;
import com.example.bookbig.R;
import com.example.bookbig.bookcover.BookcoverActivity;
import com.example.bookbig.login.LoginHomePageActivity;
import com.example.bookbig.login.PhoneLoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserInfoActivity extends AppCompatActivity {
    private static final String TAG = "UserInfo: ";
    private Button mSetting,mProfile,mSwipe,mLogout,mGallery;
    private FirestoreOperation firestoreOperation;
    private Dialog dialog;
    private TextView distanceLabel,mName;
    private ImageView mImageView;
    private float distance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        firestoreOperation = new FirestoreOperation();
        mSetting = findViewById(R.id.setting);
        mProfile = findViewById(R.id.profile);
        mGallery = findViewById(R.id.gallery);
        mImageView = findViewById(R.id.profilePicture);
        mName = findViewById(R.id.userName);
        mSwipe = findViewById(R.id.swipe);
        mLogout = findViewById(R.id.userLogout);
        dialog = new Dialog(this);
        getUserAccount();
        mSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(UserInfoActivity.this, SettingActivity.class);
//                startActivity(intent);
//                finish();
                dialog.setContentView(R.layout.activity_distance_setting);
                firestoreOperation.getCurrentUserAccountRef()
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        Button mCancel;
                                        Button mUpdate;
                                        distanceLabel = dialog.findViewById(R.id.distance);
                                        distance = (float) document.getLong("maxDistance");
                                        distanceLabel.setText("Maximum Distance: " + distance );
                                        mUpdate = dialog.findViewById(R.id.update);
                                        mCancel = dialog.findViewById(R.id.cancel);
                                        final SeekBar seekBar = dialog.findViewById(R.id.seekBar);
                                        seekBar.setProgress((int) distance);
                                        seekBar.setOnSeekBarChangeListener(seekBarChangeListener);

                                        mCancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.dismiss();
                                            }
                                        });

                                        mUpdate.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                int maxDistance = seekBar.getProgress();
                                                firestoreOperation.updateUserMaxDistance(maxDistance);
                                                distance = maxDistance;
                                                dialog.dismiss();
                                            }
                                        });
                                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                        dialog.show();
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });

            }
        });

        mProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, UserProfileActivity.class);
                startActivity(intent);
                //delete finish()
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, BookcoverActivity.class);
                startActivity(intent);
                //delete finish()
            }
        });


        mSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.slideinright, R.anim.slideoutleft);
            }
        });

        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreOperation.logOut();
                Intent intent = new Intent(UserInfoActivity.this, LoginHomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // updated continuously as the user slides the thumb
            distanceLabel.setText("Maximum Distance: " + progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // called when the user first touches the SeekBar
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // called after the user finishes moving the SeekBar
        }
    };

    private void getUserAccount(){
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
                                String profilePic = document.getData().get("profilePicture").toString();
                                mName.setText(name);
                                Glide.with(getBaseContext()).load(profilePic).into(mImageView);
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

}
