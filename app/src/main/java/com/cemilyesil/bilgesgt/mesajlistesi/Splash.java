package com.cemilyesil.bilgesgt.mesajlistesi;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(Splash.this,
                        Mesaj_Listesi.class);
                startActivity(i);
                finish();

                overridePendingTransition(R.anim.fade_in,
                        R.anim.fade_out);
            }
        }, SPLASH_TIME_OUT);
    }
}
