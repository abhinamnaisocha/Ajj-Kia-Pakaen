package com.mba.AjjkiaPakaen;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
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
    private ShareActionProvider mShareActionProvider;
    DishRecipie dish;
    DBHelper myDbHelper;
    TextView dishName, dishIng, dishRecipie, rateTxt, txtRating;
    Toolbar toolbar;
    ImageView dishImg;
    RatingBar userrating, totalrating;
    Firebase writeFirebaseRef, readFirebaseRef, readFirebaseRef1;
    long count = 0;
    double ratings = 0, prevRating = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detaildish);

        initializations();
        setSupportActionBar(toolbar);

        userrating = ratingColor(Color.parseColor("#f409c1"), userrating);
        totalrating = ratingColor(Color.parseColor("#9c037b"), totalrating);

        Intent activityIntent = getIntent();
        if (activityIntent.getStringExtra("class").equals("menu")) {
            String name = activityIntent.getStringExtra("dishId");
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            dish = myDbHelper.getReciepieByName(name);

            dishName.setText(dish.getDishName());
            dishIng.setText(dish.getDishIng());
            dishRecipie.setText(dish.getDishRecipie());
            if (dish.getImg() != null) {
                Bitmap b1 = BitmapFactory.decodeByteArray(dish.getImg(), 0, dish.getImg().length);
                dishImg.setImageBitmap(b1);
            } else
                dishImg.setImageResource(R.drawable.ic_launcher);
            userrating.setRating(dish.getRated());

            prevRating = userrating.getRating();

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
            if (dish.getImg() != null) {
                Bitmap b1 = BitmapFactory.decodeByteArray(dish.getImg(), 0, dish.getImg().length);
                dishImg.setImageBitmap(b1);
            } else
                dishImg.setImageResource(R.drawable.ic_launcher);
            myDbHelper.close();
        }
        if (dish.getRated() > 0) {
            rateTxt.setText("ریٹنگ تبدیل کریں");
        }
        userrating.setOnRatingBarChangeListener(this);


        readFirebaseRef1 = new Firebase("https://dishes-12c91.firebaseio.com/" + dish.getDishId() + "/count");
        readFirebaseRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    count = (long) snapshot.getValue();
                    txtRating.setText(count + " Ratings");
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

    public RatingBar ratingColor(int color, RatingBar rb) {
        LayerDrawable stars = (LayerDrawable) rb.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        return rb;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);
// Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, dish.getDishName() + "\n" + dish.getDishIng() + "\n" + dish.getDishRecipie());
        sendIntent.setType("text/plain");

        mShareActionProvider.setShareIntent(sendIntent);


        MenuItem itemSearch = menu.findItem(R.id.Search);
        itemSearch.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


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
        rateTxt.setText("ریٹنگ تبدیل کریں");
        if (dish.getRated() == 0 && prevRating == 0) {
            myDbHelper = new DBHelper(this);
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }
            myDbHelper.updateRating(dish.getDishId(), rating);
            myDbHelper.close();


            DishRating dishRating = new DishRating(dishName.getText().toString(), (float) (ratings + rating), count + 1);
            writeFirebaseRef.child(String.valueOf(dish.getDishId())).setValue(dishRating);

        } else {
            if (prevRating > rating) {

                DishRating dishRating = new DishRating(dishName.getText().toString(), (float) (ratings - Math.abs(prevRating - rating)), count);
                writeFirebaseRef.child(String.valueOf(dish.getDishId())).setValue(dishRating);
            } else {
                DishRating dishRating = new DishRating(dishName.getText().toString(), (float) (ratings + Math.abs(rating - prevRating)), count);
                writeFirebaseRef.child(String.valueOf(dish.getDishId())).setValue(dishRating);
            }

            myDbHelper = new DBHelper(this);
            try {
                // open the database
                myDbHelper.open();
                myDbHelper.getWritableDatabase();
            } catch (SQLException sqle) {
                throw sqle;
            }

            myDbHelper.updateRating(dish.getDishId(), rating);
            myDbHelper.close();

        }
        prevRating = rating;
    }

    public void initializations() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        myDbHelper = new DBHelper(this);
        writeFirebaseRef = new Firebase("https://dishes-12c91.firebaseio.com/");

        dishName = (TextView) findViewById(R.id.txtDishName);
        dishIng = (TextView) findViewById(R.id.txtDishIng);
        dishRecipie = (TextView) findViewById(R.id.txtDishRecipie);
        rateTxt = (TextView) findViewById(R.id.textView);
        dishImg = (ImageView) findViewById(R.id.ImgDish);
        userrating = (RatingBar) findViewById(R.id.ratingBar2);
        totalrating = (RatingBar) findViewById(R.id.ratingBar);
        txtRating = (TextView) findViewById(R.id.txtRatings);
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
