package com.example.alexa.centreforinternationalrelationsuab;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.alexa.centreforinternationalrelationsuab.user.SelectAccountType;
import com.example.alexa.centreforinternationalrelationsuab.user.UserWelcome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {

    private DatabaseReference mUserDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = findViewById(R.id.textViewCRI);
        ImageView iv = findViewById(R.id.imageView);
        ImageView erasmus_logo = findViewById(R.id.erasmus_logo);
        Animation splash_transition = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        tv.startAnimation(splash_transition);
        iv.startAnimation(splash_transition);
        erasmus_logo.startAnimation(splash_transition);

        RelativeLayout splash_layout = findViewById(R.id.splash_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) splash_layout.getBackground();
        animationDrawable.setEnterFadeDuration(1000);
        animationDrawable.setExitFadeDuration(2000);
        animationDrawable.start();

        Thread timer = new Thread() {
            public void run (){
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                    // Check if user is already logged-in or not and redirect to news page
                    if(firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified())
                    {
                        String current_uid = firebaseAuth.getCurrentUser().getUid();
                        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid).child("Account_type");
                        mUserDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String accountType = dataSnapshot.getValue().toString();

                                    switch (accountType) {
                                        case "Erasmus student":
                                            Intent i1 = new Intent(getApplicationContext(), MainErasmus.class);
                                            i1.setFlags(i1.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i1);
                                            finish();
                                            break;
                                        case "Erasmus academic":
                                            Intent i2 = new Intent(getApplicationContext(), MainErasmusAcademic.class);
                                            i2.setFlags(i2.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i2);
                                            finish();
                                            break;
                                        case "International student":
                                            Intent i3 = new Intent(getApplicationContext(), MainActivity.class);
                                            i3.setFlags(i3.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(i3);
                                            finish();
                                            break;
                                            default:
                                                Intent i = new Intent(getApplicationContext(), SelectAccountType.class);
                                                i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(i);
                                                finish();
                                                break;
                                    }

//                                    if (Objects.equals(accountType, "Erasmus student")) {
//                                        Intent i = new Intent(getApplicationContext(), MainErasmus.class);
//                                        //i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    } else if (Objects.equals(accountType, "Erasmus academic")) {
//                                        Intent i = new Intent(getApplicationContext(), MainErasmusAcademic.class);
//                                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    } else if (Objects.equals(accountType, "International student")){
//                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    } else {
//                                        Intent i = new Intent(getApplicationContext(), SelectAccountType.class);
//                                        i.setFlags(i.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        startActivity(i);
//                                        finish();
//                                    }
                                }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        });

                    } else {
                        Intent i = new Intent(getApplicationContext(), SliderActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        };

        timer.start();
    }

}
