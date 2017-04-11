package com.mba.AjjkiaPakaen;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


/**
 * Created by Muhammad Bilal on 06/04/2016.
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    Button search, think, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        initializations();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.searchRecipie: {
                Intent i = new Intent(MainMenu.this, MainActivity.class);
                startActivity(i);
                break;
            }
            case R.id.thinkbutton: {
                Intent i = new Intent(MainMenu.this, DishSelector.class);
                startActivity(i);
                break;
            }
            case R.id.ratebtn: {
                Uri uri = Uri.parse("market://details?id=" + MainMenu.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + MainMenu.this.getPackageName())));
                }
                break;
            }
        }
    }

    public void initializations() {
        search = (Button) findViewById(R.id.searchRecipie);
        think = (Button) findViewById(R.id.thinkbutton);
        rate = (Button) findViewById(R.id.ratebtn);
        search.setOnClickListener(this);
        think.setOnClickListener(this);
        rate.setOnClickListener(this);
    }
}
