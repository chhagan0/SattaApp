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
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextInputEditText inNum, inPass;
    private ShapeableImageView pToggle;
    private ProgressBar progressBar;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        intVariables();
        loadData();

    }

    private void loadData() {
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    public void SignInBtn(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String number = inNum.getText().toString();
        String password = inPass.getText().toString();
        if (TextUtils.isEmpty(number)){
            Snackbar.make(view, getString(R.string.please_enter_mobile_number),2000).show();
            return;
        }
        if (number.length()!=10){
            Snackbar.make(view, getString(R.string.please_enter_valid_mobile_number),2000).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Snackbar.make(view, getString(R.string.please_enter_password),2000).show();
            return;
        }
        if (password.length()<4){
            Snackbar.make(view, getString(R.string.please_enter_min_4_digits_password),2000).show();
            return;
        }
        if (YourService.isOnline(this))
        signInMethod(number,password);
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();


    }


    private void signInMethod(String number, String password) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().getSignIn(number, password);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(@NonNull Call<DataLogIN> call, @NonNull Response<DataLogIN> response) {
                if (response.isSuccessful()){
                    DataLogIN dataLogIN = response.body();
                    assert dataLogIN != null;
                    if (dataLogIN.getStatus().equals("success")){
                        SharPrefClass.setSigninTkn(SignInActivity.this, dataLogIN.getData().getToken());
                        Intent intent = new Intent(SignInActivity.this, SPinActivity.class);
                        intent.putExtra(getString(R.string.phone_number), number);
                        intent.putExtra(getString(R.string.pin_reset), 200);
                        startActivity(intent);
                    }else{
                        Toast.makeText(SignInActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(SignInActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.on_api_failure), Toast.LENGTH_LONG).show();
                System.out.println("getSignUp OnFailure "+t);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void passToggle(View view) {
        if (inPass.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
            inPass.setTransformationMethod(new SingleLineTransformationMethod());
            pToggle.setImageResource(R.drawable.ic_baseline_visibility_off_24);
        }
        else {
            inPass.setTransformationMethod(new PasswordTransformationMethod());
            pToggle.setImageResource(R.drawable.ic_baseline_visibility_24);
        }

        inPass.setSelection(inPass.getText().length());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(myReceiver, mIntentFilter);
    }
    public void forPass(View view) {
        Intent intent = new Intent(this, ForPassActivity.class);
        if (!TextUtils.isEmpty(inNum.getText().toString())){
            intent.putExtra(getString(R.string.phone_number), inNum.getText().toString());
        }
        startActivity(intent);
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
    private void intVariables() {
        inNum = findViewById(R.id.phone_num);
        inPass = findViewById(R.id.in_pass);
        pToggle = findViewById(R.id.pass_toggle);
        progressBar = findViewById(R.id.progressBar);
        mDataConText = findViewById(R.id.dataConText);
    }

    public void registerClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }


}