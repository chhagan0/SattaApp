package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.fragment.DashboardFragment.user_name;
import static com.madhurmilan.milan.fragment.StarlineFragment.dobPV;
import static com.madhurmilan.milan.fragment.StarlineFragment.sinDV;
import static com.madhurmilan.milan.fragment.StarlineFragment.sinPV;
import static com.madhurmilan.milan.fragment.StarlineFragment.starlineRatesList;
import static com.madhurmilan.milan.fragment.StarlineFragment.triPV;
import static com.madhurmilan.milan.fragment.WalletFragment.mMinWithdCoins;
import static com.madhurmilan.milan.fragment.WalletFragment.mWithdCT;
import static com.madhurmilan.milan.fragment.WalletFragment.mWithdOT;
import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.BuildConfig;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.NonSwipeableViewPager;
import com.madhurmilan.milan.adapterclass.ViewPagerAppliance;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.fragment.DashboardFragment;
import com.madhurmilan.milan.fragment.StarlineFragment;
import com.madhurmilan.milan.fragment.WalletFragment;
import com.madhurmilan.milan.responseclass.DataGameList;
import com.madhurmilan.milan.responseclass.DataLogIN;
import com.madhurmilan.milan.responseclass.DataProfileStatus;
import com.madhurmilan.milan.responseclass.DataStarlineGameList;
import com.madhurmilan.milan.responseclass.DataWalletHistory;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private MaterialToolbar mToolbar;
    private NavigationView mNaviView;
    ImageSlider imageSlider;

    private DrawerLayout mDrawerLayout;
    public static MaterialTextView userName, mMobileNum;
    private MenuItem purse, mBankAc, mpurseMenu, howToLearn, mGameValues, mCoins,  profile, contactUs,changePassword, logout;
    private ProgressBar mProgressBar;
    private SwipeRefreshLayout refreshLayout;
    private int mAvaPoints =0;
    private SwitchMaterial mNotiSwitchBtn;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    public static NonSwipeableViewPager viewPager;
    private ViewPagerAppliance viewPagerAppliance;
    private TabLayout tabLayout;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        imageSlider=findViewById(R.id.image_slider);
        ArrayList<SlideModel> imageList = new ArrayList<>();

