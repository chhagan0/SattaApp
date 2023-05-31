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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

public class TurnamentActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ShapeableImageView sinD, jodD, sinP, douP, triP, haS, fuS;
    private Intent intent;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_turnament);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        loadData();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    private void loadData() {
        String games = getIntent().getStringExtra(getString(R.string.game));
        Boolean open = getIntent().getBooleanExtra("open", false);
        intent = new Intent(this, PlayingActivity.class);
        intent.putExtra("open", open);
        intent.putExtra("games", games);
        if(!open){
            sinD.setVisibility(View.VISIBLE);
            jodD.setVisibility(View.VISIBLE);
            sinP.setVisibility(View.VISIBLE);
            douP.setVisibility(View.VISIBLE);
            triP.setVisibility(View.VISIBLE);
            haS.setVisibility(View.VISIBLE);
            fuS.setVisibility(View.VISIBLE);
        }

        Utility utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }


    private void intVariables() {
        toolbar = findViewById(R.id.toolbar);
        sinD = findViewById(R.id.singleD);
        sinP = findViewById(R.id.single_p);
        jodD = findViewById(R.id.jodi_d);
        douP = findViewById(R.id.doP);
        haS = findViewById(R.id.half_s);
        triP = findViewById(R.id.tri_p);
        fuS = findViewById(R.id.full_s);
        mDataConText = findViewById(R.id.dataConText);
    }

    public void singleDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 1);
        startActivity(intent);
    }

    public void jodiDigit(View view) {
        intent.putExtra(getString(R.string.game_name), 2);
        startActivity(intent);
    }

    public void singlePana(View view) {
        intent.putExtra(getString(R.string.game_name), 3);
        startActivity(intent);
    }

    public void doublePana(View view) {
        intent.putExtra(getString(R.string.game_name), 4);
        startActivity(intent);
    }

    public void triplePana(View view) {
        intent.putExtra(getString(R.string.game_name), 5);
        startActivity(intent);
    }

    public void halfSangam(View view) {
        intent.putExtra(getString(R.string.game_name), 6);
        startActivity(intent);
    }

    public void fullSangam(View view) {
        intent.putExtra(getString(R.string.game_name), 7);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

}