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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.alexa.centreforinternationalrelationsuab.MainActivity;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmus;
import com.example.alexa.centreforinternationalrelationsuab.MainErasmusAcademic;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.Objects;

public class UserAuthentication extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        txtEmailLogin = findViewById(R.id.email);
        txtPwd = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void btnUserLogin_Click(View v) {

        hideKeyboard();

        if (TextUtils.isEmpty(txtEmailLogin.getText())){
            StyleableToast.makeText(getApplicationContext(), "Please complete all fields", R.style.errorToast).show();
           // Toast.makeText(UserAuthentication.this, "Please complete all fields", Toast.LENGTH_LONG).show();
        } else
            if (!Patterns.EMAIL_ADDRESS.matcher(txtEmailLogin.getText()).matches()){
                StyleableToast.makeText(getApplicationContext(), "Try again! \n Please enter a valid email address!", R.style.errorToast).show();
            } else
                if (TextUtils.isEmpty(txtPwd.getText())){
                    StyleableToast.makeText(getApplicationContext(), "Please complete all fields", R.style.errorToast).show();
                } else {
                   final ProgressDialog progressDialog = ProgressDialog.show(UserAuthentication.this, "Please wait...", "Processing...", true);
                   (firebaseAuth.signInWithEmailAndPassword(txtEmailLogin.getText().toString(), txtPwd.getText().toString()))
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();

                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                //noinspection ConstantConditions
                                if (user.isEmailVerified()){
                                    String current_uid = user.getUid();
                                    mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Account_type");
                                    mUserDatabase.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String accountType = dataSnapshot.getValue().toString();


                                                if (Objects.equals(accountType, "Erasmus student")) {
                                                    Intent i = new Intent(getApplicationContext(), MainErasmus.class);
                                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    finish();
                                                } else if (Objects.equals(accountType, "Erasmus academic")) {
                                                    Intent i = new Intent(getApplicationContext(), MainErasmusAcademic.class);
                                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    finish();
                                                } else if (Objects.equals(accountType, "International student")) {
                                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Intent i = new Intent(getApplicationContext(), SelectAccountType.class);
                                                    i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(i);
                                                    finish();
                                                }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                                    StyleableToast.makeText(getApplicationContext(),"Login successful" , R.style.successToast).show();
//                                    //Toast.makeText(UserAuthentication.this, "Login successful", Toast.LENGTH_LONG).show();
//                                    Intent i = new Intent(UserAuthentication.this, UserWelcome.class);
//                                    //noinspection ConstantConditions
//                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
//                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(i);
                                }else {
                                    user.sendEmailVerification();
                                    StyleableToast.makeText(getApplicationContext(), "Your account email is not confirmed. Please check your email inbox!", R.style.warningToast).show();
                                    //Toast.makeText(UserAuthentication.this, "Your account email is not confirmed. Please check your email inbox!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                //noinspection ConstantConditions
                                Log.e("ERROR", task.getException().toString());
                                //Toast.makeText(UserAuthentication.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                StyleableToast.makeText(getApplicationContext(), task.getException().getMessage(), R.style.errorToast).show();

                            }
                        }
                    });
        }

    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void btnForgotPassword_Click(View view) {

        Intent intent = new Intent(this, UserForgotPassword.class);
        startActivity(intent);

    }
}
