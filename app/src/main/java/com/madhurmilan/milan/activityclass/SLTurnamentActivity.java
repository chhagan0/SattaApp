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

public class SLTurnamentActivity extends AppCompatActivity {

    private Intent intent;
    private IntentFilter mIntentFilter;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_l_turnament);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));
        intVariables();
        loadData();
    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void intVariables() {
        MaterialTextView dataConText = findViewById(R.id.dataConText);
        toolbar = findViewById(R.id.toolbar);
        Utility utility = new Utility(dataConText);
        String games = getIntent().getStringExtra(getString(R.string.game));
        intent = new Intent(this, SLBPActivity.class);
        intent.putExtra("games", games);
    }

    public void singleDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 8);
        startActivity(intent);
    }

    public void singlePana(View view) {
        intent.putExtra(getString(R.string.game_name), 9);
        startActivity(intent);
    }

    public void doublePana(View view) {
        intent.putExtra(getString(R.string.game_name), 10);
        startActivity(intent);
    }

    public void triplePana(View view) {
        intent.putExtra(getString(R.string.game_name), 11);
        startActivity(intent);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

}