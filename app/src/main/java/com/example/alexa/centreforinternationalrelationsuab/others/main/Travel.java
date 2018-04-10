package com.example.alexa.centreforinternationalrelationsuab.others.main;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.alexa.centreforinternationalrelationsuab.R;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class Travel extends AppCompatActivity {

    private SpotsDialog mProgress;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        // Progress dialog Loading page
        mProgress = new SpotsDialog(this, R.style.Loading);
        Objects.requireNonNull(mProgress.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mProgress.show();
        Runnable progressRunnable = new Runnable() {

            @Override
            public void run() {
                mProgress.dismiss();
            }
        };

        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);

        //Back button


        //Get a reference to your WebView//
        WebView webView = findViewById(R.id.webview);

        //Specify the URL you want to display//
        webView.loadUrl("http://www.romania.travel/en/");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        webView.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    WebView webView = (WebView) v;

                    switch(keyCode)
                    {
                        case KeyEvent.KEYCODE_BACK:
                            if(webView.canGoBack())
                            {
                                webView.goBack();
                                return true;
                            }
                            break;
                    }
                }

                return false;
            }
        });


    }





//    @Override
//    public void onBackPressed() {
//        if (webView.canGoBack()) {
//            webView.goBack();
//        } else {
//            super.onBackPressed();
//        }
//    }
}
