package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataApp;
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPinActivity extends AppCompatActivity {

    private AppCompatCheckBox cB1, cB2, cB3, cB4;
    private LinearLayout btnCr, cBLay;
    private Animation mShake;
    private ProgressBar progressBar;
    private MaterialTextView txtCr;
    private Vibrator mVibrator;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_pin);


        intVariables();
        loadData();
    }

    private void loadData() {
        Utility utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        getAppInfoMethod(this);
    }

    private void intVariables() {
        progressBar = findViewById(R.id.progressBar);
        cB3 = findViewById(R.id.checkBox3);
        cB4 = findViewById(R.id.checkBox4);
        cB1 = findViewById(R.id.checkBox1);
        cB2 = findViewById(R.id.checkBox2);
        btnCr = findViewById(R.id.btnClear);
        txtCr = findViewById(R.id.textClear);
        cBLay = findViewById(R.id.checkBoxLay);
        mDataConText = findViewById(R.id.dataConText);
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
    }


    public void button1(View view) {
        checkBoxFun("1");
    }

    public void button2(View view) {
        checkBoxFun("2");
    }

    public void button3(View view) {
        checkBoxFun("3");
    }

    public void button4(View view) {
        checkBoxFun("4");
    }

    public void button5(View view) {
        checkBoxFun("5");
    }

    public void button6(View view) {
        checkBoxFun("6");
    }

    public void button7(View view) {
        checkBoxFun("7");
    }

    public void button8(View view) {
        checkBoxFun("8");
    }

    public void button9(View view) {
        checkBoxFun("9");
    }

    public void button0(View view) {
        checkBoxFun("0");
    }

    public void btnDlt(View view) {
        if (cB1.isChecked()&& cB2.isChecked()&& cB3.isChecked()&& cB4.isChecked()){
            cB4.setChecked(false);
        }else if (cB1.isChecked() && cB2.isChecked()&& cB3.isChecked()&& !cB4.isChecked()){
            cB3.setChecked(false);
        }else if (cB1.isChecked() && cB2.isChecked() && !cB3.isChecked()&& !cB4.isChecked()){
            cB2.setChecked(false);
        }else if (cB1.isChecked() && !cB2.isChecked()&&!cB3.isChecked()&& !cB4.isChecked()){
            cB1.setChecked(false);
            btnCr.setVisibility(View.INVISIBLE);
            txtCr.setVisibility(View.INVISIBLE);
        }
    }

    public void btnClr(View view) {
        cB1.setChecked(false);
        cB2.setChecked(false);
        cB3.setChecked(false);
        cB4.setChecked(false);
        btnCr.setVisibility(View.INVISIBLE);
        txtCr.setVisibility(View.INVISIBLE);
    }

    String pin1, pin2, pin3,pin4;
    public void checkBoxFun(String pin){
        if (!cB1.isChecked()&& !cB2.isChecked()&& !cB3.isChecked()&& !cB4.isChecked()){
            cB1.setChecked(true);
            btnCr.setVisibility(View.VISIBLE);
            txtCr.setVisibility(View.VISIBLE);
            pin1 = pin;
        }else if (cB1.isChecked() && !cB2.isChecked()&& !cB3.isChecked()&& !cB4.isChecked()){
            cB2.setChecked(true);
            pin2 = pin;
        }else if (cB1.isChecked() && cB2.isChecked() && !cB3.isChecked()&& !cB4.isChecked()){
            cB3.setChecked(true);
            pin3 = pin;
        }else if (cB1.isChecked() && cB2.isChecked()&& cB3.isChecked()&& !cB4.isChecked()){
            cB4.setChecked(true);
            pin4 = pin;
            sPinVerifyMethod(SPinActivity.this,pin1 + pin2 + pin3 + pin4);
        }
    }

    private void sPinVerifyMethod(SPinActivity activity, String pin) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().signInPin(SharPrefClass.getLoginInToken(activity), pin);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(@NonNull Call<DataLogIN> call, @NonNull Response<DataLogIN> response) {
                if (response.isSuccessful()){
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataLogIN.getStatus().equals(getString(R.string.success))){
                        SharPrefClass.setSigninTkn(activity, dataLogIN.getData().getToken());
                        SharPrefClass.setSigninSuccess(activity, true);
                        Intent intent = new Intent(activity, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        cB1.setChecked(false);
                        cB2.setChecked(false);
                        cB3.setChecked(false);
                        cB4.setChecked(false);
                        mVibrator.vibrate(500);
                        mShake = AnimationUtils.loadAnimation(activity, R.anim.shake);
                        cBLay.startAnimation(mShake);
                    }
                //    Toast.makeText(activity, dataLogIN.getMessage()+"44444", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);

                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<DataLogIN> call, @NonNull Throwable t) {
                System.out.println("security pin Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
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

    public void resetPin(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().forgotPin(SharPrefClass.getRegistrationObject(SPinActivity.this,SharPrefClass.KEY_PHONE_NUMBER));
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()) {
                    DataMain data = response.body();
                    Toast.makeText(SPinActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                    if (data.getStatus().equals("success")) {
                        Intent intent = new Intent(SPinActivity.this, OTPActivity.class);
                        intent.putExtra(getString(R.string.verification), 400);
                        intent.putExtra(getString(R.string.phone_number),SharPrefClass.getRegistrationObject(SPinActivity.this,SharPrefClass.KEY_PHONE_NUMBER));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } else
                    Toast.makeText(SPinActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(SPinActivity.this, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

    private void getAppInfoMethod(SPinActivity activity) {
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
}