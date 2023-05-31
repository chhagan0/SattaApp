package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataPlayTraining;
import com.madhurmilan.milan.responseclass.DataValue;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GameValuesActivity extends AppCompatActivity {

    private MaterialToolbar mToolbar;
    private FrameLayout mSingle, mJodi, mSingleP, mDoubleD, mTripleD, mHalfS, mFullS;
    private MaterialTextView mSingleDValue, mJodiDValue, mSinglePValue, mDoubleDValue, mTripleDValue, mHalfSValue, mFullSValue;
    private int activity=0;
    private MaterialCardView images;
    private MaterialCardView mHowToLearn;
    private MaterialButton mWatchYT;
    private MaterialTextView mHowToLarnText;
    private ProgressBar mProgressBar;
    private IntentFilter mIntentFilter;
    Utility utility;

    List<MaterialTextView> mDigitValue = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_value);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        LoadDataMethod();
    }

    private void intVariables() {
        mToolbar = findViewById(R.id.toolbar);
        mSingle = findViewById(R.id.singleD);
        mSingleDValue = findViewById(R.id.single_d_v);
        mJodi = findViewById(R.id.jodi_d);
        mJodiDValue = findViewById(R.id.jodi_d_value);
        mSingleP = findViewById(R.id.single_p);
        mSinglePValue = findViewById(R.id.single_p_v);
        mDoubleD = findViewById(R.id.double_d);
        mDoubleDValue = findViewById(R.id.double_d_v);
        mTripleD = findViewById(R.id.triple_d);
        mTripleDValue = findViewById(R.id.triple_d_v);
        mHalfS = findViewById(R.id.half_s);
        mHalfSValue = findViewById(R.id.half_s_v);
        mFullS = findViewById(R.id.full_s);
        mFullSValue = findViewById(R.id.full_s_v);
        mHowToLarnText = findViewById(R.id.howToPlayText);
        mProgressBar = findViewById(R.id.progressBar);

        images = findViewById(R.id.icon);
        mHowToLearn = findViewById(R.id.how_to_learn_card);
        mWatchYT = findViewById(R.id.watcch_yt);

        MaterialTextView mDataConText = findViewById(R.id.dataConText);
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);

        activity = getIntent().getIntExtra(getString(R.string.main_activity), 0);
    }

    private void LoadDataMethod() {
        if (activity==1){
            mToolbar.setTitle(getString(R.string.rates));
            mSingle.setVisibility(View.VISIBLE);
            mJodi.setVisibility(View.VISIBLE);
            mSingleP.setVisibility(View.VISIBLE);
            mDoubleD.setVisibility(View.VISIBLE);
            mTripleD.setVisibility(View.VISIBLE);
            mHalfS.setVisibility(View.VISIBLE);
            mFullS.setVisibility(View.VISIBLE);
            gameValuesMethod(GameValuesActivity.this);
        }else if (activity==2){
            mToolbar.setTitle(getString(R.string.how_play));
            images.setVisibility(View.VISIBLE);
            mWatchYT.setVisibility(View.VISIBLE);
            mHowToLearn.setVisibility(View.VISIBLE);
            getHowToLearnMethod(GameValuesActivity.this);

        }

        mDigitValue.add(mSingleDValue);
        mDigitValue.add(mJodiDValue);
        mDigitValue.add(mSinglePValue);
        mDigitValue.add(mDoubleDValue);
        mDigitValue.add(mTripleDValue);
        mDigitValue.add(mHalfSValue);
        mDigitValue.add(mFullSValue);

        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void gameValuesMethod(GameValuesActivity activity) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataValue> call = ApiClass.getClient().tournamentValueList(SharPrefClass.getLoginInToken(activity),"");
        call.enqueue(new Callback<DataValue>() {
            @Override
            public void onResponse(Call<DataValue> call, Response<DataValue> response) {
                if (response.isSuccessful()){
                    DataValue dataValue = response.body();
                    if (dataValue.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataValue.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if(dataValue.getStatus().equalsIgnoreCase("success")) {
                        List<DataValue.Data> gameRateData = dataValue.getData();
                        for (int i = 0; i < gameRateData.size(); i++) {
                            String value = gameRateData.get(i).getCost_amount() + "-" + gameRateData.get(i).getEarning_amount();
                            mDigitValue.get(i).setText(value);
                        }
                    }
               //     Toast.makeText(GameValuesActivity.this, dataValue.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataValue> call, Throwable t) {
                System.out.println("gameRateList Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getHowToLearnMethod(GameValuesActivity activity) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataPlayTraining> call = ApiClass.getClient().howToLearn(SharPrefClass.getLoginInToken(activity),"");
        call.enqueue(new Callback<DataPlayTraining>() {
            @Override
            public void onResponse(Call<DataPlayTraining> call, Response<DataPlayTraining> response) {
                if (response.isSuccessful()){
                    DataPlayTraining howToPlayModel = response.body();
                    if (howToPlayModel.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, howToPlayModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (howToPlayModel.getStatus().equals(getString(R.string.success)))
                    mHowToLarnText.setText(howToPlayModel.getData());
                //    Toast.makeText(GameValuesActivity.this, howToPlayModel.getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataPlayTraining> call, Throwable t) {
                System.out.println("howToPlay Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
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

    public void WatchItOnYoutubeBtn(View view) {
        Toast.makeText(this, getString(R.string.youtube_video_not_added_yet), Toast.LENGTH_SHORT).show();
    }

}