// Add images to the list


        imageList.add(new SlideModel("https://im2.ezgif.com/tmp/ezgif-2-f200a1358f.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://im2.ezgif.com/tmp/ezgif-2-f200a1358f.jpg", ScaleTypes.CENTER_CROP));
        imageList.add(new SlideModel("https://im2.ezgif.com/tmp/ezgif-2-f200a1358f.jpg", ScaleTypes.CENTER_CROP));

            imageSlider.setImageList(imageList);








        intVariables();
        updateUserStatus();
        confToolbar();
        confiData();
        clickListener();
        confiViewPager();


        Utility utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);


    }

    private void confiViewPager() {
        viewPagerAppliance.add(new DashboardFragment(),"Dashboard");
        viewPagerAppliance.add(new StarlineFragment(), "StarLine");
        viewPagerAppliance.add(new WalletFragment(), "Wallet");
        viewPager.setAdapter(viewPagerAppliance);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clickListener() {
        refreshLayout.setOnRefreshListener(() -> {
            if (YourService.isOnline(DashboardActivity.this)){
                checkUserStatusMethod();
                getGameListMethod(DashboardActivity.this);
                getTurnamentList(DashboardActivity.this);
                getDesawarGame(DashboardActivity.this);
                purseStatementMethod(DashboardActivity.this);
                getUserInfoMethod(DashboardActivity.this, SharPrefClass.getLoginInToken(DashboardActivity.this));
            }
            else Toast.makeText(DashboardActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        });
        mNotiSwitchBtn.setOnCheckedChangeListener((buttonView, isChecked) -> SharPrefClass.setBinalData(DashboardActivity.this, SharPrefClass.KEY_FIREBSE_ALLOW, isChecked));

        mToolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId()==R.id.purse||item.getItemId()==R.id.coins){
                viewPager.setCurrentItem(2, true);
            }
            return false;
        });


    }

    private void confiData() {

        if (YourService.isOnline(this)){
            checkUserStatusMethod();
            getGameListMethod(DashboardActivity.this);
            getTurnamentList(DashboardActivity.this);
            getDesawarGame(DashboardActivity.this);
            purseStatementMethod(DashboardActivity.this);
            getUserInfoMethod(DashboardActivity.this, SharPrefClass.getLoginInToken(DashboardActivity.this));
        }
        else Toast.makeText(this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();

    }

    private void intVariables() {
        mDrawerLayout = findViewById(R.id.drawerLayout);
        mToolbar = findViewById(R.id.toolbar);
        mNaviView = findViewById(R.id.navigationView);
        refreshLayout= findViewById(R.id.swipe_ref_lyt);
        mProgressBar = findViewById(R.id.progressBar);
        mDataConText = findViewById(R.id.dataConText);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);
        viewPager.setOffscreenPageLimit(3);
        viewPagerAppliance = new ViewPagerAppliance(getSupportFragmentManager());

        mNaviView.setItemIconTintList(null);
        userName = mNaviView.getHeaderView(0).findViewById(R.id.userDName);
        mMobileNum = mNaviView.getHeaderView(0).findViewById(R.id.mobile_nav_num);
        mNotiSwitchBtn = mNaviView.getHeaderView(0).findViewById(R.id.notiSwitchBtn);

        purse = mNaviView.getMenu().findItem(R.id.purse);
        mBankAc = mNaviView.getMenu().findItem(R.id.bank_ac);
        mpurseMenu = mToolbar.getMenu().findItem(R.id.purse);
        mCoins = mToolbar.getMenu().findItem(R.id.coins);
        howToLearn = mNaviView.getMenu().findItem(R.id.how_to_learn);
        mGameValues = mNaviView.getMenu().findItem(R.id.game_values);
        userName.setText(SharPrefClass.getRegistrationObject(this, SharPrefClass.KEY_USER_NAME));
        mMobileNum.setText(SharPrefClass.getRegistrationObject(this, SharPrefClass.KEY_PHONE_NUMBER));
        mNotiSwitchBtn.setChecked(SharPrefClass.getBinalObject(this, SharPrefClass.KEY_FIREBSE_ALLOW,true));

        profile = mNaviView.getMenu().findItem(R.id.profile);
        contactUs = mNaviView.getMenu().findItem(R.id.contactUs);
        changePassword = mNaviView.getMenu().findItem(R.id.changePassword);
        logout = mNaviView.getMenu().findItem(R.id.logout);
        if (SharPrefClass.getSharedBooleanStatus(this, SharPrefClass.KEY_DEVELOPER_MODE)){
            mToolbar.setTitleCentered(true);
            mToolbar.setTitle(getString(R.string.app_name));
            profile.setVisible(false);
            contactUs.setVisible(false);
            changePassword.setVisible(false);
            logout.setTitle("Exit App");
            userName.setVisibility(View.GONE);
            mMobileNum.setVisibility(View.GONE);
        }else {
            mToolbar.setTitleCentered(false);
            profile.setVisible(true);
            contactUs.setVisible(true);
            changePassword.setVisible(true);
            logout.setTitle(getString(R.string.signout));
            userName.setVisibility(View.VISIBLE);
            mMobileNum.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
        checkUserStatusMethod();
        purseStatementMethod(this);

    }

    private void checkUserStatusMethod() {
        refreshLayout.setRefreshing(true);
        Call<DataProfileStatus> call = ApiClass.getClient().Customer_status(SharPrefClass.getLoginInToken(this),"");
        call.enqueue(new Callback<DataProfileStatus>() {
            @Override
            public void onResponse(@NonNull Call<DataProfileStatus> call, @NonNull Response<DataProfileStatus> response) {
                if (response.isSuccessful()){
                    DataProfileStatus dataProfileStatus = response.body();
                    if (dataProfileStatus.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(DashboardActivity.this);
                        Toast.makeText(DashboardActivity.this, dataProfileStatus.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataProfileStatus.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        SharPrefClass.setUserCoins(DashboardActivity.this, dataProfileStatus.getData().getAvailablePoints());
                        SharPrefClass.setTransmitCoins(DashboardActivity.this, dataProfileStatus.getData().getTransferPoint().equalsIgnoreCase("1"));
                        SharPrefClass.setAddAmountUPI(DashboardActivity.this, SharPrefClass.KEY_ADD_COINS_BHIM_ID, dataProfileStatus.getData().getUpiPaymentId());
                        SharPrefClass.setAddAmountUPI(DashboardActivity.this, SharPrefClass.KEY_ADD_COINS_BHIM_NAME, dataProfileStatus.getData().getUpiName());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MAX_ADD_AMOUNT_COINS, dataProfileStatus.getData().getMaximumDeposit());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MIN_ADD_AMOUNT_COINS, dataProfileStatus.getData().getMinimumDeposit());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MAX_EXTRACT_COINS, dataProfileStatus.getData().getMaximumWithdraw());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MIN_EXTRACT_COINS, dataProfileStatus.getData().getMinimumWithdraw());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MAX_OFFER_SUM, dataProfileStatus.getData().getMaximumBidAmount());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MIN_OFFER_SUM, dataProfileStatus.getData().getMinimumBidAmount());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MAX_TRANSMIT_COINS, dataProfileStatus.getData().getMaximumTransfer());
                        SharPrefClass.setMaxMinData(DashboardActivity.this, SharPrefClass.KEY_MIN_TRANSMIT_COINS, dataProfileStatus.getData().getMinimumTransfer());
                        mAvaPoints = Integer.parseInt(dataProfileStatus.getData().getAvailablePoints());
                        setToolBarTitle(mAvaPoints);
                        mMinWithdCoins.setText("Minimum withdrawal points are - "+ dataProfileStatus.getData().getMinimumWithdraw());
                    }
                    SharPrefClass.setLiveUser(DashboardActivity.this, dataProfileStatus.getStatus().equalsIgnoreCase("success"));
                    updateUserStatus();

                }else {
                    Toast.makeText(DashboardActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataProfileStatus> call, Throwable t) {
                Toast.makeText(DashboardActivity.this, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
                System.out.println("user_status error "+ t);
                refreshLayout.setRefreshing(false);
            }
        });
    }
    private void updateUserStatus() {
        if (!SharPrefClass.getLiveUser(this)){
            purse.setVisible(false);
            mCoins.setVisible(false);
            mBankAc.setVisible(false);
            mpurseMenu.setVisible(false);
            howToLearn.setVisible(false);
            mGameValues.setVisible(false);
            tabLayout.setVisibility(View.GONE);
        }else{
            purse.setVisible(true);
            mCoins.setVisible(true);
            mBankAc.setVisible(true);
            mpurseMenu.setVisible(true);
            howToLearn.setVisible(true);
            mGameValues.setVisible(true);
//            tabLayout.setVisibility(View.VISIBLE);
            tabLayout.setVisibility(View.GONE);

        }
    }

    private void confToolbar() {
        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        mNaviView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.home:
                    mDrawerLayout.closeDrawers();
                    break;
                case R.id.seeFullProfile:
                    Intent profile = new Intent(DashboardActivity.this, UserInfoActivity.class);
                    startActivity(profile);
                    break;
                case R.id.addFunds:
                    Intent addFunds = new Intent(DashboardActivity.this, AddCoinActivity.class);
                    startActivity(addFunds);
                    break;
                case R.id.withdrawPoints:
                    Intent withdrawPoints = new Intent(DashboardActivity.this, TakeOutActivity.class);
                    startActivity(withdrawPoints);
                    break;
                case R.id.walletStatement:
                    viewPager.setCurrentItem(2, true);
                    break;
                case R.id.transferPoints:
                    if (SharPrefClass.getTransmitCoins(DashboardActivity.this)){
                        Intent transferPoints = new Intent(DashboardActivity.this, TCoinActivity.class);
                        startActivity(transferPoints);
                    }else {
                        Toast.makeText(DashboardActivity.this, "Transfer is not enable in your account", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.manageBank:
                    Intent manageBank = new Intent(DashboardActivity.this, BActivity.class);
                    startActivity(manageBank);
                    break;
                case R.id.managePaytm:
                    Intent managePaytm = new Intent(DashboardActivity.this, UPIDActivity.class);
                    managePaytm.putExtra(getString(R.string.upi), 1);
                    startActivity(managePaytm);
                    break;
                case R.id.manageGooglePay:
                    Intent manageGooglePay = new Intent(DashboardActivity.this, UPIDActivity.class);
                    manageGooglePay.putExtra(getString(R.string.upi),3);
                    startActivity(manageGooglePay);
                    break;
                case R.id.managePhonePe:
                    Intent managePhonePay = new Intent(DashboardActivity.this, UPIDActivity.class);
                    managePhonePay.putExtra(getString(R.string.upi), 2);
                    startActivity(managePhonePay);
                    break;
                case R.id.winHistory:
                    Intent winHistory = new Intent(DashboardActivity.this, WonHistoryActivity.class);
                    winHistory.putExtra(getString(R.string.history), 100);
                    startActivity(winHistory);
                    break;
                case R.id.bidHistory:
                    Intent bidHistory = new Intent(DashboardActivity.this, WonHistoryActivity.class);
                    bidHistory.putExtra(getString(R.string.history),200);
                    startActivity(bidHistory);
                    break;
                case R.id.game_values:
                    Intent gameRates = new Intent(DashboardActivity.this, GameValuesActivity.class);
                    gameRates.putExtra(getString(R.string.main_activity), 1);
                    startActivity(gameRates);
                    break;
                case R.id.how_to_learn:
                    Intent howToPlay = new Intent(DashboardActivity.this, GameValuesActivity.class);
                    howToPlay.putExtra(getString(R.string.main_activity), 2);
                    startActivity(howToPlay);
                    break;
                case R.id.contactUs:
                    Intent contactUs = new Intent(DashboardActivity.this, UserHelpActivity.class);
                    startActivity(contactUs);
                    break;
                case R.id.shareWithFriends:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,
                            "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                    break;
                case R.id.rateApp:
                    String url = "https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    break;
                case R.id.changePassword:
                    String[] arrayStrings = new String[]{SharPrefClass.getRegistrationObject(DashboardActivity.this, SharPrefClass.KEY_PHONE_NUMBER), SharPrefClass.getLoginInToken(DashboardActivity.this)};
                    Intent changePassword = new Intent(DashboardActivity.this, NewPassActivity.class);
                    changePassword.putExtra(getString(R.string.changePassword), 1);
                    changePassword.putExtra(getString(R.string.phone_number), arrayStrings);
                    startActivity(changePassword);
                    break;
                case R.id.logout:
                    LogOutDialog();
                    mDrawerLayout.closeDrawers();
                    break;
            }
            mDrawerLayout.closeDrawers();
            return true;
        });
    }

    private void LogOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.exit_application));
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_exit));
        builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (SharPrefClass.getSharedBooleanStatus(DashboardActivity.this, SharPrefClass.KEY_DEVELOPER_MODE)){
                    finishAffinity();
                }else {
                    Intent logOut = new Intent(DashboardActivity.this, SignInActivity.class);
                    startActivity(logOut);
                    SharPrefClass.setSigninSuccess(DashboardActivity.this, false);
                }
            }
        });

        builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(Typeface.DEFAULT_BOLD);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(Typeface.DEFAULT_BOLD);

        alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(DashboardActivity.this,R.drawable.rounded_corner_white));
        alertDialog.getWindow().setLayout(900, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
    private void getUserInfoMethod(DashboardActivity activity, String token) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataLogIN> call = ApiClass.getClient().GetUserInfo(token,"");
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
                        SharPrefClass.setRegisterData(activity, SharPrefClass.KEY_USER_NAME, dataLogIN.getData().getUsername());
                        SharPrefClass.setRegisterData(activity, SharPrefClass.KEY_PHONE_NUMBER, dataLogIN.getData().getMobile());
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_CLIENT_EMAIL, dataLogIN.getData().getEmail());
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_BANK_USER_NAME, dataLogIN.getData().getAccount_holder_name());
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_AC_N, dataLogIN.getData().getBank_account_no());
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_IFSC_C, dataLogIN.getData().getIfsc_code());
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_B_N, dataLogIN.getData().getBank_name());
                        SharPrefClass.setBankInformation(activity, SharPrefClass.KEY_BRANCH_ADDRESS, dataLogIN.getData().getBranch_address());
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_P_UPI_ID, dataLogIN.getData().getPaytm_mobile_no());
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_PP_UPI_ID, dataLogIN.getData().getPhonepe_mobile_no());
                        SharPrefClass.setPrefrenceStrngData(activity, SharPrefClass.KEY_G_PAY_UPI_ID, dataLogIN.getData().getGpay_mobile_no());

                        user_name.setText("Hello,  "+ dataLogIN.getData().getUsername());
                        userName.setText(dataLogIN.getData().getUsername());
                        mMobileNum.setText(dataLogIN.getData().getMobile());
                    }else
                        Toast.makeText(activity, dataLogIN.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
                DashboardFragment.recall();
                StarlineFragment.recall();
                WalletFragment.recall();
            }

            @Override
            public void onFailure(Call<DataLogIN> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                System.out.println("getUserDetails error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void setToolBarTitle(int i) {
        SpannableString s = new SpannableString(String.valueOf(i));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        mCoins.setTitle(s);
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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(mNaviView)){
            mDrawerLayout.closeDrawers();
            return;
        }
        if (viewPager.getCurrentItem()!=0){
            viewPager.setCurrentItem(0, true);
            return;
        }
        finishAffinity();
    }

    private void getGameListMethod(DashboardActivity activity) {
        mProgressBar.setVisibility(View.VISIBLE);
        Call<DataGameList> call = ApiClass.getClient().MainTournamentList(SharPrefClass.getLoginInToken(activity), "");
        call.enqueue(new Callback<DataGameList>() {
            @Override
            public void onResponse(@NonNull Call<DataGameList> call, @NonNull Response<DataGameList> response) {
                if (response.isSuccessful()){
                    DataGameList dataGameList = response.body();
                    assert dataGameList != null;
                    if (dataGameList.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataGameList.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    if(dataGameList.getStatus().equalsIgnoreCase("success")){
                        DashboardFragment.mDataList = dataGameList.getData();
                        DashboardFragment.confRecyView(activity);
                    }
                    if (tabLayout.getSelectedTabPosition()==0){
                        //  Toast.makeText(activity, dataGameList.getMessage()+"1010101010", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<DataGameList> call, @NonNull Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                System.out.println("game list Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTurnamentList(DashboardActivity activity) {
        refreshLayout.setRefreshing(true);
        Call<DataStarlineGameList> call = ApiClass.getClient().slTurnament(SharPrefClass.getLoginInToken(activity), "");
        call.enqueue(new Callback<DataStarlineGameList>() {
            @Override
            public void onResponse(@NonNull Call<DataStarlineGameList> call, @NonNull Response<DataStarlineGameList> response) {
                if (response.isSuccessful()) {
                    DataStarlineGameList dataStarlineGameList = response.body();
                    assert dataStarlineGameList != null;
                    if (dataStarlineGameList.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataStarlineGameList.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataStarlineGameList.getStatus().equalsIgnoreCase("success")) {
                        DataStarlineGameList.Data data = dataStarlineGameList.getData();
                        StarlineFragment.stringURL = data.getStarlineChart();
                        starlineRatesList = data.getStarlineRates();
                        for (int i = 0; i < starlineRatesList.size(); i++) {
                            String value = starlineRatesList.get(i).getCost_amount() + "-" + starlineRatesList.get(i).getEarning_amount();
                            if(i==0)sinDV.setText(value);
                            if(i==1)sinPV.setText(value);
                            if(i==2)dobPV.setText(value);
                            if(i==3)triPV.setText(value);
                        }

                        StarlineFragment.starlineGameList = data.getStarlineGame();
                        StarlineFragment.confRecycler(activity);
                    }
                    if (tabLayout.getSelectedTabPosition()==1){}
                 //   Toast.makeText(activity, dataStarlineGameList.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<DataStarlineGameList> call, @NonNull Throwable t) {
                refreshLayout.setRefreshing(false);
                System.out.println("game list Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void getDesawarGame(DashboardActivity activity) {
        refreshLayout.setRefreshing(true);
        Call<DataStarlineGameList> call = ApiClass.getClient().slTurnament(SharPrefClass.getLoginInToken(activity), "");
        call.enqueue(new Callback<DataStarlineGameList>() {
            @Override
            public void onResponse(@NonNull Call<DataStarlineGameList> call, @NonNull Response<DataStarlineGameList> response) {
                if (response.isSuccessful()) {
                    DataStarlineGameList dataStarlineGameList = response.body();
                    assert dataStarlineGameList != null;
                    if (dataStarlineGameList.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataStarlineGameList.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataStarlineGameList.getStatus().equalsIgnoreCase("success")) {
                        DataStarlineGameList.Data data = dataStarlineGameList.getData();
                        StarlineFragment.stringURL = data.getStarlineChart();
                        starlineRatesList = data.getStarlineRates();
                        for (int i = 0; i < starlineRatesList.size(); i++) {
                            String value = starlineRatesList.get(i).getCost_amount() + "-" + starlineRatesList.get(i).getEarning_amount();
                            if(i==0)sinDV.setText(value);
                            if(i==1)sinPV.setText(value);
                            if(i==2)dobPV.setText(value);
                            if(i==3)triPV.setText(value);
                        }

                        StarlineFragment.starlineGameList = data.getStarlineGame();
                        StarlineFragment.confRecycler(activity);
                    }
                    if (tabLayout.getSelectedTabPosition()==1){}
                    //   Toast.makeText(activity, dataStarlineGameList.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<DataStarlineGameList> call, @NonNull Throwable t) {
                refreshLayout.setRefreshing(false);
                System.out.println("game list Error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void purseStatementMethod(DashboardActivity activity) {
        refreshLayout.setRefreshing(true);
        Call<DataWalletHistory> call = ApiClass.getClient().purseStatement(SharPrefClass.getLoginInToken(activity),"");
        call.enqueue(new Callback<DataWalletHistory>() {
            @Override
            public void onResponse(Call<DataWalletHistory> call, Response<DataWalletHistory> response) {
                if (response.isSuccessful()){
                    DataWalletHistory dataWalletHistory = response.body();
                    if (dataWalletHistory.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataWalletHistory.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataWalletHistory.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        SharPrefClass.setUserCoins(activity, dataWalletHistory.getData().getAvailablePoints());
                        WalletFragment.coins.setText(dataWalletHistory.getData().getAvailablePoints());
                        mWithdOT.setText("Withdraw Open time = "+ dataWalletHistory.getData().getWithdrawOpenTime());
                        mWithdCT.setText("Withdraw Close time = "+ dataWalletHistory.getData().getWithdrawCloseTime());
                        WalletFragment.modelWalletArrayList = dataWalletHistory.getData().getStatement();
                       WalletFragment.confRecycler(DashboardActivity.this);
                    }

                    if (tabLayout.getSelectedTabPosition()==2){
                        //   Toast.makeText(activity, dataWalletHistory.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataWalletHistory> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                System.out.println("walletStatement error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

}