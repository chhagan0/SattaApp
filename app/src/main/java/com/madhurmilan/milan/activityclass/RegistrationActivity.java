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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataApp;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText mInName, mIMobNum, mInPass, mInPC;
    private ShapeableImageView passToggle, passTogglePin;
    private ProgressBar progressBar;
    private MaterialTextView dataConText;
    private IntentFilter mIntentFilter;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
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
        mInName = findViewById(R.id.user_name);
        mIMobNum = findViewById(R.id.phone_num);
        mInPass = findViewById(R.id.in_pass);
        passToggle = findViewById(R.id.pass_toggle);
        mInPC = findViewById(R.id.in_pc);
        passTogglePin = findViewById(R.id.pass_tpc);
        progressBar = findViewById(R.id.progressBar);
        dataConText = findViewById(R.id.dataConText);
    }

    public void signUp(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(mInName.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_name),2000).show();
            return;
        }
        if (TextUtils.isEmpty(mIMobNum.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_mobile_number),2000).show();
            return;
        }
        if (mIMobNum.getText().toString().length()<10){
            Snackbar.make(view, getString(R.string.please_enter_valid_mobile_number),2000).show();
            return;
        }
        if (TextUtils.isEmpty(mInPass.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_password),2000).show();
            return;
        }
        if (mInPass.getText().toString().length()<4){
            Snackbar.make(view, getString(R.string.please_enter_min_4_digits_password),2000).show();
            return;
        }
        if (TextUtils.isEmpty(mInPC.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_pin),2000).show();
            return;
        }
        if (mInPC.getText().toString().length()<4){
            Snackbar.make(view, getString(R.string.please_enter_min_4_digit_pin),2000).show();
            return;
        }
        if (YourService.isOnline(this))
        SignUpMethod(this ,mInName.getText().toString().trim(), mIMobNum.getText().toString().trim(), mInPass.getText().toString().trim(), mInPC.getText().toString().trim());
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
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


    private void SignUpMethod(RegistrationActivity activity, String personName, String mobileNumber, String password, String pinCode) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().getRegisterMethod(personName,mobileNumber,pinCode,password);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {

                if (response.isSuccessful()){
                    DataMain dataMain = response.body();
                    assert dataMain != null;
                    if (dataMain.getStatus().equals("success")){
                        getAppInfoMethod(activity);
                        Intent intent = new Intent(RegistrationActivity.this, OTPActivity.class);
                        SharPrefClass.setRegisterData(RegistrationActivity.this, SharPrefClass.KEY_USER_NAME,personName);
                        SharPrefClass.setRegisterData(RegistrationActivity.this, SharPrefClass.KEY_PHONE_NUMBER,mobileNumber);
                        intent.putExtra(getString(R.string.verification), 200);
                        intent.putExtra(getString(R.string.phone_number),mobileNumber);
                        startActivity(intent);
                    }else
                    Toast.makeText(RegistrationActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();

                }
                else Toast.makeText(RegistrationActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.on_api_failure), Toast.LENGTH_LONG).show();
                System.out.println("getSignUp OnFailure "+t);
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

    public void AlreadyRegistered(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
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

    private void getAppInfoMethod(RegistrationActivity activity) {
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
                        SharPrefClass.setContactUsInfo(activity, SharPrefClass.KEY_PHONE_NUMBER2,  dataApp.getData().getContact_details().getTelegram_channel_link());
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