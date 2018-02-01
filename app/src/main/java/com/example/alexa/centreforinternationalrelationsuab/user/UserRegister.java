package com.example.alexa.centreforinternationalrelationsuab.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

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
                if (TextUtils.isEmpty(userMailInput.getText())){
                    Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }else {
                    if (userPasswordInput.getText().toString().equals(userConfirmationPasswordInput.getText().toString())){
                        sendRequest(userMailInput.getText().toString(),
                                userPasswordInput.getText().toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Passwords don't match!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void sendRequest(final String mail, final String password) {
        final ProgressDialog progressDialog = ProgressDialog.show(UserRegister.this, "Please wait...", "Processing...", true);
        (firebaseAuth.createUserWithEmailAndPassword(mail, password))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //Create User profile in database

                        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
                        assert mCurrentUser != null;
                        String mCurrentUserUid = mCurrentUser.getUid();
                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

                        Map<String, String> userData = new HashMap<>();

                        userData.put("Firstname", "User");
                        userData.put("Lastname", "add last name");
                        userData.put("Gender", "add");
                        userData.put("Date_of_birth", "add");
                        userData.put("Country", "add");
                        userData.put("University", "add");
                        userData.put("Image_Profile_URL", "https://firebasestorage.googleapis.com/v0/b/fir-auth-5396b.appspot.com/o/User_Profile%2Flogo.png?alt=media&token=2b50979f-2289-4ea9-b7d6-1d6d08f7601b");

                        mUserDatabase = mUserDatabase.child(mCurrentUserUid);
                        mUserDatabase.setValue(userData);

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            Toast.makeText(UserRegister.this, "Registration successful. Please confirm your email before login", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(UserRegister.this, UserAuthentication.class);
                            startActivity(i);
                            finish();
                            Intent moveToHome = new Intent(UserRegister.this, UserAuthentication.class);
                            moveToHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(moveToHome);
                        }
                        else
                        {
                            //noinspection ConstantConditions
                            Log.e("ERROR", task.getException().toString());
                            Toast.makeText(UserRegister.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
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
