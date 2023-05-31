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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.DesawarBAdapter;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataDesawarBid;
import com.madhurmilan.milan.responseclass.DataMain;
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

public class DesawarBPActivity extends AppCompatActivity {

    private int TuranPro =0;
    private int totalCoins = 0;
    private int currentCoins = 0;
    private String turnamentID;
    private final boolean isOpen = false;
    private MaterialToolbar toolbar;
    private MaterialTextView mtotalCoins;
    private MaterialAutoCompleteTextView inD;
    private LinearLayout ll_b_b;
    private TextInputEditText inputCoins;
    private ArrayList<String> digits;
    private RecyclerView recyclerView;
    private final List<DataDesawarBid> desawarBidArrayList = new ArrayList<>();
    private DesawarBAdapter desawarBAdapter;
    private MenuItem coins;
    private ProgressBar progressBar;
    private MaterialTextView dataConText;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desawar_b_p);
        intVariables();
        confToolbar();
        confRecycler();
        loadData();
    }

    private void loadData() {
        Utility utility = new Utility(dataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }

    private void intVariables() {
        toolbar = findViewById(R.id.tool_bar);
        MaterialTextView chooseDate = findViewById(R.id.choose_date);
        inD = findViewById(R.id.input_d);
        inputCoins = findViewById(R.id.edit_text_coins);
        MaterialTextView digText = findViewById(R.id.digText);
        mtotalCoins = findViewById(R.id.mtv_total_coins);
        MaterialButton proceedConform = findViewById(R.id.pro_conf);
        MaterialButton btn_proceed = findViewById(R.id.btn_proceed);
        recyclerView = findViewById(R.id.recyclerView);
        ll_b_b = findViewById(R.id.ll_bid_bottom);
        progressBar = findViewById(R.id.progress_bar);
        dataConText = findViewById(R.id.internet_text);
        TuranPro = getIntent().getIntExtra(getString(R.string.game_name), 12);
        turnamentID = getIntent().getStringExtra("games");
        currentCoins = Integer.parseInt(SharPrefClass.getCustomerCoins(DesawarBPActivity.this));
        digits = new ArrayList<String>();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("EEE dd-MMM-yyyy", Locale.getDefault());
        String formattedDate = df.format(c);
        chooseDate.setText(formattedDate);
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceed(v);
            }
        });

    }
    private void confRecycler() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        desawarBAdapter = new DesawarBAdapter(this, desawarBidArrayList, new DesawarBAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                int index = position;
                int size = desawarBidArrayList.size();
                if(position-size>=0){
                    index = size-1;
                }
                int bid_points = Integer.parseInt(desawarBidArrayList.get(index).getBid_points());
                totalCoins = totalCoins - bid_points;
                mtotalCoins.setText("Total Points : "+ totalCoins);
                desawarBidArrayList.remove(index);
                if(desawarBidArrayList.size()==0) ll_b_b.setVisibility(View.GONE);
                desawarBAdapter.notifyItemRemoved(position);
                setToolBarTitle(currentCoins - totalCoins);
            }
        });
        recyclerView.setAdapter(desawarBAdapter);
    }

    private void confToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        switch (TuranPro){
            case 12:
                actionbar.setTitle(getString(R.string.left_digit));
                for (int i = 0; i <= 9; i++) {
                    digits.add(String.valueOf(i));
                }
                break;
            case 13:
                actionbar.setTitle(getString(R.string.right_digit));
                for (int i = 0; i <= 9; i++) {
                    digits.add(String.valueOf(i));
                }                break;
            case 14:
                actionbar.setTitle(getString(R.string.jodi_digit));
                for (int i = 0; i <= 9; i++) {
                    for (int j = 0; j<=9; j++){
                        digits.add(String.valueOf(i)+String.valueOf(j));
                    }
                }                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, digits);

        inD.setThreshold(1);//will start working from first character
        inD.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView


        int maxLength = digits.get(0).length() ;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        inD.setFilters(fArray);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_purse, menu);
        coins = menu.findItem(R.id.coins);
        MenuItem wallet = menu.findItem(R.id.purse);
        coins.setEnabled(false);
        wallet.setEnabled(false);
        coins.setVisible(true);

        SpannableString s = new SpannableString(String.valueOf(currentCoins));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        coins.setTitle(s);
        return super.onCreateOptionsMenu(menu);
    }
    public void proceed(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        if (TextUtils.isEmpty(inD.getText().toString())){
            snackbar(getString(R.string.please_enter_digits),view);
            return;
        }
        if (!digits.contains(inD.getText().toString())){
            snackbar(getString(R.string.please_enter_valid_digits), view);
            return;
        }
        if (TextUtils.isEmpty(inputCoins.getText().toString().trim())){
            snackbar(getString(R.string.please_enter_points),view);
            return;
        }
        if (Integer.parseInt(inputCoins.getText().toString().trim())<Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM))
                ||Integer.parseInt(inputCoins.getText().toString().trim())>Integer.parseInt(SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM))){
            snackbar("Minimum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MIN_OFFER_SUM)+" and Maximum Bid Points "+ SharPrefClass.getMaxMinObject(this, SharPrefClass.KEY_MAX_OFFER_SUM),view);
            return;
        }
        setRecycleData(TuranPro,view);

    }

    private void setRecycleData(int gameProceed, View view) {
        String openNum = inD.getText().toString();
        String points = inputCoins.getText().toString();
        totalCoins += Integer.parseInt(points);
        if(totalCoins > currentCoins){
            snackbar("Insufficient Points",view);
            totalCoins = totalCoins - Integer.parseInt(points);
            return;
        }
        setToolBarTitle(currentCoins - totalCoins);
        switch (gameProceed){
            case 12:
                desawarBidArrayList.add(new DataDesawarBid(turnamentID,"left_digit",points,openNum,""));
                break;
            case 13:
                desawarBidArrayList.add(new DataDesawarBid(turnamentID,"right_digit",points,"",openNum));
                break;
            case 14:
                String left_digit = openNum.substring(0,1);
                String right_digit = openNum.substring(1,2);
                desawarBidArrayList.add(new DataDesawarBid(turnamentID,"jodi_digit",points,left_digit,right_digit));
                break;
        }
        inD.setText("");
        inputCoins.setText("");
        recyclerView.setVisibility(View.VISIBLE);
        ll_b_b.setVisibility(View.VISIBLE);
        mtotalCoins.setText("Total Points : "+ totalCoins);
        desawarBAdapter.notifyDataSetChanged();
    }

    private void setToolBarTitle(int i) {
        SpannableString s = new SpannableString(String.valueOf(i));
        s.setSpan(new ForegroundColorSpan(Color.WHITE), 0, s.length(), 0);
        s.setSpan(new RelativeSizeSpan(1.50f),0,s.length(),0);
        s.setSpan(new StyleSpan(Typeface.BOLD),0,s.length(),0);
        coins.setTitle(s);
    }

    private void snackbar(String messaage, View view) {
        Snackbar.make(view,messaage, 2000).show();
    }

    public void proceedConformBtn(View view) {

        String gsonData = new Gson().toJson(desawarBidArrayList);
        String serverData = getString(R.string.bids_api_open)+gsonData+getString(R.string.bids_api_close);

        MBSFragment mbsFragment = new MBSFragment(new MBSFragment.OnConformClick() {
            @Override
            public void onConformClick() {
                if (YourService.isOnline(DesawarBPActivity.this))
                    conformDialog(DesawarBPActivity.this, serverData);
                else
                    Toast.makeText(DesawarBPActivity.this, getString(R.string.check_your_internet_connection), Toast.LENGTH_SHORT).show();
            }
        });
        mbsFragment.show(getSupportFragmentManager(),getString(R.string.bottom_sheet));
        mbsFragment.setCancelable(false);
    }

    AlertDialog dialog;
    private void conformDialog(DesawarBPActivity activity, String serverData) {

        progressBar.setVisibility(View.VISIBLE);
        Call<DataMain> call = ApiClass.getClient().galiDesawarPlaceBid(SharPrefClass.getLoginInToken(DesawarBPActivity.this), serverData);
        call.enqueue(new Callback<DataMain>() {
            @Override
            public void onResponse(@NonNull Call<DataMain> call, @NonNull Response<DataMain> response) {
                if (response.isSuccessful()){
                    DataMain mainModel = response.body();
                    assert mainModel != null;
                    if (mainModel.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, mainModel.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (mainModel.getStatus().equals("success")){
                        SharPrefClass.setUserCoins(DesawarBPActivity.this, coins.getTitle().toString());
                        desawarBidArrayList.clear();
                        desawarBAdapter.notifyDataSetChanged();
                        ll_b_b.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(DesawarBPActivity.this);
                        LayoutInflater inflater = LayoutInflater.from(DesawarBPActivity.this);
                        View view = inflater.inflate(R.layout.d_b_layout, null);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                        dialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_corner_white);
                        dialog.getWindow().setLayout(700,LinearLayout.LayoutParams.WRAP_CONTENT);
                    }
                    Toast.makeText(DesawarBPActivity.this, mainModel.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DesawarBPActivity.this, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onFailure(Call<DataMain> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.on_api_failure), Toast.LENGTH_LONG).show();
                System.out.println("starlinePlaceBid OnFailure "+t);
                progressBar.setVisibility(View.GONE);
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
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(myReceiver, mIntentFilter);
    }
}