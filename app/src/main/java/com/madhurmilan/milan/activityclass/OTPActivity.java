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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OTPActivity extends AppCompatActivity {

    private TextInputEditText mInPC,phone_num;
    private ShapeableImageView passTogglePin;
    private ProgressBar progressBar;
    private MaterialTextView dataConText,mtv_activity;
    private IntentFilter mIntentFilter;
    int code = 200;
    String mobileNumber = "";
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        intVariables();
        loadData();
    }

    private void loadData() {
        utility = new Utility(dataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    private void intVariables() {
        mInPC = findViewById(R.id.in_pc);
        phone_num = findViewById(R.id.phone_num);
        passTogglePin = findViewById(R.id.pass_tpc);
        progressBar = findViewById(R.id.progressBar);
        dataConText = findViewById(R.id.dataConText);
        mtv_activity = findViewById(R.id.mtv_activity);
        code = getIntent().getIntExtra(getString(R.string.verification),200);
        mobileNumber = getIntent().getStringExtra(getString(R.string.phone_number));
        phone_num.setText(mobileNumber);
        switch (code){
            case 200:
                mtv_activity.setText("Registration");
                break;
            case 300:
                mtv_activity.setText("Forgot Password");
                break;
            case 400:
                mtv_activity.setText("Forgot Pin");
                break;
        }
    }

    public void verifyOtp(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(mInPC.getText().toString())){
            Snackbar.make(view, "Please Enter OTP",2000).show();
            return;
        }
        if (mInPC.getText().toString().length()<4){
            Snackbar.make(view, "Please Enter a valid OTP",2000).show();
            return;
        }
        if (YourService.isOnline(this)){
            switch (code){
                case 200:
                    verifyUserMethod(this ,SharPrefClass.getRegistrationObject(OTPActivity.this,SharPrefClass.KEY_PHONE_NUMBER), "mobile_token", mInPC.getText().toString().trim());
                    break;
                case 300:
                case 400:
                    verifyOTP(this,mobileNumber,mInPC.getText().toString().trim());
                    break;
            }
        }
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void verifyOTP(OTPActivity otpActivity, String mobileNumber, String otp) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().verifyOtp(mobileNumber,otp);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                if (response.isSuccessful()) {
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getStatus().equals("success")) {
                        SharPrefClass.setSigninTkn(otpActivity, dataLogIN.getData().getToken());
                        Intent intent = new Intent(otpActivity, VerifyingActivity.class);
                        intent.putExtra(getString(R.string.verification), code);
                        intent.putExtra(getString(R.string.phone_number),mobileNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(otpActivity, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(otpActivity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(otpActivity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
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


    private void verifyUserMethod(OTPActivity otpActivity, String mobileNumber, String mobile_token, String otp) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().verifyCustomer(mobileNumber, mobile_token,otp);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                if (response.isSuccessful()) {
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getStatus().equals("success")) {
                        SharPrefClass.setSigninTkn(otpActivity, dataLogIN.getData().getToken());
                        SharPrefClass.setSigninSuccess(otpActivity, true);
                        Intent intent = new Intent(otpActivity, DashboardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    Toast.makeText(otpActivity, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(otpActivity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(otpActivity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


    public void resendOtp(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().resendOtp(mobileNumber);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()) {
                    DataMain modelSignIn = response.body();
                    Toast.makeText(OTPActivity.this, modelSignIn.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(OTPActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(OTPActivity.this, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}