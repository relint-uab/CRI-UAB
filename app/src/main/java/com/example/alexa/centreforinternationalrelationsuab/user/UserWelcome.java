package com.example.alexa.centreforinternationalrelationsuab.user;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.example.alexa.centreforinternationalrelationsuab.TermsOfUse;
import com.google.firebase.database.DatabaseReference;

public class UserWelcome extends AppCompatActivity {

    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_welcome);

        ConstraintLayout splash_layout = findViewById(R.id.user_welcome);
        AnimationDrawable animationDrawable = (AnimationDrawable) splash_layout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();

//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//
//        // Check if user is already logged-in or not and redirect to news page
//        if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified())
//        {
//            String current_uid = firebaseAuth.getCurrentUser().getUid();
//            mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
//            mUserDatabase.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        String accountType = dataSnapshot.child("Account_type").getValue().toString();
//
//                        if (Objects.equals(accountType, "Section")) {
//                            Intent i = new Intent(getApplicationContext(), SelectAccountType.class);
//                            i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            if (Objects.equals(accountType, "Erasmus student")) {
//                                Intent i = new Intent(UserWelcome.this, MainErasmus.class);
//                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
//                            } else if (Objects.equals(accountType, "Erasmus academic")) {
//                                Intent i = new Intent(UserWelcome.this, MainForAcademic.class);
//                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
//                            } else {
//                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//
//                });
//
//        }

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
