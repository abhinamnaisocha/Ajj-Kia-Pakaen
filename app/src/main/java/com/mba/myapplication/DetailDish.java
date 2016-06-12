package com.mba.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Muhammad Bilal on 06/04/2016.
 */
public class DetailDish extends AppCompatActivity implements RatingBar.OnRatingBarChangeListener {
    private DishRecipie dish;
    DBHelper myDbHelper;
    TextView dishName, dishIng, dishRecipie;
    ImageView dishImg;
    RatingBar userrating, totalrating;
    Firebase writeFirebaseRef, readFirebaseRef, readFirebaseRef1, readFirebaseRef2;
    long count = 0;
    double ratings = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myDbHelper = new DBHelper(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        writeFirebaseRef = new Firebase("https://dishes-12c91.firebaseio.com/");

        dishName = (TextView) findViewById(R.id.txtDishName);
        dishIng = (TextView) findViewById(R.id.txtDishIng);
        dishRecipie = (TextView) findViewById(R.id.txtDishRecipie);
        dishImg = (ImageView) findViewById(R.id.ImgDish);
        userrating = (RatingBar) findViewById(R.id.ratingBar2);
        totalrating = (RatingBar) findViewById(R.id.ratingBar);
        totalrating.setEnabled(false);

        Intent activityIntent = getIntent();
        if (activityIntent.getStringExtra("class").equals("menu")) {
            int id = activityIntent.getIntExtra("dishId", 0);
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            dish = myDbHelper.getReciepie(id);

            dishName.setText(dish.getDishName());
            dishIng.setText(dish.getDishIng());
            dishRecipie.setText(dish.getDishRecipie());
            dishImg.setImageResource(dish.getImgId());
            myDbHelper.close();
        } else if (activityIntent.getStringExtra("class").equals("selector")) {
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            int id = activityIntent.getIntExtra("dishId", 0);
            dish = myDbHelper.getReciepie(id);

            dishName.setText(dish.getDishName());
            dishIng.setText(dish.getDishIng());
            dishRecipie.setText(dish.getDishRecipie());
            dishImg.setImageResource(dish.getImgId());
            myDbHelper.close();
        }
        if (dish.getRated() >= 1) {
            userrating.setEnabled(false);
        }
        userrating.setOnRatingBarChangeListener(this);


        readFirebaseRef1 = new Firebase("https://dishes-12c91.firebaseio.com/" + dish.getDishId() + "/count");
        readFirebaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    count = (long) snapshot.getValue();
                } else
                    count = 0;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(DetailDish.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        readFirebaseRef = new Firebase("https://dishes-12c91.firebaseio.com/" + dish.getDishId() + "/rating");
        readFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    ratings = (double) snapshot.getValue();
                    if (count > 0) {
                        totalrating.setRating(Float.parseFloat(String.valueOf((Double) snapshot.getValue() / count)));
                    } else
                        totalrating.setRating(Float.parseFloat(String.valueOf((Double) snapshot.getValue())));
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(DetailDish.this, "The read failed: " + firebaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        if (dish.getRated() == 0) {
            myDbHelper = new DBHelper(this);
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            myDbHelper.updateRating(dish.getDishId());
            myDbHelper.close();


            DishRating dishRating = new DishRating(dishName.getText().toString(), (float) (ratings + rating), count + 1);
            writeFirebaseRef.child(String.valueOf(dish.getDishId())).setValue(dishRating);
            userrating.setEnabled(false);

        }
    }

    public class DishRating {
        private float rating;
        private String name;
        private long count;

        public DishRating(String name, float rating, long count) {

            (this).name = name;
            (this).rating = rating;
            (this).count = count;
        }

        public long getCount() {
            return count;
        }

        public float getRating() {
            return rating;
        }

        public String getName() {
            return name;
        }
    }
}
