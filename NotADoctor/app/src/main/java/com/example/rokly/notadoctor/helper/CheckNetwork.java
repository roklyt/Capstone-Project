package com.example.rokly.notadoctor.helper;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

public class CheckNetwork {

    public  boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);


        return ((cm != null ? cm.getActiveNetworkInfo() : null) != null);
    }
}
