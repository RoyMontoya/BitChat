package com.example.amado.bitchat;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Amado on 01/06/2015.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(getApplicationContext(), "xVYF72JX72EINWbVub57Jr97EgV6U6jWUttQXyMz",
                "UMrz3srOWWkzRkeefdWYo3ZfwTP33CvlFy6WC4O8");
    }
}
