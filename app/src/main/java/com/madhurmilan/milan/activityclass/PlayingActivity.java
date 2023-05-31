package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
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
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.PlayingAdapter;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataMain;
import com.madhurmilan.milan.responseclass.DataPlaying;
import com.madhurmilan.milan.shareprefclass.MBSFragment;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayingActivity extends AppCompatActivity {

    private int mProceed =0;
    private int mTotalCoins = 0;
    private int mCurrentCoins = 0;
    private String mGameID;
    private MaterialToolbar mToolbar;
    private MaterialTextView mChooseDate, mChooseSes, mOpenCDigit, mDigText, mtvTotalCoins;
    private RadioGroup mRadioGroup;
    private MaterialAutoCompleteTextView mInputD, mInpCloseD;
    private LinearLayout llBidBottom;
    private TextInputEditText mInputCoins;
    private MaterialRadioButton mOpen, mClose;
    private MaterialButton btnProceed;
    private ArrayList<String> mNumbers, mNumbers2;
    private RecyclerView mRecyclerView;
    private final List<DataPlaying> dataPlayingList = new ArrayList<>();
    private PlayingAdapter playingAdapter;
    private MenuItem mCoins;
    private ProgressBar mProgressBar;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    Utility utility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        loadData();
        confToolbar();
        confRecycler();
    }

    private void loadData() {
        utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
        mProceed = getIntent().getIntExtra(getString(R.string.game_name), 0);
        mGameID = getIntent().getStringExtra("games");
        boolean isOpen = getIntent().getBooleanExtra("open", false);
        mCurrentCoins = Integer.parseInt(SharPrefClass.getCustomerCoins(PlayingActivity.this));
        mNumbers = new ArrayList<String>();
        mNumbers2 = new ArrayList<String>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        mChooseDate.setText(formattedDate);
        mOpen.setEnabled(isOpen);
        mClose.setChecked(!isOpen);
        setToolBarTitle(mCurrentCoins);

        if (mProceed ==6){
            mOpen.setChecked(true);
            mOpenCDigit.setVisibility(View.VISIBLE);
            mInpCloseD.setVisibility(View.VISIBLE);
            mDigText.setText(R.string.open_digit);
            mOpenCDigit.setText(R.string.close_pana);
        }
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (mProceed ==6) confToolbar();
            }
        });
    }


    private void intVariables() {
        mToolbar = findViewById(R.id.toolbar);
        mCoins = mToolbar.getMenu().findItem(R.id.coins);
        mCoins.setVisible(true);
        MenuItem mPurse = mToolbar.getMenu().findItem(R.id.purse);
        mPurse.setEnabled(false);
        mChooseDate = findViewById(R.id.choose_date);
        mChooseSes = findViewById(R.id.chooseSession);
        mRadioGroup = findViewById(R.id.radioGroup);
        mInputD = findViewById(R.id.input_d);
        mInputCoins = findViewById(R.id.inputCoins);
        mOpenCDigit = findViewById(R.id.openCD);
        mInpCloseD = findViewById(R.id.inputCD);
        mDigText = findViewById(R.id.digText);
        mtvTotalCoins = findViewById(R.id.mtv_total_coins);
        mOpen = findViewById(R.id.open);
        mClose = findViewById(R.id.close);
        MaterialButton mProConform = findViewById(R.id.pro_conf);
        btnProceed = findViewById(R.id.btn_proceed);
        mRecyclerView = findViewById(R.id.recyclerView);
        llBidBottom = findViewById(R.id.ll_bid_bottom);
        mProgressBar = findViewById(R.id.progressBar);
        mDataConText = findViewById(R.id.dataConText);

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedBtn(v);
            }
        });

        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void confToolbar() {
        mNumbers.clear();
        mNumbers2.clear();

        switch (mProceed){
            case 1:
                mToolbar.setTitle(getString(R.string.single_digit));
                for (int i = 0; i <= 9; i++) {
                    mNumbers.add(String.valueOf(i));
                }
                break;
            case 2:
                mToolbar.setTitle(getString(R.string.jodi_digit));
                mChooseSes.setVisibility(View.GONE);
                mRadioGroup.setVisibility(View.GONE);
                mOpen.setChecked(true);
                for (int i = 0; i <= 9; i++) {
                    for (int j = 0; j<=9; j++){
                        mNumbers.add(String.valueOf(i)+String.valueOf(j));
                    }
                }
                break;
            case 3:
                mToolbar.setTitle(getString(R.string.single_pana));
                for (int a =1 ; a<=9; a++){
                    for (int b = 1;b<=9;b++){
                        for (int c = 0;c<=9;c++){
                            if(a!=b && a!=c && b!=c){
                                if (a < b && b<c||c==0&& a<b){
                                    mNumbers.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                                }
                            }
                        }
                    }
                }
                break;
            case 4:
                mToolbar.setTitle(getString(R.string.double_pana));
                for (int a =1 ; a<=9; a++){
                    for (int b = 0;b<=9;b++){
                        for (int c = 0;c<=9;c++){
                            if(a == b && b < c || b == 0 && c == 0 || a == b && c == 0||a<b && b==c){
                                mNumbers.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                            }
                        }
                    }
                }
                break;
            case 5:
                mToolbar.setTitle(getString(R.string.triple_pana));
                for (int a =0 ; a<=9; a++){
                    for (int b = 0;b<=9;b++){
                        for (int c = 0;c<=9;c++){
                            if(a == b && b == c ){
                                mNumbers.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                            }
                        }
                    }
                }
                break;
            case 6:
                mToolbar.setTitle(getString(R.string.half_sangam));
                if (mOpen.isChecked()){
                    mDigText.setText(R.string.open_digit);
                    mOpenCDigit.setText(R.string.close_pana);
                    for (int a =0 ; a<=9; a++){
                        mNumbers.add(String.valueOf(a));
                    }
                    for (int a =0 ; a<=9; a++){
                        for (int b = 0;b<=9;b++){
                            for (int c = 0;c<=9;c++){
                                if(a > 0 && a <= b && b <= c || b == 0 && c == 0 || c == 0 && a <= b &&a!=0){
                                    mNumbers2.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                                }
                            }
                        }
                    }
                }else if (mClose.isChecked()){
                    mDigText.setText(R.string.open_pana);
                    mOpenCDigit.setText(R.string.close_digit);
                    for (int a =0 ; a<=9; a++){
                        for (int b = 0;b<=9;b++){
                            for (int c = 0;c<=9;c++){
                                if(a > 0 && a <= b && b <= c || b == 0 && c == 0 || c == 0 && a <= b &&a!=0){
                                    mNumbers.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                                }
                            }
                        }
                    }
                    for (int a =0 ; a<=9; a++){
                        mNumbers2.add(String.valueOf(a));
                    }
                }
                break;
            case 7:
                mChooseSes.setVisibility(View.GONE);
                mRadioGroup.setVisibility(View.GONE);
                mOpenCDigit.setVisibility(View.VISIBLE);
                mInpCloseD.setVisibility(View.VISIBLE);
                mDigText.setText(R.string.open_pana);
                mOpenCDigit.setText(R.string.close_pana);
                mToolbar.setTitle(getString(R.string.full_sangam));
                for (int a =0 ; a<=9; a++){
                    for (int b = 0;b<=9;b++){
                        for (int c = 0;c<=9;c++){
                            if(a > 0 && a <= b && b <= c || b == 0 && c == 0 || c == 0 && a <= b &&a!=0){
                                mNumbers.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                                mNumbers2.add(String.valueOf(a)+String.valueOf(b)+String.valueOf(c));
                            }
                        }
                    }
                }

                break;
        }

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, mNumbers);
        ArrayAdapter<String> mAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, mNumbers2);

        mInputD.setThreshold(1);//will start working from first character
        mInputD.setAdapter(mAdapter);//setting the adapter data into the AutoCompleteTextView

        mInpCloseD.setThreshold(1);
        mInpCloseD.setAdapter(mAdapter2);

        if (mProceed!=6){
            int maxLength = mNumbers.get(0).length() ;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            mInputD.setFilters(fArray);

            int maxLength2 = 3 ;
            InputFilter[] fArray2 = new InputFilter[1];
            fArray2[0] = new InputFilter.LengthFilter(maxLength2);
            mInpCloseD.setFilters(fArray2);
        }else {
            int maxLength = 3 ;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            mInputD.setFilters(fArray);
            mInpCloseD.setFilters(fArray);
        }
    }

    public void proceedBtn(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(mInputD.getText().toString())){
            snackbar(getString(R.string.please_enter_digits),view);
            return;
        }
        switch (mProceed){
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                if(!mOpen.isChecked() && !mClose.isChecked()){
                    snackbar("Please select session", view);
                    return;
                }
                if (!mNumbers.contains(mInputD.getText().toString())){
                    snackbar(getString(R.string.please_enter_valid_digits), view);
                    return;
                }
                if (TextUtils.isEmpty(mInputCoins.getText().toString().trim())){
                    snackbar(getString(R.string.please_enter_points),view);
                    return;
                }
                if (Integer.parseInt(mInputCoins.getText().toString().trim())<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM))
                        ||Integer.parseInt(mInputCoins.getText().toString().trim())>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM))){
                    snackbar("Minimum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM)+" and Maximum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM),view);
                    return;
                }
                setRecycleViewData(mProceed,view);
                break;
            case 6:
                if(!mOpen.isChecked() && !mClose.isChecked()){
                    snackbar("Please select session", view);
                    return;
                }
                if (mOpen.isChecked()){
                    if (!mNumbers.contains(mInputD.getText().toString())){
                        snackbar(getString(R.string.please_enter_valid_open_digits), view);
                        return;
                    }
                    if (TextUtils.isEmpty(mInpCloseD.getText().toString().trim())){
                        snackbar(getString(R.string.please_enter_close_pana), view);
                        return;
                    }
                    if (!mNumbers2.contains(mInpCloseD.getText().toString())){
                        snackbar(getString(R.string.please_enter_valid_close_pana), view);
                        return;
                    }
                }else {
                    if (!mNumbers.contains(mInputD.getText().toString())){
                        snackbar(getString(R.string.please_enter_valid_open_pana), view);
                        return;
                    }
                    if (TextUtils.isEmpty(mInpCloseD.getText().toString().trim())){
                        snackbar(getString(R.string.please_enter_close_digits), view);
                        return;
                    }
                    if (!mNumbers2.contains(mInpCloseD.getText().toString())){
                        snackbar(getString(R.string.please_enter_valid_close_digits), view);
                        return;
                    }
                }
                if (TextUtils.isEmpty(mInputCoins.getText().toString().trim())){
                    snackbar(getString(R.string.please_enter_points),view);
                    return;
                }
                if (Integer.parseInt(mInputCoins.getText().toString().trim())<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM))
                        ||Integer.parseInt(mInputCoins.getText().toString().trim())>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM))){
                    snackbar("Minimum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM)+" and Maximum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM),view);
                    return;
                }
                setRecycleViewData(mProceed,view);
                break;
            case 7:
                if (!mNumbers.contains(mInputD.getText().toString())){
                    snackbar(getString(R.string.please_enter_valid_open_pana), view);
                    return;
                }
                if (TextUtils.isEmpty(mInpCloseD.getText().toString().trim())){
                    snackbar(getString(R.string.please_enter_close_pana), view);
                    return;
                }
                if (!mNumbers2.contains(mInpCloseD.getText().toString())){
                    snackbar(getString(R.string.please_enter_valid_close_pana), view);
                    return;
                }
                if (TextUtils.isEmpty(mInputCoins.getText().toString().trim())){
                    snackbar(getString(R.string.please_enter_points),view);
                    return;
                }
                if (Integer.parseInt(mInputCoins.getText().toString().trim())<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM))
                        ||Integer.parseInt(mInputCoins.getText().toString().trim())>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM))){
                    snackbar("Minimum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM)+" and Maximum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM),view);
                    return;
                }
                setRecycleViewData(mProceed,view);
                break;
        }

    }

    private void setRecycleViewData(int gameProceed, View view) {
        String openNum = mInputD.getText().toString();
        String closeNum = mInpCloseD.getText().toString();
        String points = mInputCoins.getText().toString();
        mTotalCoins += Integer.parseInt(points);
        if(mTotalCoins > mCurrentCoins){
            snackbar("Insufficient Points",view);
            mTotalCoins = mTotalCoins - Integer.parseInt(points);
            return;
        }
        setToolBarTitle(mCurrentCoins - mTotalCoins);
        switch (gameProceed){
            case 1:
                if(mOpen.isChecked()){
                    dataPlayingList.add(new DataPlaying(mGameID,"single_digit","Open",points,openNum,"","",""));
                }
                else{
                    dataPlayingList.add(new DataPlaying(mGameID,"single_digit","Close",points,"",openNum,"",""));
                }
                break;
            case 2:
                String open_digit = openNum.substring(0,1);
                String close_digit = openNum.substring(1,2);
                dataPlayingList.add(new DataPlaying(mGameID,"jodi_digit","Open",points,open_digit,close_digit,"",""));
                break;
            case 3:
                if(mOpen.isChecked()){
                    dataPlayingList.add(new DataPlaying(mGameID,"single_panna","Open",points,"","",openNum,""));
                }
                else{
                    dataPlayingList.add(new DataPlaying(mGameID,"single_panna","Close",points,"","","",openNum));
                }
                break;
            case 4:
                if(mOpen.isChecked()){
                    dataPlayingList.add(new DataPlaying(mGameID,"double_panna","Open",points,"","",openNum,""));
                }
                else{
                    dataPlayingList.add(new DataPlaying(mGameID,"double_panna","Close",points,"","","",openNum));
                }
                break;
            case 5:
                if(mOpen.isChecked()){
                    dataPlayingList.add(new DataPlaying(mGameID,"triple_panna","Open",points,"","",openNum,""));
                }
                else{
                    dataPlayingList.add(new DataPlaying(mGameID,"triple_panna","Close",points,"","","",openNum));
                }
                break;
            case 6:
                if(mOpen.isChecked()){
                    dataPlayingList.add(new DataPlaying(mGameID,"half_sangam","Open",points,openNum,"","",closeNum));
                }
                else{
                    dataPlayingList.add(new DataPlaying(mGameID,"half_sangam","Close",points,"",closeNum,openNum,""));
                }
                break;
            case 7:
                dataPlayingList.add(new DataPlaying(mGameID,"full_sangam","Open",points,"","",openNum,closeNum));
                break;
        }
        mInpCloseD.setText("");
        mInputD.setText("");
        mInputCoins.setText("");
        if(gameProceed!=2) mRadioGroup.clearCheck();
        mRecyclerView.setVisibility(View.VISIBLE);
        llBidBottom.setVisibility(View.VISIBLE);
        mtvTotalCoins.setText("Total Points : "+ mTotalCoins);
         confRecycler();
        playingAdapter.notifyDataSetChanged();
    }

    private void setToolBarTitle(int i) {
        SpannableString s = new SpannableString(String.valueOf(i));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        mCoins.setTitle(s);
    }

    private void snackbar(String messaage, View view) {
        Snackbar.make(view,messaage, 2000).show();
    }

    public void proceedConformBtn(View view) {

        String gsonData = new Gson().toJson(dataPlayingList);
        String serverData = getString(R.string.bids_api_open)+gsonData+getString(R.string.bids_api_close);

        com.madhurmilan.milan.shareprefclass.MBSFragment MBSFragment = new MBSFragment(new MBSFragment.OnConformClick() {
            @Override
            public void onConformClick() {
                if (YourService.isOnline(PlayingActivity.this))
                    conDiaMethod(PlayingActivity.this, serverData);
                else
                    Toast.makeText(PlayingActivity.this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
        MBSFragment.show(getSupportFragmentManager(),getString(R.string.bottom_sheet));
        MBSFragment.setCancelable(false);
    }

    AlertDialog dialog;
    private void conDiaMethod(PlayingActivity activity, String serverData) {

        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().makeoffer(SharPrefClass.getLoginInToken(PlayingActivity.this), serverData);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(@NonNull Call<DataMain> call, @NonNull Response<DataMain> response) {
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
                    if (dataMain.getStatus().equals("success")){
                        SharPrefClass.setUserCoins(PlayingActivity.this, mCoins.getTitle().toString());
                        dataPlayingList.clear();
                        playingAdapter.notifyDataSetChanged();
                        llBidBottom.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlayingActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(PlayingActivity.this);
                        View view = inflater.inflate(R.layout.d_b_layout, null);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_white);
                        dialog.getWindow().setLayout(700,LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                    Toast.makeText(PlayingActivity.this, dataMain.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(PlayingActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.on_api_failure), Toast.LENGTH_LONG).show();
                System.out.println("Placed Bid OnFailure "+t);
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    public void playAgainBtn(View view) {
        dialog.dismiss();
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

    private void confRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        playingAdapter = new PlayingAdapter(this, dataPlayingList, new PlayingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int index = position;
                int size = dataPlayingList.size();
                if(position-size>=0){
                    index = size-1;
                }
                int bid_points = Integer.parseInt(dataPlayingList.get(index).getBid_points());
                mTotalCoins = mTotalCoins - bid_points;
                mtvTotalCoins.setText("Total Points : "+ mTotalCoins);
                dataPlayingList.remove(index);
                if(dataPlayingList.size()==0) llBidBottom.setVisibility(View.GONE);
                playingAdapter.notifyItemRemoved(position);
                setToolBarTitle(mCurrentCoins - mTotalCoins);
            }
        });
        mRecyclerView.setAdapter(playingAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }
}