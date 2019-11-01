package com.example.bookbig.login;

import androidx.appcompat.app.AppCompatActivity;
import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.bookbig.FirestoreOperation;
import com.example.bookbig.MainActivity;
import com.example.bookbig.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;

public class PhoneLoginActivity extends AppCompatActivity {
//    private Spinner spinnerDialog;
    private SpinnerDialog spinnerDialog;
    private Button mCountryCode;
    private EditText mEditText;
    private Button mSend;
    private FirebaseAuth mAuth;
    private FirestoreOperation firestoreOperation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);
        final ArrayList<String> countriesName = new ArrayList(Arrays.asList(CountryPhoneCode.countryNames));
        final ArrayList<String> countriesCode = new ArrayList(Arrays.asList(CountryPhoneCode.countryAreaCodes));
        mAuth = FirebaseAuth.getInstance();
        mCountryCode = findViewById(R.id.countryCode);
//        spinnerDialog = findViewById(R.id.countriesSpinner);
        spinnerDialog = new SpinnerDialog(PhoneLoginActivity.this,countriesName,"Countries Code",R.style.DialogAnimations_SmileWindow,"Close");
        spinnerDialog.setCancellable(true);
        spinnerDialog.setShowKeyboard(false);
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                mCountryCode.setText(item);
            }
        });
        mCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerDialog.showSpinerDialog();
            }
        });
//        spinnerDialog.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, CountryPhoneCode.countryNames));
        mSend = findViewById(R.id.send);
        mEditText = findViewById(R.id.phoneNumber);
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                }
                return false;
            }
        });
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = countriesName.indexOf(mCountryCode.getText().toString());
                String phoneCode = countriesCode.get(index);
                String phoneNumber = mEditText.getText().toString().trim();
                if(phoneNumber.isEmpty()){
                    mEditText.setError("Please enter your valide phone number");
                    mEditText.requestFocus();
                    finish();
                } else if(phoneNumber.length() < 6 || phoneCode.length() > 13){
                    mEditText.setError("Please enter your valid phone number");
                    mEditText.requestFocus();
                    finish();
                }

                String phoneNumberWithCode = "+" + phoneCode + phoneNumber;
                Log.d("Phone",phoneNumberWithCode);
                Intent intent = new Intent(PhoneLoginActivity.this, VerifyPhoneActivity.class);
                intent.putExtra("phoneNumber",phoneNumberWithCode);
                startActivity(intent);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(PhoneLoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
