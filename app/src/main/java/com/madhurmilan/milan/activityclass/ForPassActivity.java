package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForPassActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextInputEditText phone_num;
    private IntentFilter mIntentFilter;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_pass);
        intVariables();
    }

    private void intVariables() {
        progressBar = findViewById(R.id.progressBar);
        MaterialTextView mDataConText = findViewById(R.id.dataConText);
        phone_num = findViewById(R.id.phone_num);
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
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

    public void sendOTP(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(phone_num.getText().toString())){
            Snackbar.make(view, "Please Enter Mobile Number",2000).show();
            return;
        }
        if (phone_num.getText().toString().length()<10){
            Snackbar.make(view, "Please Enter a valid Mobile Number",2000).show();
            return;
        }
        if (YourService.isOnline(this)){
            forgotPassword(this , phone_num.getText().toString().trim());
        }
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();

    }
    private void forgotPassword(ForPassActivity forPassActivity, String mobileNumber) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().forgotPassword(mobileNumber);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()) {
                    DataMain data = response.body();
                    if (data.getStatus().equals("success")) {
                        Intent intent = new Intent(forPassActivity, OTPActivity.class);
                        intent.putExtra(getString(R.string.verification), 300);
                        intent.putExtra(getString(R.string.phone_number),mobileNumber);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    Toast.makeText(forPassActivity, data.getMessage(), Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(forPassActivity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("verifyUser " + t);
                Toast.makeText(forPassActivity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}