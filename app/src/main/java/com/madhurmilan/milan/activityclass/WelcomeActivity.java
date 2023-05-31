package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataApp;
import com.madhurmilan.milan.responseclass.DataPlayTraining;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelcomeActivity extends AppCompatActivity {

    ProgressBar progressBar;
    private MaterialTextView dataConText;
    private IntentFilter mIntentFilter;
    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_welcome);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        progressBar = findViewById(R.id.progress_bar);
        dataConText = findViewById(R.id.dataConText);

        utility = new Utility(dataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        method();

    }

    private void method() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (SharPrefClass.getsignInSuccess(WelcomeActivity.this)){
                    System.out.println(SharPrefClass.getsignInSuccess(WelcomeActivity.this));
                    SharPrefClass.setSharedBooleanStatus(WelcomeActivity.this, SharPrefClass.KEY_DEVELOPER_MODE, false);
                    Intent intent = new Intent(WelcomeActivity.this, SPinActivity.class);
                    intent.putExtra(getString(R.string.pin_reset), 300);
                    intent.putExtra(getString(R.string.phone_number), SharPrefClass.getRegistrationObject(WelcomeActivity.this, SharPrefClass.KEY_PHONE_NUMBER));
                    startActivity(intent);
                    finish();
                } else {
                    if (YourService.isOnline(WelcomeActivity.this))
                        AppLiveStatusFun(WelcomeActivity.this);
                    else Toast.makeText(WelcomeActivity.this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
                }
            }
        },2000);

    }

    private void AppLiveStatusFun(WelcomeActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataPlayTraining> call = ApiClass.getClient().appLiveStatus("");
        call.enqueue(new Callback<DataPlayTraining>() {
            @Override
            public void onResponse(Call<DataPlayTraining> call, Response<DataPlayTraining> response) {
                if (response.isSuccessful()){
                    DataPlayTraining liveModel = response.body();
                    assert liveModel != null;
                    System.out.println(liveModel.getCode());
                    if (liveModel.getCode().equals("100")){
                        SharPrefClass.setSharedBooleanStatus(activity, SharPrefClass.KEY_DEVELOPER_MODE, false);
                        Intent i = new Intent(WelcomeActivity.this, RegistrationActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        SharPrefClass.setSigninTkn(activity, liveModel.getData());
                        SharPrefClass.setSharedBooleanStatus(activity, SharPrefClass.KEY_DEVELOPER_MODE, true);
                        getAppInfoMethod(WelcomeActivity.this);
                    }
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataPlayTraining> call, Throwable t) {
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getAppInfoMethod(WelcomeActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataApp> call = ApiClass.getClient().getAppInfo("");
        call.enqueue(new Callback<DataApp>() {
            @Override
            public void onResponse(@NonNull Call<DataApp> call, @NonNull Response<DataApp> response) {
                if (response.isSuccessful()) {
                    DataApp dataApp = response.body();
                    if (dataApp.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataApp.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataApp.getStatus().equalsIgnoreCase(getString(R.string.success))) {
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_MAR_TXT, dataApp.getData().getBanner_marquee());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_PHONE_NUMBER1, "+91" + dataApp.getData().getContact_details().getMobile_no_1());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_PHONE_NUMBER2,   dataApp.getData().getContact_details().getTelegram_channel_link());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_WHATSAP_NUMBER, "+91" + dataApp.getData().getContact_details().getWhatsapp_no());
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_REACH_US_EMAIL, dataApp.getData().getContact_details().getEmail_1());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES1, dataApp.getData().getBanner_image().getBanner_img_1());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES2, dataApp.getData().getBanner_image().getBanner_img_2());
                        SharPrefClass.setPosterImages(activity, SharPrefClass.KEY_POSTER_IMAGES3, dataApp.getData().getBanner_image().getBanner_img_3());

                        Intent intent = new Intent(activity, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    } else
                        Toast.makeText(activity, dataApp.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DataApp> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("getAppDetails error " + t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
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