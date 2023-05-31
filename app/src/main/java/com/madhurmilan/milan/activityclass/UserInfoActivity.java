package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.fragment.DashboardFragment.user_name;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
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

public class UserInfoActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TextView materialButton;
    private ProgressBar progressBar;
    private TextInputEditText editTextName, editTextNumbar, editTextEmail;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        intVariables();
        loadData();
        toolbarMethod();
    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        editTextName.setText(SharPrefClass.getRegistrationObject(this, SharPrefClass.KEY_USER_NAME));
        editTextNumbar.setText(SharPrefClass.getRegistrationObject(this, SharPrefClass.KEY_PHONE_NUMBER));
        editTextEmail.setText(SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_CLIENT_EMAIL));
        editTextName.setSelection(editTextName.getText().length());
    }


    private void intVariables() {
        toolbar = findViewById(R.id.toolbar);
        editTextName = findViewById(R.id.user_name);
        editTextEmail = findViewById(R.id.edit_txt_email);
        editTextNumbar = findViewById(R.id.phone_num);
        materialButton = findViewById(R.id.suBtn);
        progressBar = findViewById(R.id.progressBar);

        MaterialTextView mDataConText = findViewById(R.id.dataConText);
        Utility utility = new Utility(mDataConText);
    }
    private void toolbarMethod() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void editBtn(View view) {
        materialButton.setVisibility(View.VISIBLE);
        editTextName.setEnabled(true);
        editTextEmail.setEnabled(true);
        editTextName.requestFocus();
    }

    public void updatBtn(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(editTextName.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_name), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(editTextEmail.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_email), 2000).show();
            return;
        }
        if (!isValidEmail(editTextEmail.getText())){
            Snackbar.make(view, getString(R.string.please_enter_valid_email), 2000).show();
            return;
        }
        if (YourService.isOnline(this))
        updateInfoMethod(UserInfoActivity.this, editTextEmail.getText().toString(), editTextName.getText().toString());
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void updateInfoMethod(UserInfoActivity activity, String email, String name) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().UpgrdeUserInfo(SharPrefClass.getLoginInToken(activity),email,name);
        call.enqueue(new Callback<DataLogIN>() {
            @Override
            public void onResponse(Call<DataLogIN> call, Response<DataLogIN> response) {
                if (response.isSuccessful()){
                    DataLogIN dataLogIN = response.body();
                    if (dataLogIN.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataLogIN.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        materialButton.setVisibility(View.GONE);
                        editTextName.setEnabled(false);
                        editTextEmail.setEnabled(false);
                        DashboardActivity.userName.setText(name);
                        user_name.setText("Hello, "+ name);
                        SharPrefClass.setRegisterData(activity, SharPrefClass.KEY_USER_NAME, dataLogIN.getData().getUsername());
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_CLIENT_EMAIL, dataLogIN.getData().getEmail());
                    }
                    Toast.makeText(UserInfoActivity.this, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                }else
                Toast.makeText(UserInfoActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                System.out.println("updateProfile error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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