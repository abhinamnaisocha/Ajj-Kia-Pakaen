package com.mba.myapplication;

import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Muhammad Bilal on 21/04/2016.
 */
public class DishSelector extends AppCompatActivity implements View.OnClickListener {
    DBHelper dbHelper;
    List<DishRecipie> dishlist;
    Button rice, chicken, beef, vegitable;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dishlist = new ArrayList<>();
        dbHelper = new DBHelper(this);
        setContentView(R.layout.activity_dishselector);

        try {
            // check if database exists in app path, if not copy it from assets
            dbHelper.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            // open the database
            dbHelper.open();
            dbHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }



        rice = (Button) findViewById(R.id.btnrice);
        beef = (Button) findViewById(R.id.btnbeef);
        vegitable = (Button) findViewById(R.id.btnvegitables);
        rice.setOnClickListener(this);
        beef.setOnClickListener(this);
        vegitable.setOnClickListener(this);

    }

    public int recipieSelected(String ing) {
        dishlist = dbHelper.selectedRecipie(ing);
        Random r = new Random();
        return r.nextInt(dishlist.size());
    }

    @Override
    public void onClick(View v) {

        i = new Intent(DishSelector.this, DetailDish.class);
        i.putExtra("class", "selector");
        switch (v.getId()) {

            case R.id.btnrice: {

                int id = recipieSelected("چاول");
                i.putExtra("dishId", dishlist.get(id).getDishId());
                startActivity(i);
                break;
            }

            case R.id.btnbeef: {
                int id = recipieSelected("گوشت");
                i.putExtra("dishId", dishlist.get(id).getDishId());
                startActivity(i);
                break;
            }

            case R.id.btnvegitables: {
                int id = recipieSelected("سبزی");
                i.putExtra("dishId", dishlist.get(id).getDishId());
                startActivity(i);
                break;
            }
        }

    }
}
