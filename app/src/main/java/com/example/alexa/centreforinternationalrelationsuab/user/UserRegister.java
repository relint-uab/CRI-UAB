package com.example.alexa.centreforinternationalrelationsuab.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import com.muddzdev.styleabletoastlibrary.StyleableToast;

public class UserRegister extends AppCompatActivity {

    private EditText userMailInput;
    private EditText userPasswordInput;
    private EditText userConfirmationPasswordInput;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        //Back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        userMailInput = findViewById(R.id.email);
        userPasswordInput = findViewById(R.id.password);
        userConfirmationPasswordInput = findViewById(R.id.repassword);
        firebaseAuth = FirebaseAuth.getInstance();


        Button createAccount = findViewById(R.id.email_registration_button);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideKeyboard();

                if (TextUtils.isEmpty(userMailInput.getText()) || !Patterns.EMAIL_ADDRESS.matcher(userMailInput.getText()).matches()){
                    StyleableToast.makeText(getApplicationContext(), "Try again! \n Please enter a valid email address!", R.style.errorToast).show();
                    //Toast.makeText(getApplicationContext(), "Try again! \n Please enter a valid email address!", Toast.LENGTH_SHORT).show();
                }else {
                    if (TextUtils.isEmpty(userPasswordInput.getText())){
                        StyleableToast.makeText(getApplicationContext(), "Try again! \n Password field can not be empty", R.style.errorToast).show();
                        //Toast.makeText(getApplicationContext(), "Try again! \n Password field can not be empty", Toast.LENGTH_SHORT).show();
                    } else
                        if (userPasswordInput.length()<8){
                            StyleableToast.makeText(getApplicationContext(), "Try again! \n Password must have minimum 8 characters", R.style.errorToast).show();
                            //Toast.makeText(getApplicationContext(), "Try again! \n Password must have minimum 8 characters.", Toast.LENGTH_SHORT).show();
                        } else
                            if (userConfirmationPasswordInput.length() == 0) {
                                StyleableToast.makeText(getApplicationContext(), "Please confirm password", R.style.errorToast).show();
                            } else
                                if (userPasswordInput.getText().toString().equals(userConfirmationPasswordInput.getText().toString())){
                                    sendRequest(userMailInput.getText().toString(),
                                            userPasswordInput.getText().toString());
                                } else {
                                    StyleableToast.makeText(getApplicationContext(), "Passwords don't match \n Try again!", R.style.errorToast).show();
                                    //Toast.makeText(getApplicationContext(), "Passwords don't match \n Try again!", Toast.LENGTH_SHORT).show();
                                }
                }
            }
        });


    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void sendRequest(final String mail, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(UserRegister.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(mail, password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            progressDialog.dismiss();
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                StyleableToast.makeText(getApplicationContext(), task.getException().getMessage(), R.style.errorToast).show();
                               // Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            //Create User profile in database

                            mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                            assert mCurrentUser != null;
                            String mCurrentUserUid = mCurrentUser.getUid();
                            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                            Map<String, String> userData = new HashMap<String, String>();

                            userData.put("Account_type", "Undefined");
                            userData.put("Firstname", "User");
                            userData.put("Lastname", "add lastname");
                            userData.put("Gender", "add");
                            userData.put("Date_of_birth", "add");
                            userData.put("Country", "add");
                            userData.put("University", "add");
                            userData.put("Image_Profile_URL", "https://firebasestorage.googleapis.com/v0/b/cir-uab.appspot.com/o/User_Profile%2Fuab_logo_white.png?alt=media&token=55231866-7bea-4fcd-bcfa-cbb1bdfc2003");

                            mUserDatabase = mUserDatabase.child(mCurrentUserUid);
                            mUserDatabase.setValue(userData);

                            progressDialog.dismiss();

                            StyleableToast.makeText(getApplicationContext(), "Registration successful.\n Please confirm your email before login", R.style.successToast).show();
                            // Toast.makeText(UserRegister.this, "Registration successful. Please confirm your email before login", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(UserRegister.this, UserAuthentication.class);
//                            startActivity(i);
//                            finish();
                            mCurrentUser.sendEmailVerification();
                            Intent i = new Intent(getApplicationContext(), UserAuthentication.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }





//                        if (task.isSuccessful()) {
//
//                        }
//                        else
//                        {
//                            progressDialog.dismiss();
//                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(getApplicationContext(), "User with this email already exist.", Toast.LENGTH_SHORT).show();
//                            }
//                            Log.e("ERROR", task.getException().toString());
//                            //noinspection ConstantConditions
//                            StyleableToast.makeText(getApplicationContext(), task.getException().getMessage(), R.style.errorToast).show();
//                            //Toast.makeText(UserRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
//                        }
                    }
                });

    }


    //Back button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
