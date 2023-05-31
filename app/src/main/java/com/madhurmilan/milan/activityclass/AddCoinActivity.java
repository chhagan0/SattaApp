package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import dev.shreyaspatil.easyupipayment.EasyUpiPayment;
import dev.shreyaspatil.easyupipayment.listener.PaymentStatusListener;
import dev.shreyaspatil.easyupipayment.model.PaymentApp;
import dev.shreyaspatil.easyupipayment.model.TransactionDetails;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCoinActivity extends AppCompatActivity implements PaymentStatusListener,PaymentResultWithDataListener {

    private static final String TAG = "AddCoin";
    private TextInputEditText mInputCoins;
    private int mCoins;
    String amountPaid = "0.0";
    private MaterialToolbar mToolbar;
    private ProgressBar mProgressBar;
    private MaterialTextView mUpiIDTxt;
    private PaymentApp mPayApp;
    private RadioGroup mRadioGroup;
    private IntentFilter mIntentFilter;
    Utility utility;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coin);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVeriables();
        confiToolbar();
    }
    private void confiToolbar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.GPay:
                        mPayApp = PaymentApp.GOOGLE_PAY;
                        break;
                    case R.id.phone_pe_btn:
                        mPayApp = PaymentApp.PHONE_PE;
                        break;
                    case R.id.payTmBtn:
                        mPayApp = PaymentApp.PAYTM;
                        break;
                    default:
                        mPayApp = PaymentApp.ALL;
                        break;
                }
            }
        });
    }

    private void payDialog() {
        String transactionId = "TID" + System.currentTimeMillis();
        String amount = mInputCoins.getText().toString()+".0";
        // START PAYMENT INITIALIZATION
        EasyUpiPayment.Builder easyBuilder = new EasyUpiPayment.Builder(this)
                .with(mPayApp)
                .setPayeeVpa(SharPrefClass.getAddAmountUpiId(this, SharPrefClass.KEY_ADD_COINS_BHIM_ID))
                .setPayeeName(SharPrefClass.getAddAmountUpiId(this, SharPrefClass.KEY_ADD_COINS_BHIM_NAME))
                .setTransactionId(transactionId)
                .setTransactionRefId(transactionId)
                .setPayeeMerchantCode("")
                .setDescription(getString(R.string.app_name))
                .setAmount(amount);

        // END INITIALIZATION
        try {
            // Build instance
            EasyUpiPayment  easyUpiPayment = easyBuilder.build();

            // Register Listener for Events
            easyUpiPayment.setPaymentStatusListener(this);

            // Start payment / transaction
            easyUpiPayment.startPayment();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Error "+exception.getMessage());

        }
    }

    private void addCoinMethod(AddCoinActivity activity, String amount, String orderID) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().AddCoins(SharPrefClass.getLoginInToken(activity), amount,"successful",orderID);
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
                        Toast.makeText(AddCoinActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                        mInputCoins.setText("");
                    }
                    Toast.makeText(AddCoinActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                System.out.println("addFund Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onTransactionCompleted(@NotNull TransactionDetails transactionDetails) {
        switch (transactionDetails.getTransactionStatus()) {
            case SUCCESS:
                onTransactionSuccess(transactionDetails.getAmount());
                break;
            case FAILURE:
                onTransactionFailed();
                break;
            case SUBMITTED:
                onTransactionSubmitted();
                break;
        }
    }

    @Override
    public void onTransactionCancelled() {
        // Payment Cancelled by User
        toast("Cancelled by user");
    }

    private void onTransactionSuccess(String amount) {
        // Payment Success
        toast("Success");
        addCoinMethod(this,amount, "paid with upi");
    }

    private void onTransactionSubmitted() {
        // Payment Pending
        toast("Pending | Submitted");
    }

    private void onTransactionFailed() {
        // Payment Failed
        toast("Failed");
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void UpiTxtCoppy(View view) {
        setClipboard(this, mUpiIDTxt.getText().toString());
    }
    private void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "UPI Copied", Toast.LENGTH_SHORT).show();
    }

    private void intVeriables() {
        mToolbar = findViewById(R.id.toolbar);
        mInputCoins = findViewById(R.id.inputCoins);
        mProgressBar = findViewById(R.id.progressBar);
        mUpiIDTxt = findViewById(R.id.upiI_id);
        mRadioGroup = findViewById(R.id.radioGroup);
        MaterialTextView mDataConText = findViewById(R.id.dataConText);
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        mUpiIDTxt.setText(SharPrefClass.getAddAmountUpiId(this, SharPrefClass.KEY_ADD_COINS_BHIM_ID));
        Checkout.preload(getApplicationContext());
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
    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        toast("successful");
        addCoinMethod(this,amountPaid,s);
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        System.out.println("RazorPay "+i+" "+s+" "+paymentData);
        toast("failed");
    }
    public void submitCoins(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        String mString = mInputCoins.getText().toString();
        if (mString.length()>0){
            mCoins = Integer.parseInt(mString);
        }
        if (TextUtils.isEmpty(mString)){
            Snackbar.make(view, getString(R.string.please_enter_points), 2000).show();
            return;
        }
        if (mCoins <Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_ADD_AMOUNT_COINS))){
            Snackbar.make(view,getString(R.string.minimum_points_must_be_greater_then_)+" "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_ADD_AMOUNT_COINS), 2000).show();
            return;
        }
        if (mCoins >Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_ADD_AMOUNT_COINS))){
            Snackbar.make(view, getString( R.string.maximum_points_must_be_less_then_)+" "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_ADD_AMOUNT_COINS), 2000).show();
            return;
        }
        if (mRadioGroup.getCheckedRadioButtonId()==-1){
            Snackbar.make(view,"Please Select Payment Method",2000).show();
            return;
        }

        if (YourService.isOnline(this)){
            if(mRadioGroup.getCheckedRadioButtonId()==R.id.razorPay) startPayment();
            else payDialog();
        }
        else Toast.makeText(this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
    }
    public void startPayment()
    {
        Checkout checkout = new Checkout();
        checkout.setKeyID(getString(R.string.razor_pay_key));
        checkout.setImage(R.drawable.logomatka);
        String desc = getString(R.string.app_name)+" "+ SharPrefClass.getRegistrationObject(this,SharPrefClass.KEY_PHONE_NUMBER)+"  " +System.currentTimeMillis();
        String amountRequested = mInputCoins.getText().toString()+"00";
        amountPaid = mInputCoins.getText().toString()+".0";
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", getString(R.string.app_name));
            options.put("description",desc );
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            //options.put("order_id", "order_DBJOWzybf0sJbb");
            //from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", amountRequested);
            //pass amount in currency subunits
            options.put("prefill.email", "userapk@example.com");
            options.put("prefill.contact","+91"+SharPrefClass.getRegistrationObject(this,SharPrefClass.KEY_PHONE_NUMBER));
            JSONObject retryObj = new JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }
}