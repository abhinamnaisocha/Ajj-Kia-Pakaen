package com.mba.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Muhammad Bilal on 06/04/2016.
 */
public class MainMenu extends Activity implements View.OnClickListener {
    Button search, think, rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
        search = (Button) findViewById(R.id.searchbtn);
        think = (Button) findViewById(R.id.thinkbutton);
        rate = (Button) findViewById(R.id.ratebtn);
        search.setOnClickListener(this);
        think.setOnClickListener(this);
        rate.setOnClickListener(this);

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
            case R.id.searchbtn: {
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
                break;
            }
        }
    }
}
