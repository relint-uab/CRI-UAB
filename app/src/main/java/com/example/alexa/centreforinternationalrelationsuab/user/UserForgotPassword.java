package com.example.alexa.centreforinternationalrelationsuab.user;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import dmax.dialog.SpotsDialog;

public class UserForgotPassword extends AppCompatActivity {

    private EditText inputEmail;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_forgot_password);

        inputEmail = findViewById(R.id.textInputEmail);
        Button btnReset = findViewById(R.id.btnResetPassword);
        progressBar = findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        //Back button
        if(getSupportActionBar()!= null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    StyleableToast.makeText(getApplicationContext(), "Enter your registered email address!", R.style.errorToast).show();
                    //Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    StyleableToast.makeText(getApplicationContext(), "Try again \n Wrong email address format!", R.style.errorToast).show();
                }

                progressBar.setVisibility(View.VISIBLE);

                auth.sendPasswordResetEmail(email)

                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    StyleableToast.makeText(getApplicationContext(), "We have sent you instructions to reset your password!", R.style.successToast).show();
                                    //Toast.makeText(UserForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();

                                } else {
                                    StyleableToast.makeText(getApplicationContext(), "Failed to send reset email!", R.style.errorToast).show();
                                    //Toast.makeText(UserForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                                }

                                progressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getApplicationContext(), UserAuthentication.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });

    }



    //Back button

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
