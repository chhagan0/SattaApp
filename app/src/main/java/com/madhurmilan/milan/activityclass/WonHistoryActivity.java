package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.DisaWonHistoryAdapter;
import com.madhurmilan.milan.adapterclass.SLWonHistoryAdapter;
import com.madhurmilan.milan.adapterclass.WonHistoryAdapter;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataDisawarWin;
import com.madhurmilan.milan.responseclass.DataStarlineWin;
import com.madhurmilan.milan.responseclass.DataWin;
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

public class WonHistoryActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private MaterialTextView from_date, to_date;
    private Date f_date, t_date;
    private final SimpleDateFormat userSF = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    private final SimpleDateFormat serverSF = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
    private final Calendar fromCalender = Calendar.getInstance();
    private final Calendar toCalendar = Calendar.getInstance();
    private final Calendar todayCalwndar = Calendar.getInstance();
    private ShapeableImageView emptyImage;

    private int his =0;
    private RecyclerView recyclerView;
    private WonHistoryAdapter wonHistoryAdapter;
    private SLWonHistoryAdapter SLWonHistoryAdapter;
    private DisaWonHistoryAdapter disaWonHistoryAdapter;
    private List<DataWin.Data> dataArrayList = new ArrayList<>();
    private List<DataStarlineWin.Data> slWonModelList = new ArrayList<>();
    private List<DataDisawarWin.Data> disaWonModelList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private Call<DataWin> call;
    private Call<DataStarlineWin> sLCall;
    private Call<DataDisawarWin> disaCall;
    private IntentFilter mIntentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_won_history);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.matka_blue));

        intVariables();
        loadData();
        toolbarMethod();


    }

    private void loadData() {
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);

        f_date = Calendar.getInstance().getTime();
        t_date = Calendar.getInstance().getTime();
        from_date.setText(userSF.format(f_date));
        to_date.setText(userSF.format(t_date));
        his = getIntent().getIntExtra(getString(R.string.history), 0);
        if (his ==100 || his ==200){
            historyMethod(WonHistoryActivity.this, f_date, t_date);
        }
        if (his ==300 || his ==400){
            winHistoryHistoryMethod(WonHistoryActivity.this, f_date, t_date);
        }
        if (his==500||his==600){
            desawarHistoryMethod(WonHistoryActivity.this, f_date, t_date);
        }
    }


    private void intVariables() {
        toolbar = findViewById(R.id.toolbar);
        from_date = findViewById(R.id.fromDate);
        to_date = findViewById(R.id.toDate);
        recyclerView = findViewById(R.id.recyclerView);
        emptyImage = findViewById(R.id.emptyImage);
        swipeRefreshLayout = findViewById(R.id.swipe_ref_lyt);

        MaterialTextView dataConText = findViewById(R.id.dataConText);
        Utility utility = new Utility(dataConText);

    }
    DatePickerDialog.OnDateSetListener toDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            toCalendar.set(Calendar.YEAR, year);
            toCalendar.set(Calendar.MONTH, monthOfYear);
            toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            if(toCalendar.getTimeInMillis()< fromCalender.getTimeInMillis()){
                Toast.makeText(WonHistoryActivity.this, "To Date can't be smaller then From Date", Toast.LENGTH_SHORT).show();
                return;
            }
            t_date = toCalendar.getTime();
            to_date.setText(userSF.format(t_date));
        }
    };

    DatePickerDialog.OnDateSetListener fromDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            fromCalender.set(Calendar.YEAR, year);
            fromCalender.set(Calendar.MONTH, monthOfYear);
            fromCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            f_date = fromCalender.getTime();
            from_date.setText(userSF.format(f_date));
        }
    };

    private void toolbarMethod() {
        if (his ==100){
            toolbar.setTitle(getString(R.string.win_his));
        }else {
            toolbar.setTitle(getString(R.string.bid_his));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (his ==100 || his ==200){
                    historyMethod(WonHistoryActivity.this, f_date, t_date);
                }
                if (his ==300 || his ==400){
                    winHistoryHistoryMethod(WonHistoryActivity.this, f_date, t_date);
                }
            }
        });
    }
    public void fromDate(View view) {
        DatePickerDialog datePickerDialog=  new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Panel, fromDatePicker, fromCalender
                .get(Calendar.YEAR), fromCalender.get(Calendar.MONTH), fromCalender.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        long maxDate = todayCalwndar.getTime().getTime() ;
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.white));
        datePickerDialog.show();
    }

    public void toDate(View view) {
        DatePickerDialog datePickerDialog=  new DatePickerDialog(this,android.R.style.Theme_Holo_Light_Panel, toDatePicker, toCalendar
                .get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();


        long maxDate = todayCalwndar.getTime().getTime() ;
        datePickerDialog.getDatePicker().setMaxDate(maxDate);
        datePickerDialog.setCanceledOnTouchOutside(true);
        datePickerDialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.white));
        datePickerDialog.show();
    }



    public void submitWinHistory(View view) {

        if (his ==100 || his ==200){
            historyMethod(WonHistoryActivity.this, f_date, t_date);
        }
        if (his ==300 || his ==400){
            winHistoryHistoryMethod(WonHistoryActivity.this, f_date, t_date);
        }
    }

    private void winHistoryHistoryMethod(WonHistoryActivity activity, Date fDate, Date tDate) {
        String fromDate = serverSF.format(fDate) + " 00:00:00";
        String toDate = serverSF.format(tDate) + " 23:59:59";

        swipeRefreshLayout.setRefreshing(true);
        switch (his){
            case 300:
                sLCall = ApiClass.getClient().HistorySLBids(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
            case 400:
                sLCall = ApiClass.getClient().starLineBidHistory(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
        }

        sLCall.enqueue(new Callback<DataStarlineWin>() {
            @Override
            public void onResponse(@NonNull Call<DataStarlineWin> call, @NonNull Response<DataStarlineWin> response) {
                if (response.isSuccessful()){
                    DataStarlineWin dataStarlineWin = response.body();
                    if (dataStarlineWin.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataStarlineWin.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataStarlineWin.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

                        slWonModelList = dataStarlineWin.getData();
                        recyclerView.setLayoutManager(layoutManager);
                        SLWonHistoryAdapter = new SLWonHistoryAdapter(activity, slWonModelList);
                        recyclerView.setAdapter(SLWonHistoryAdapter);
                        emptyImage.setVisibility(View.GONE);
                    }else {
                        emptyImage.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(activity, dataStarlineWin.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataStarlineWin> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                System.out.println("bidHistory error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void historyMethod(WonHistoryActivity activity, Date fDate, Date tDate) {
        String fromDate = serverSF.format(fDate) + " 00:00:00";
        String toDate = serverSF.format(tDate) + " 23:59:59";
        swipeRefreshLayout.setRefreshing(true);
        switch (his){
            case 100:
                call = ApiClass.getClient().HistoryOfWins(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
            case 200:
                call = ApiClass.getClient().HistoryOfBids(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
        }

        call.enqueue(new Callback<DataWin>() {
            @Override
            public void onResponse(@NonNull Call<DataWin> call, @NonNull Response<DataWin> response) {
                if (response.isSuccessful()){
                    DataWin dataWin = response.body();
                    if (dataWin.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataWin.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    System.out.println("winModel.getStatus() "+ dataWin.getStatus());
                    if (dataWin.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);

                        dataArrayList = dataWin.getData();
                        recyclerView.setLayoutManager(layoutManager);
                        wonHistoryAdapter = new WonHistoryAdapter(activity, dataArrayList);
                        recyclerView.setAdapter(wonHistoryAdapter);
                        System.out.println("winModelArrayList.size() "+ dataArrayList.size());
                        emptyImage.setVisibility(View.GONE);

                    }else {
                        emptyImage.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(activity, dataWin.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataWin> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                System.out.println("bidHistory error "+t);
                Toast.makeText(activity, getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void desawarHistoryMethod(WonHistoryActivity activity, Date fDate, Date tDate) {
        String fromDate = serverSF.format(fDate) + " 00:00:00";
        String toDate = serverSF.format(tDate) + " 23:59:59";
        swipeRefreshLayout.setRefreshing(true);
        switch (his){
            case 500:
                disaCall = ApiClass.getClient().deasawarBidHistory(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
            case 600:
                disaCall = ApiClass.getClient().desawarWinHistory(SharPrefClass.getLoginInToken(activity),fromDate, toDate);
                break;
        }

        disaCall.enqueue(new Callback<DataDisawarWin>() {
            @Override
            public void onResponse(@NonNull Call<DataDisawarWin> call, @NonNull Response<DataDisawarWin> response) {
                if (response.isSuccessful()){
                    DataDisawarWin dataDisawarWin = response.body();
                    if (dataDisawarWin.getCode().equalsIgnoreCase("505")){
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataDisawarWin.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataDisawarWin.getStatus().equalsIgnoreCase(getString(R.string.success))){
                        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                        disaWonModelList = dataDisawarWin.getData();

                        recyclerView.setLayoutManager(layoutManager);
                        disaWonHistoryAdapter = new DisaWonHistoryAdapter(activity, disaWonModelList);
                        recyclerView.setAdapter(disaWonHistoryAdapter);
                        emptyImage.setVisibility(View.GONE);

                    }else {
                        recyclerView.setVisibility(View.GONE);
                        emptyImage.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(activity, dataDisawarWin.getMessage(), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(activity, getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<DataDisawarWin> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                System.out.println("bidHistory error "+t);
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