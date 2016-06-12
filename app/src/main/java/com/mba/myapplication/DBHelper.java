package com.mba.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muhammad Bilal on 22/03/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ajjkiapakaen.db";

    private static final int DATABASE_VERSION = 1;

    private static final String TABLE1_NAME = "dishes";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INGREDIENTS = "ingredient";
    private static final String COLUMN_RECIPIE = "recipie";
    private static final String COLUMN_IMG = "img";
    private static final String COLUMN_RATED = "Rated";


    private SQLiteDatabase database;

    private final Context context;

    // database path
    private static String DATABASE_PATH;

    /**
     * constructor
     */
    public DBHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = ctx;
        DATABASE_PATH = context.getFilesDir().getParentFile().getPath()
                + "/databases/";

    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void create() throws IOException {
        boolean check = checkDataBase();

        SQLiteDatabase db_Read = null;

        // Creates empty database default system path
        db_Read = this.getWritableDatabase();
        db_Read.close();
        try {
            if (!check) {
                copyDataBase();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null,
                        SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            // database does't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DATABASE_PATH + DATABASE_NAME;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * open the database
     */
    public void open() throws SQLException {
        String myPath = DATABASE_PATH + DATABASE_NAME;
        database = SQLiteDatabase.openDatabase(myPath, null,
                SQLiteDatabase.OPEN_READWRITE);
    }

    /**
     * close the database
     */
    @Override
    public synchronized void close() {
        if (database != null)
            database.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    // retrieves a particular recipie
    public DishRecipie getReciepie(int id) throws SQLException {
        DishRecipie dish = new DishRecipie();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE1_NAME + " WHERE " + COLUMN_ID + "=" + id, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        dish.setDishId(id);
        dish.setDishName(cursor.getString(1));
        dish.setDishIng(cursor.getString(2));
        dish.setDishRecipie(cursor.getString(3));
        dish.setImgId(R.drawable.ic_launcher);
        dish.setRated(cursor.getInt(5));
        return dish;
    }

    public List<DishRecipie> selectedRecipie(String ing) throws SQLException {


        List<DishRecipie> list = new ArrayList<>();
        Log.d("lol", "SELECT * FROM " + TABLE1_NAME + " WHERE " + COLUMN_RECIPIE + " LIKE '%" + "lol" + "%'");

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE1_NAME + " WHERE " + COLUMN_RECIPIE + " LIKE '%" + ing + "%'", null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DishRecipie current = new DishRecipie();
            current.setDishId(cursor.getInt(0));
            current.setDishName(cursor.getString(1));
            current.setDishIng(cursor.getString(2));
            current.setDishRecipie(cursor.getString(3));
            current.setImgId(R.drawable.ic_launcher);
            current.setRated(cursor.getInt(5));

            list.add(current);
            cursor.moveToNext();
        }

        cursor.close();
        return list;
    }


    // retrieves all recipies
    public List<DishRecipie> getDishes() {
        List<DishRecipie> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE1_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            DishRecipie current = new DishRecipie();
            current.setDishId(cursor.getInt(0));
            current.setDishName(cursor.getString(1));
            current.setDishIng(cursor.getString(2));
            current.setDishRecipie(cursor.getString(3));
            current.setImgId(R.drawable.ic_launcher);
            current.setRated(cursor.getInt(5));

            list.add(current);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void updateRating(int id)
    {
        String updateQuery="Update "+TABLE1_NAME+ " SET "+COLUMN_RATED+"="+1+" where "+COLUMN_ID+"="+id;
        database.execSQL(updateQuery);
    }
}



