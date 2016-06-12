package com.mba.myapplication;

import com.firebase.client.Firebase;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Muhammad Bilal on 10/06/2016.
 */
public class DishApplication extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/aleem-urdu-unicode-regular-1.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
    }
}