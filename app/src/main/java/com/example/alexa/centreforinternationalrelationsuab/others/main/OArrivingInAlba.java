package com.example.alexa.centreforinternationalrelationsuab.others.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OArrivingInAlbaAirBuc;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OArrivingInAlbaAirCluj;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OArrivingInAlbaAirSibiu;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OArrivingInAlbaAirTim;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OArrivingInAlbaAutogari;

public class OArrivingInAlba extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oarriving_in_alba);

        ImageButton sendToAutogari = findViewById(R.id.web_autogari);
        sendToAutogari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OArrivingInAlba.this,OArrivingInAlbaAutogari.class);
                startActivity(intent);
            }
        });

        ImageButton sendToAirBuc = findViewById(R.id.web_airport_buc);
        sendToAirBuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OArrivingInAlba.this,OArrivingInAlbaAirBuc.class);
                startActivity(intent);
            }
        });

        ImageButton sendToAirSibiu = findViewById(R.id.web_airport_sibiu);
        sendToAirSibiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OArrivingInAlba.this,OArrivingInAlbaAirSibiu.class);
                startActivity(intent);
            }
        });

        ImageButton sendToAirCluj = findViewById(R.id.web_airport_cluj);
        sendToAirCluj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OArrivingInAlba.this,OArrivingInAlbaAirCluj.class);
                startActivity(intent);
            }
        });

        ImageButton sendToAirTim = findViewById(R.id.web_airport_tim);
        sendToAirTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OArrivingInAlba.this,OArrivingInAlbaAirTim.class);
                startActivity(intent);
            }
        });
    }
}
