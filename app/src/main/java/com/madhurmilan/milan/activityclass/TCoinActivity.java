package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.responseclass.DataVerifyTransferCoin;
import com.madhurmilan.milan.shareprefclass.MyBSTransferCoins;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TCoinActivity extends AppCompatActivity {

    private TextInputEditText inputCoins, uNumb;
    private int coins=0, ava_coins =0;
    private InputMethodManager inputMethodManager;
    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private MenuItem itemCoins;
    private MaterialTextView coinText, userN;
    private MaterialButton subBtn, veBtn;
    private MaterialTextView dataConText;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t_coin);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariabless();
        loadData();
        ToolbarMethod();
    }

    private void loadData() {
        Utility utility = new Utility(dataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);

        itemCoins.setVisible(true);
        ava_coins = Integer.parseInt(SharPrefClass.getCustomerCoins(TCoinActivity.this));
        setToolBar(ava_coins);
        veBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
    }
    private void ToolbarMethod() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        uNumb.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<10){
                    veBtn.setEnabled(false);
                    veBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    coinText.setVisibility(View.GONE);
                    inputCoins.setVisibility(View.GONE);
                    subBtn.setVisibility(View.GONE);
                    userN.setVisibility(View.GONE);

                }else {
                    veBtn.setVisibility(View.VISIBLE);
                    veBtn.setEnabled(true);
                    veBtn.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor( TCoinActivity.this, R.color.green)));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void submitCoins(View view) {
        inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String s = inputCoins.getText().toString();
        if (s.length()>0){
            coins = Integer.parseInt(s);
        }
        if (TextUtils.isEmpty(s) || coins<1){
            Snackbar.make(view, getString(R.string.please_enter_points), 2000).show();
            return;
        }
        if (coins<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_TRANSMIT_COINS))){
            Snackbar.make(view,getString(R.string.minimum_points_must_be_greater_then_)+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_TRANSMIT_COINS), 2000).show();
            return;
        }
        if (coins>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_TRANSMIT_COINS))){
            Snackbar.make(view, getString( R.string.maximum_points_must_be_less_then_)+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_TRANSMIT_COINS), 2000).show();
            return;
        }
        if (coins> ava_coins){
            Snackbar.make(view, "Insufficient Points", 2000).show();
            return;
        }
        if (YourService.isOnline(this))
        transferDialog();
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();

    }

    private void transferDialog() {
        MyBSTransferCoins myBSTransferCoins = new MyBSTransferCoins(new MyBSTransferCoins.OnConformClick() {
            @Override
            public void onConformClick() {
                transferCoints(TCoinActivity.this);
            }
        });

        myBSTransferCoins.show(getSupportFragmentManager(),getString(R.string.bottom_sheet));
        myBSTransferCoins.setCancelable(false);
    }

    private void transferCoints(TCoinActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().transmitCoins(SharPrefClass.getLoginInToken(activity), inputCoins.getText().toString(), uNumb.getText().toString());
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
                        inputCoins.setText("");
                        uNumb.setText("");
                        SharPrefClass.setUserCoins(activity,String.valueOf(ava_coins -coins));
                        setToolBar(ava_coins -coins);
                        veBtn.setVisibility(View.VISIBLE);

                    }
                    Toast.makeText(TCoinActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("transferPoints Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setToolBar(int i) {
        SpannableString s = new SpannableString(String.valueOf(i));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        itemCoins.setTitle(s);
    }

    private void verifyMethod(TCoinActivity activity) {
        progressBar.setVisibility(View.VISIBLE);
        Call<DataVerifyTransferCoin> call = ApiClass.getClient().transmitVerify(SharPrefClass.getLoginInToken(activity), uNumb.getText().toString());
        call.enqueue(new Callback<DataVerifyTransferCoin>() {
            @Override
            public void onResponse(Call<DataVerifyTransferCoin> call, Response<DataVerifyTransferCoin> response) {
                if (response.isSuccessful()){
                    DataVerifyTransferCoin verifyModel = response.body();
                    if (verifyModel.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, verifyModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (verifyModel.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        coinText.setVisibility(View.VISIBLE);
                        inputCoins.setVisibility(View.VISIBLE);
                        subBtn.setVisibility(View.VISIBLE);
                        veBtn.setVisibility(View.GONE);
                        userN.setText(verifyModel.getData().getName());
                        userN.setVisibility(View.VISIBLE);
                        inputCoins.requestFocus();
                    }
                    Toast.makeText(TCoinActivity.this, verifyModel.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataVerifyTransferCoin> call, Throwable t) {
                System.out.println("transferVerify Error "+t);
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
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }

    private void intVariabless() {
        toolbar = findViewById(R.id.toolbar);
        inputCoins = findViewById(R.id.inputCoins);
        progressBar = findViewById(R.id.progressBar);
        uNumb = findViewById(R.id.user_num);
        veBtn = findViewById(R.id.verifyText);
        coinText = findViewById(R.id.coin_txt);
        userN = findViewById(R.id.userN);
        subBtn = findViewById(R.id.suBtn);
        dataConText = findViewById(R.id.dataConText);
        itemCoins = toolbar.getMenu().findItem(R.id.coins);
    }
    public void btnVerify(View view) {
        inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (YourService.isOnline(this))
            verifyMethod(TCoinActivity.this);
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

}