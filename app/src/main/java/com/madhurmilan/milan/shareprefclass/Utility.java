package com.madhurmilan.milan.shareprefclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.google.android.material.textview.MaterialTextView;

public class Utility {
    public static final String BroadCastStringForAction="checkingInternet";
    public static MaterialTextView status;

    public Utility(MaterialTextView status) {
        Utility.status = status;
    }

    public static BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)){
                if (intent.getStringExtra("online_status").equals("true")){
                    status.setVisibility(View.GONE);
                }else status.setVisibility(View.VISIBLE);
            }
        }
    };
}
