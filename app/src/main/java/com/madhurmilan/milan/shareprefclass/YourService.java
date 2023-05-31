package com.madhurmilan.milan.shareprefclass;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;

public class YourService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("","");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("not yet implement");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler.post(priodicUpdate);
        return START_STICKY;
    }

    public static boolean isOnline(Context c){
        ConnectivityManager cm = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    Handler handler = new Handler();
    private final Runnable priodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(priodicUpdate, 1*1000- SystemClock.elapsedRealtime()%1000);
            Intent broadCastIntent = new Intent();
            broadCastIntent.setAction(BroadCastStringForAction);
            broadCastIntent.putExtra("online_status", ""+isOnline(YourService.this));
            sendBroadcast(broadCastIntent);
        }
    };

}