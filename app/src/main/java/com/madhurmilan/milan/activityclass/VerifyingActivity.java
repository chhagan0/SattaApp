package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyingActivity extends AppCompatActivity {

    private TextInputEditText mInPass, mInPC;
    private RelativeLayout rl_pass,rl_pin;
    private ShapeableImageView passToggle, passTogglePin;
    private MaterialTextView mtv_activity;
    private String mobileNumber;
    private int code = 300;
    private ProgressBar progressBar;
    private IntentFilter mIntentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifying);
        intVariables();
        loadData();
    }

    private void loadData() {
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
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
    private void intVariables() {

        mInPC = findViewById(R.id.in_pc);
        passTogglePin = findViewById(R.id.pass_tpc);
        mInPass = findViewById(R.id.in_pass);
        passToggle = findViewById(R.id.pass_toggle);
        mtv_activity = findViewById(R.id.mtv_activity);
        progressBar = findViewById(R.id.progressBar);
        rl_pin = findViewById(R.id.rl_pin);
        rl_pass = findViewById(R.id.rl_pass);
        mobileNumber = getIntent().getStringExtra(getString(R.string.phone_number));
        code = getIntent().getIntExtra(getString(R.string.verification),300);
        MaterialTextView dataConText = findViewById(R.id.dataConText);
        Utility utility = new Utility(dataConText);
        mIntentFilter = new IntentFilter();
        switch (code){
            case 300:
                rl_pass.setVisibility(View.VISIBLE);
                rl_pin.setVisibility(View.GONE);
                mtv_activity.setText("Forgot Password");
                break;
            case 400:
                rl_pass.setVisibility(View.GONE);
                rl_pin.setVisibility(View.VISIBLE);
                mtv_activity.setText("Forgot Pin");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
    public void passTogglePin(View view) {
        if (mInPC.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            mInPC.setTransformationMethod(new SingleLineTransformationMethod());
            passTogglePin.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            mInPC.setTransformationMethod(new PasswordTransformationMethod());
            passTogglePin.setImageResource(R.drawable.ic_baseline_visibility_24);
        }
        mInPC.setSelection(mInPC.getText().length());
    }

    public void passToggle(View view) {
        if (mInPass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            mInPass.setTransformationMethod(new SingleLineTransformationMethod());
            passToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            mInPass.setTransformationMethod(new PasswordTransformationMethod());
            passToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
        }

        mInPass.setSelection(mInPass.getText().length());
    }

    public void submit(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if(code==300){
            if (TextUtils.isEmpty(mInPass.getText().toString())){
                Snackbar.make(view, getString(R.string.please_enter_password),2000).show();
                return;
            }
            if (mInPass.getText().toString().length()<4){
                Snackbar.make(view, getString(R.string.please_enter_min_4_digits_password),2000).show();
                return;
            }
            if (YourService.isOnline(this)) createPassword(SharPrefClass.getLoginInToken(this),mobileNumber,mInPass.getText().toString().trim(),"mobile_token");
            else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();

        }else{
            if (TextUtils.isEmpty(mInPC.getText().toString())){
                Snackbar.make(view, getString(R.string.please_enter_pin),2000).show();
                return;
            }
            if (mInPC.getText().toString().length()<4){
                Snackbar.make(view, getString(R.string.please_enter_min_4_digit_pin),2000).show();
                return;
            }
            if (YourService.isOnline(this)) createPin(SharPrefClass.getLoginInToken(this),mobileNumber,mInPC.getText().toString().trim(),"mobile_token");
            else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();

        }

    }

    private void createPin(String loginInToken, String mobileNumber, String pin, String mobile_token) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().newPin(loginInToken,mobileNumber, "firebase_token",pin);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                if (response.isSuccessful()) {
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getStatus().equals("success")) {
                        SharPrefClass.setSigninTkn(VerifyingActivity.this, dataLogIN.getData().getToken());
                        SharPrefClass.setSigninSuccess(VerifyingActivity.this, true);
                        Intent intent = new Intent(VerifyingActivity.this, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(VerifyingActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(VerifyingActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(VerifyingActivity.this, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void createPassword(String loginInToken, String mobileNumber, String pass, String mobile_token) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().newPassword(loginInToken, mobileNumber,"firebase_token",pass);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                if (response.isSuccessful()) {
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getStatus().equals("success")) {
                        SharPrefClass.setSigninTkn(VerifyingActivity.this, dataLogIN.getData().getToken());
                        SharPrefClass.setSigninSuccess(VerifyingActivity.this, true);
                        Intent intent = new Intent(VerifyingActivity.this, SPinActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(VerifyingActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(VerifyingActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(VerifyingActivity.this, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}