package com.example.alexa.centreforinternationalrelationsuab.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alexa.centreforinternationalrelationsuab.MainActivity;
import com.example.alexa.centreforinternationalrelationsuab.R;
import com.example.alexa.centreforinternationalrelationsuab.TermsOfUse;
import com.google.firebase.auth.FirebaseAuth;

public class UserWelcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        // Check if user is already logged-in or not and redirect to news page
        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified())
        {
            finish();
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        Button sendToAuthenticate = findViewById(R.id.btnAuthenticate);
        sendToAuthenticate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this,UserAuthentication.class);
                startActivity(intent);
            }
        });

        Button sendToRegister = findViewById(R.id.btnRegister);
        sendToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this,UserRegister.class);
                startActivity(intent);
            }
        });

        TextView terms_of_use = findViewById(R.id.welcome_terms_of_use);
        terms_of_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserWelcome.this,TermsOfUse.class);
                startActivity(intent);
            }
        });
    }
}
