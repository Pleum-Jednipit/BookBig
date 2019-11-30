package com.example.bookbig.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookbig.MainActivity;
import com.example.bookbig.R;
import com.example.bookbig.registration.UserProfileRegistrationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private static final String TAG = "Verification Page : ";
    private String mVerificationId;
    private FirebaseAuth mAuth;
    private ProgressBar mProgressBar;
    private EditText mEnterCode;
    private TextView mPhoneNumber;
    private Button mResendCode, mLogin;
    private String phoneNumber;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        mProgressBar = findViewById(R.id.progressbar);
        mResendCode = findViewById(R.id.resendCode);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mLogin = findViewById(R.id.login);
        mEnterCode = findViewById(R.id.enterCode);
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        mPhoneNumber.setText(phoneNumber);
        Log.d("HEREEEERr",phoneNumber);
        sendVerificationCode(phoneNumber);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = mEnterCode.getText().toString().trim();

                if(code.isEmpty() || code.length() < 6){
                    mEnterCode.setError("Please enter valid code");
                    mEnterCode.requestFocus();
                    finish();
                }
                verifyPhoneNumberWithCode(code);

            }
        });


        mResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVerificationCode(phoneNumber);
            }
        });

        mEnterCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });


    }

    private void verifyPhoneNumberWithCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }


    private void sendVerificationCode(String phoneNumber){
        mProgressBar.setVisibility(View.VISIBLE);
        Log.d("Pleum",phoneNumber);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks;

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            db.collection("user_account")
                                    .document(mAuth.getUid())
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if(documentSnapshot.exists()){
                                                Log.d(TAG, "Already Register");
                                                Intent intent = new Intent(VerifyPhoneActivity.this, MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);

                                            } else{
                                                Log.d(TAG, "New User");
                                                Intent intent = new Intent(VerifyPhoneActivity.this, UserProfileRegistrationActivity.class);
                                                intent.putExtra("phoneNumber",phoneNumber);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
//                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:" + credential);
            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e);
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                // Invalid request
                // ...
                Log.d(TAG,"Invalid Request");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                // ...
                Log.d(TAG,"The SMS quota for the project has been exceeded");
            }

            // Show a message and update the UI
            // ...
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:" + verificationId);

            // Save verification ID and resending token so we can use them later
            mVerificationId = verificationId;


            // ...
        }
    };

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    }
