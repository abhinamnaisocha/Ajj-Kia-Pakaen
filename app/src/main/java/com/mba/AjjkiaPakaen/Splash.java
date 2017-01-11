package com.mba.AjjkiaPakaen;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Muhammad Bilal on 24/03/2016.
 */
public class Splash extends Activity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                try {
                    Class c = Class.forName("com.mba.AjjkiaPakaen.MainMenu");
                    Intent classIntent = new Intent(Splash.this, c);
                    startActivity(classIntent);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

}

