//package com.example.bookbig;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.example.bookbig.registration.UserProfileRegistrationActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class RegistrationActivity extends AppCompatActivity {
//    private static final String TAG = "EmailPassword";
//    private Button mSubmit;
//    private EditText mEmail;
//    private EditText mPassword;
//    private FirebaseAuth mAuth;
//    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activityregistration);
//
//        mAuth = FirebaseAuth.getInstance();
//        firebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                final FirebaseUser user = mAuth.getCurrentUser();
//                if(user != null){
//                    Log.v(TAG,"HELLO");
//                    Log.v(TAG, user.getEmail());
//                    Intent intent = new Intent(RegistrationActivity.this, UserProfileRegistrationActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//        };
//
//
//        mSubmit = (Button) findViewById(R.id.submit);
//        mEmail = (EditText) findViewById(R.id.email);
//        mPassword = (EditText) findViewById(R.id.password);
//
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
//                mAuth.createUserWithEmailAndPassword(email, password)
//                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    // Sign in success, update UI with the signed-in user's information
//                                    Log.d(TAG, "createUserWithEmail:success");
//                                    FirebaseUser user = mAuth.getCurrentUser();
//                                } else {
//                                    // If sign in fails, display a message to the user.
//                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                                    Toast.makeText(RegistrationActivity.this, "Authentication failed.",
//                                            Toast.LENGTH_SHORT).show();
//                                }
//
//                                // ...
//                            }
//                        });
//            }
//        });
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(firebaseAuthStateListener);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        mAuth.removeAuthStateListener(firebaseAuthStateListener);
//    }
//}
