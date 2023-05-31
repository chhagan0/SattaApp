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
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPassActivity extends AppCompatActivity {

    private TextInputEditText mINewPass, mIConfPass;
    private ShapeableImageView mBackIcon, mPassToggle, mPassToggleConf;
    private ProgressBar mProgressBar;
    private IntentFilter mIntentFilter;
    private int changePassword=0;
    private String[] mMNumber = null;
    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pass);
        intVariables();
        loadData();
    }

    private void loadData() {
        if (changePassword==1){
            mBackIcon.setVisibility(View.VISIBLE);
        }else {
            mBackIcon.setVisibility(View.GONE);
        }
        MaterialTextView mDataConText = findViewById(R.id.dataConText);
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    private void intVariables() {
        mBackIcon = findViewById(R.id.backIcon);
        mINewPass = findViewById(R.id.inputNewPass);
        mPassToggle = findViewById(R.id.pass_toggle);
        mIConfPass = findViewById(R.id.inputConformPass);
        mPassToggleConf = findViewById(R.id.passToggleEyeConf);
        mProgressBar = findViewById(R.id.progressBar);
        changePassword = getIntent().getIntExtra(getString(R.string.changePassword), 0);
        mMNumber = getIntent().getStringArrayExtra(getString(R.string.phone_number));

    }

    public void GoChangePassBtn(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(mINewPass.getText().toString())){
            Snackbar.make(view,getString( R.string.please_enter_your_new_password), 2000).show();
            return;
        }
        if (mINewPass.getText().toString().length()<4){
            Snackbar.make(view, getString(R.string.new_password_must_be_of_at_least_4_characters_length), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mIConfPass.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_conform_password), 2000).show();
            return;
        }
        if (!mIConfPass.getText().toString().trim().equals(mINewPass.getText().toString().trim())){
            Snackbar.make(view, getString(R.string.password_not_matchin), 2000).show();
            return;
        }
        if (YourService.isOnline(this))
        changePassMethod(mMNumber[0], mMNumber[1], mIConfPass.getText().toString());
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void changePassMethod(String mobile, String mobileToken, String inputConformPass) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().forgotPassVerify(mobileToken,mobile,"phone_token",inputConformPass);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                DataLogIN dataLogIN = response.body();
                if (response.isSuccessful()) {
                    if (dataLogIN.getStatus().equals("success")) {
                        if (dataLogIN.getCode().equalsIgnoreCase("505")) {
                            SharPrefClass.setCleaninfo(NewPassActivity.this);
                            Toast.makeText(NewPassActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NewPassActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        if (changePassword == 1) {
                            SharPrefClass.setSigninTkn(NewPassActivity.this, dataLogIN.getData().getToken());
                            Intent intent = new Intent(NewPassActivity.this, DashboardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                        if (changePassword == 2) {
                            SharPrefClass.setSigninTkn(NewPassActivity.this, dataLogIN.getData().getToken());
                            Intent intent = new Intent(NewPassActivity.this, SignInActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    }
                    Toast.makeText(NewPassActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(NewPassActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                System.out.println("forgotPasswordVerify "+t);
                Toast.makeText(NewPassActivity.this, "An error has occurred", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);

            }
        });
    }

    public void passToggleConf(View view) {
        if (mIConfPass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            mIConfPass.setTransformationMethod(new SingleLineTransformationMethod());
            mPassToggleConf.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            mIConfPass.setTransformationMethod(new PasswordTransformationMethod());
            mPassToggleConf.setImageResource(R.drawable.ic_baseline_visibility_24);
        }
        mIConfPass.setSelection(mIConfPass.getText().length());
    }
    public void passToggle(View view) {
        if (mINewPass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            mINewPass.setTransformationMethod(new SingleLineTransformationMethod());
            mPassToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            mINewPass.setTransformationMethod(new PasswordTransformationMethod());
            mPassToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
        }

        mINewPass.setSelection(mINewPass.getText().length());
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
    public void backBtn(View view) {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
}