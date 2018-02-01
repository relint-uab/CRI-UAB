package com.example.alexa.centreforinternationalrelationsuab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexa.centreforinternationalrelationsuab.user.UserWelcome;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        TextView tv = findViewById(R.id.textViewCRI);
        ImageView iv = findViewById(R.id.imageView);
        Animation splash_transition = AnimationUtils.loadAnimation(this, R.anim.splash_transition);
        tv.startAnimation(splash_transition);
        iv.startAnimation(splash_transition);
        final Intent i = new Intent(this, UserWelcome.class);

        Thread timer = new Thread() {
            public void run (){
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };

        timer.start();
    }
}
