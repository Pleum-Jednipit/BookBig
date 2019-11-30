package com.example.bookbig.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookbig.MainActivity;
import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginHomePageActivity extends AppCompatActivity {
    private Button mLogin;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_registration);
        mAuth = FirebaseAuth.getInstance();
        mLogin = (Button) findViewById(R.id.login);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginHomePageActivity.this, PhoneLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(LoginHomePageActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
