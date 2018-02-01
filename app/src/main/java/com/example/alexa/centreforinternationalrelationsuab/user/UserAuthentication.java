package com.example.alexa.centreforinternationalrelationsuab.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.alexa.centreforinternationalrelationsuab.R;

public class UserAuthentication extends AppCompatActivity {

    private EditText txtEmailLogin;
    private EditText txtPwd;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);

        txtEmailLogin = findViewById(R.id.email);
        txtPwd = findViewById(R.id.password);
        firebaseAuth = FirebaseAuth.getInstance();



    }

    public void btnUserLogin_Click(View v) {
        if (TextUtils.isEmpty(txtEmailLogin.getText())){
            Toast.makeText(UserAuthentication.this, "Please complete all fields", Toast.LENGTH_LONG).show();
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
                                    Toast.makeText(UserAuthentication.this, "Login successful", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(UserAuthentication.this, MainActivity.class);
                                    //noinspection ConstantConditions
                                    i.putExtra("Email", firebaseAuth.getCurrentUser().getEmail());
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(i);
                                }else {
                                    user.sendEmailVerification();
                                    Toast.makeText(UserAuthentication.this, "Your account email is not confirmed. Please check your email inbox!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                //noinspection ConstantConditions
                                Log.e("ERROR", task.getException().toString());
                                Toast.makeText(UserAuthentication.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });
        }

    }

    public void btnForgotPassword_Click(View view) {

        Intent intent = new Intent(this, UserForgotPassword.class);
        startActivity(intent);

    }
}
