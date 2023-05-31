package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.PurseAdapter;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.responseclass.DataWalletHistory;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeOutActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private ProgressBar progressBar;
    private PurseAdapter purseAdapter;
    private List<DataWalletHistory.Data.Statement> modelWalletArrayList = new ArrayList<>();
    private ShapeableImageView emptyIV;
    private MenuItem coin, purse;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private int currentCoins = 0;
    private TextInputEditText inWithdCoins;
    private int points;
    private MaterialTextView sPayMethod;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_out);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        loadData();
        toolbarMethod();
        withdSMethod(TakeOutActivity.this);
    }

    private void loadData() {
        Utility utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        coin.setEnabled(false);
        purse.setEnabled(false);
        coin.setVisible(true);
        coin.setTitle(SharPrefClass.getCustomerCoins(TakeOutActivity.this));
        SpannableString s = new SpannableString(coin.getTitle());
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        coin.setTitle(s);

    }
    private void withdSMethod(TakeOutActivity activity) {
        swipeRefreshLayout.setRefreshing(true);
        Call<DataWalletHistory> call = ApiClass.getClient().withdSatment(SharPrefClass.getLoginInToken(this),"");
        call.enqueue(new Callback<DataWalletHistory>() {
            @Override
            public void onResponse(Call<DataWalletHistory> call, Response<DataWalletHistory> response) {
                if (response.isSuccessful()) {
                    DataWalletHistory dataWalletHistory = response.body();
                    if (dataWalletHistory.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataWalletHistory.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataWalletHistory.getStatus().equalsIgnoreCase(getString(R.string.success))) {

                        modelWalletArrayList = dataWalletHistory.getData().getStatement();
                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                        recyclerView.setLayoutManager(layoutManager);
                        purseAdapter = new PurseAdapter(activity, modelWalletArrayList);
                        recyclerView.setAdapter(purseAdapter);

                        if (!modelWalletArrayList.isEmpty()) {
                            emptyIV.setVisibility(View.GONE);
                        } else {
                            emptyIV.setVisibility(View.VISIBLE);
                        }
                    }
                    Toast.makeText(activity, dataWalletHistory.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);


            }

            @Override
            public void onFailure(Call<DataWalletHistory> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                System.out.println("walletStatement error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toolbarMethod() {

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                withdSMethod(TakeOutActivity.this);
            }
        });

    }

    public void btnWithdCoins(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String s = inWithdCoins.getText().toString();
        if (s.length()>0){
            points = Integer.parseInt(s);
        }
        if (TextUtils.isEmpty(inWithdCoins.getText().toString())){
            Snackbar.make(view, getString(R.string.enter_points), 2000).show();
            return;
        }
        if (points<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_EXTRACT_COINS))){
            Snackbar.make(view, getString(R.string.minimum_points_must_be_greater_then_)+" "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_EXTRACT_COINS),2000).show();
            return;
        }
        if (points>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_EXTRACT_COINS))){
            Snackbar.make(view, getString(R.string.maximum_points_must_be_less_then_)+" "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_EXTRACT_COINS), 2000).show();
            return;
        }
        if (sPayMethod.getText().toString().equals(getString(R.string.select_withdraw_method))){
            Snackbar.make(view, getString(R.string.please_select_payment_method), 2000).show();
            return;
        }
        if (YourService.isOnline(this))
        withdCoinsM(TakeOutActivity.this, inWithdCoins.getText().toString(), sPayMethod.getText().toString());
        else
        Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }

    private void withdCoinsM(TakeOutActivity activity, String points, String method) {
        progressBar.setVisibility(View.VISIBLE);
        String methodStr = null;
        if (method.contains("Account number")){
            methodStr = "bank_name";
        }else if (method.contains("PayTM")){
            methodStr = "paytm_mobile_no";
        }else if (method.contains("PhonePe")){
            methodStr = "phonepe_mobile_no";
        }else if (method.contains("GooglePay")){
            methodStr = "gpay_mobile_no";
        }
        Call<DataMain> call = ApiClass.getClient().RetrieveAmnt(SharPrefClass.getLoginInToken(activity), points, methodStr);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(Call<DataMain> call, Response<DataMain> response) {
                if (response.isSuccessful()){
                    DataMain dataMain = response.body();
                    assert dataMain != null;
                    if (dataMain.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataMain.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        currentCoins = Integer.parseInt(SharPrefClass.getCustomerCoins(activity))-Integer.parseInt(points);
                        coin.setTitle(String.valueOf(currentCoins));
                        setToolBarTitle(currentCoins);
                        SharPrefClass.setUserCoins(TakeOutActivity.this, String.valueOf(currentCoins));
                        withdSMethod(activity);
                    }
                    Toast.makeText(TakeOutActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(TakeOutActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("withdrawPoints Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setToolBarTitle(int i) {
        SpannableString s = new SpannableString(String.valueOf(i));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        coin.setTitle(s);
    }
    private PopupWindow popupWindow;
    private MaterialTextView bInfo, pUpi, pPeUpi, gPUpi;
    public void sPayMethod(View view) {
        View popupView = LayoutInflater.from(this).inflate(R.layout.select_pay_method_popup, null);
        bInfo = popupView.findViewById(R.id.bankDetails);
        pPeUpi = popupView.findViewById(R.id.phonePeUpi);
        gPUpi = popupView.findViewById(R.id.googlePayUpi);
        pUpi = popupView.findViewById(R.id.paytmUpi);
        popupWindow = new PopupWindow(popupView, 900, LinearLayout.LayoutParams.WRAP_CONTENT, true );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(20);
        }
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(view,0,-135);


        if (SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N)!=null && !SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N).equals("")){
            bInfo.setText("Account number ( "+ SharPrefClass.getBankObject(this, SharPrefClass.KEY_B_AC_N)+" )");
            bInfo.setVisibility(View.VISIBLE);
        }
        if (SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_P_UPI_ID)!=null && !SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_P_UPI_ID).equals("")){
            pUpi.setVisibility(View.VISIBLE);
            pUpi.setText("PayTM ( "+ SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_P_UPI_ID)+" )");
        }
        if (SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_PP_UPI_ID)!=null && !SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_PP_UPI_ID).equals("")){
            pPeUpi.setVisibility(View.VISIBLE);
            pPeUpi.setText("PhonePe ( "+ SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_PP_UPI_ID)+" )");
        }
        if (SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_G_PAY_UPI_ID)!=null && !SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_G_PAY_UPI_ID).equals("")){
            gPUpi.setVisibility(View.VISIBLE);
            gPUpi.setText("GooglePay ( "+ SharPrefClass.getPrfrnceinfo(this, SharPrefClass.KEY_G_PAY_UPI_ID)+" )");
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
    public void googlepayUpi(View view) {
        sPayMethod.setText(gPUpi.getText().toString());
        popupWindow.dismiss();
    }


    public void payTmUpi(View view) {
        sPayMethod.setText(pUpi.getText().toString());
        popupWindow.dismiss();
    }

    public void bankDeails(View view) {
        sPayMethod.setText(bInfo.getText().toString());
        popupWindow.dismiss();
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
    private void intVariables() {
        sPayMethod = findViewById(R.id.selectPayMethod);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
        inWithdCoins = findViewById(R.id.inputWithdrawPoints);
        swipeRefreshLayout = findViewById(R.id.swipe_ref_lyt);
        recyclerView = findViewById(R.id.recyclerView);
        mDataConText = findViewById(R.id.dataConText);
        purse = toolbar.getMenu().findItem(R.id.purse);
        emptyIV = findViewById(R.id.emptyImage);
        coin = toolbar.getMenu().findItem(R.id.coins);
    }
    public void phonePeUpi(View view) {
        sPayMethod.setText(pPeUpi.getText().toString());
        popupWindow.dismiss();
    }
}