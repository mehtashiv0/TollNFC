package com.firstapp.tollnfc;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class Utility {
    Context context;

    Utility(Context context){
        this.context=context;
    }

    public boolean checkInternetConnectionALL() {
        ConnectivityManager conmag = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // for all active Network connection

        NetworkInfo info = conmag.getActiveNetworkInfo();
        if (info != null && info.isConnected())
            return true;
        else
            Toast.makeText(context.getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        return false;
    }
    public void toast(String msg){
        Toast.makeText(context.getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }
}
