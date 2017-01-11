package com.mba.AjjkiaPakaen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.cuboid.cuboidcirclebutton.CuboidButton;

/**
 * Created by Muhammad Bilal on 06/04/2016.
 */
public class MainMenu extends AppCompatActivity implements View.OnClickListener {
    CuboidButton search, think, rate;

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
                break;
            }
        }
    }

    public void initializations() {
        search = (CuboidButton) findViewById(R.id.searchRecipie);
        think = (CuboidButton) findViewById(R.id.thinkbutton);
        rate = (CuboidButton) findViewById(R.id.ratebtn);
        search.setOnClickListener(this);
        think.setOnClickListener(this);
        rate.setOnClickListener(this);

    }
}
