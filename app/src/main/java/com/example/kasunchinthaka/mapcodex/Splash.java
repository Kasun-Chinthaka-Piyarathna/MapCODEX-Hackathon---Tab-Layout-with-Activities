package com.example.kasunchinthaka.mapcodex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splash extends Activity {

    // Splash screen timer
        private static int SPLASH_TIME_OUT = 3000;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);


            setContentView(R.layout.activity_splash);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    finish();
                    Intent i = new Intent(Splash.this, TabBarActivity.class);
                    startActivity(i);

                }
            }, SPLASH_TIME_OUT);

        }


}

