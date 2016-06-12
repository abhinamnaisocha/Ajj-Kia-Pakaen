package com.mba.myapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.IOException;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, RecycleListAdapter.ClickListner {

    private RecyclerView recyclerView;
    private RecycleListAdapter adapter;
    private MenuItem searchMenuItem;
    private SearchView searchView;
    private List<DishRecipie> dishes;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        recyclerView = (RecyclerView) findViewById(R.id.rvdishes);

        DBHelper myDbHelper = new DBHelper(this);

        try {
            // check if database exists in app path, if not copy it from assets
            myDbHelper.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            // open the database
            myDbHelper.open();
            myDbHelper.getWritableDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        dishes = myDbHelper.getDishes();


        adapter = new RecycleListAdapter(getBaseContext(), dishes);
        myDbHelper.close();
        adapter.setClickListner(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).build());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager)
                getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setSearchableInfo(searchManager.
                getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            adapter.getFilter().filter("");
        } else {
            adapter.getFilter().filter(newText.toString());
        }
        return true;
    }

    @Override
    public void itemClicked(View itemView, int position) {

        Toast.makeText(MainActivity.this, "Item Clicked: " + dishes.get(position).getDishName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this, DetailDish.class);
        i.putExtra("dishId", dishes.get(position).getDishId());
        i.putExtra("class", "menu");
        startActivity(i);
    }
}
