package com.example.alexa.centreforinternationalrelationsuab.others.secondary;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.alexa.centreforinternationalrelationsuab.R;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class OUsefulInfoPosta extends AppCompatActivity {

    private SpotsDialog mProgress;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ouseful_info_posta);
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

        //Get a reference to your WebView//
        WebView webView = findViewById(R.id.webview_posta);

        //Specify the URL you want to display//
        webView.loadUrl("https://www.posta-romana.ro/");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        //Back button
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    WebView webView = (WebView) v;

                    switch (keyCode) {
                        case KeyEvent.KEYCODE_BACK:
                            if (webView.canGoBack()) {
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

}