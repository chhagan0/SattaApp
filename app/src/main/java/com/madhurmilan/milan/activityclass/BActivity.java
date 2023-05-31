package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BActivity extends AppCompatActivity {

    private MaterialToolbar mToolbar;
    private TextInputEditText mName, mAcNumber, mConfAcNumber, mIfscCode, mBankName, mBankAddress;
    private ProgressBar mProgressBar;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    Utility utility;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        confiToolbar();
        confiData();
    }

    private void confiData() {

        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_BANK_USER_NAME)!=null)
            mName.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_BANK_USER_NAME));else mName.setText("");
        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N)!=null) mAcNumber.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N));else mAcNumber.setText("");
        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N)!=null)
            mConfAcNumber.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N));else mConfAcNumber.setText("");
        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_IFSC_C)!=null) mIfscCode.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_IFSC_C));else mIfscCode.setText("");
        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_N)!=null) mBankName.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_N));else mBankName.setText("");
        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_BRANCH_ADDRESS)!=null)
            mBankAddress.setText(SharPrefClass.getBankObject(this, SharPrefClass.KEY_BRANCH_ADDRESS));else mBankAddress.setText("");

        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }


    private void confiToolbar() {
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public void submitInfo(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(mName.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_bank_account_holder_name), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mAcNumber.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_bank_account_number), 2000).show();
            return;
        }
        if (mAcNumber.getText().toString().length()<5){
            Snackbar.make(view, getString(R.string.enter_valid_bank_account_number), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mConfAcNumber.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_conform_bank_account_number), 2000).show();
            return;
        }
        if (!mAcNumber.getText().toString().equals(mConfAcNumber.getText().toString())){
            Snackbar.make(view, getString(R.string.account_number_not_matching), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mIfscCode.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_bank_ifsc_code), 2000).show();
            return;
        }
        if (mIfscCode.getText().toString().length()<11){
            Snackbar.make(view, getString(R.string.please_enter_valid_ifsc_code_of_your_bank), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mBankName.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_bank_name), 2000).show();
            return;
        }
        if (TextUtils.isEmpty(mBankAddress.getText().toString())){
            Snackbar.make(view, getString(R.string.please_enter_your_branch_address), 2000).show();
            return;
        }

        if (YourService.isOnline(this))
            bankInfoMethod(BActivity.this, mName.getText().toString(), mAcNumber.getText().toString(), mIfscCode.getText().toString(), mBankName.getText().toString(), mBankAddress.getText().toString());
        else Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
    }
    private void bankInfoMethod(BActivity activity, String holderName, String accountNumber, String ifscCode, String bankName, String bankAddress) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().upgradeBInfo(SharPrefClass.getLoginInToken(this), holderName,accountNumber,ifscCode,bankName,bankAddress);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()){
                    DataMain dataMain = response.body();
                    if (dataMain.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataMain.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_BANK_USER_NAME, holderName);
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_AC_N, accountNumber);
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_IFSC_C, ifscCode);
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_N, bankName);
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_BRANCH_ADDRESS,bankAddress);
                        onBackPressed();
                    }else
                        Toast.makeText(activity, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressBar.setVisibility(View.GONE);
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                System.out.println("updateBankDetails error "+t);
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

    private void intVariables() {
        mToolbar = findViewById(R.id.toolbar);
        mName = findViewById(R.id.name);
        mAcNumber = findViewById(R.id.acNumber);
        mConfAcNumber = findViewById(R.id.confAcNum);
        mIfscCode = findViewById(R.id.ifsc_code);
        mBankName = findViewById(R.id.bank_name);
        mBankAddress = findViewById(R.id.bank_address);
        mProgressBar = findViewById(R.id.progressBar);
        mDataConText = findViewById(R.id.dataConText);
    }

}