package com.example.alexa.centreforinternationalrelationsuab.others.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.alexa.centreforinternationalrelationsuab.R;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OUsefulInfoOrange;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OUsefulInfoPosta;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OUsefulInfoTelekom;
import com.example.alexa.centreforinternationalrelationsuab.others.secondary.OUsefulInfoVodafone;

public class OUsefulInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouseful_info);

        ImageView sendToPosta = findViewById(R.id.web_posta);
        sendToPosta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OUsefulInfo.this,OUsefulInfoPosta.class);
                startActivity(intent);
            }
        });

        ImageView sendToTelekom = findViewById(R.id.web_telekom);
        sendToTelekom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OUsefulInfo.this,OUsefulInfoTelekom.class);
                startActivity(intent);
            }
        });

        ImageView sendToOrange = findViewById(R.id.web_orange);
        sendToOrange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OUsefulInfo.this,OUsefulInfoOrange.class);
                startActivity(intent);
            }
        });

        ImageView sendToVodafone = findViewById(R.id.web_vodafone);
        sendToVodafone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OUsefulInfo.this,OUsefulInfoVodafone.class);
                startActivity(intent);
            }
        });
    }
}
