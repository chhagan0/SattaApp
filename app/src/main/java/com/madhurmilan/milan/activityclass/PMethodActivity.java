package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

public class PMethodActivity extends AppCompatActivity {

    private MaterialToolbar mToolbar;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_method);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        confToolbar();
        loadData();

    }

    private void loadData() {
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    private void intVariables() {
        mToolbar = findViewById(R.id.toolbar);
        mDataConText = findViewById(R.id.dataConText);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }


    public void Bank(View view) {
        Intent intent = new Intent(this, BActivity.class);
        startActivity(intent);
    }

    public void paytm(View view) {
        Intent intent = new Intent(this, UPIDActivity.class);
        intent.putExtra(getString(R.string.upi), 1);
        startActivity(intent);
    }

    public void phonePe(View view) {
        Intent intent = new Intent(this, UPIDActivity.class);
        intent.putExtra(getString(R.string.upi), 2);
        startActivity(intent);
    }

    public void gPay(View view) {
        Intent intent = new Intent(this, UPIDActivity.class);
        intent.putExtra(getString(R.string.upi),3);
        startActivity(intent);
    }

    private void confToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

}