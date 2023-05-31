package com.madhurmilan.milan.activityclass;

import static com.madhurmilan.milan.shareprefclass.Utility.BroadCastStringForAction;
import static com.madhurmilan.milan.shareprefclass.Utility.myReceiver;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.adapterclass.GaliDesawarListAdapter;
import com.madhurmilan.milan.apiclass.ApiClass;
import com.madhurmilan.milan.responseclass.DataDesawarList;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.madhurmilan.milan.shareprefclass.Utility;
import com.madhurmilan.milan.shareprefclass.YourService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisawarActivity extends AppCompatActivity {

    private static RecyclerView recSL;
    private static GaliDesawarListAdapter galiDesawarListAdapter;
    public static MaterialTextView sinDV, sinPV, dobPV;
    public static String stringURL = "";
    public static List<DataDesawarList.Data.GaliDesawarGame> galiDesawarGameList = new ArrayList<>();
    public static List<DataDesawarList.Data.GalidesawarRates> galidesawarRates;
    private static Vibrator mVibe;
    private MaterialButton wHisBtn, bHisBtn,chartTableBtn;
    private SwipeRefreshLayout refreshLayout;
    private MaterialTextView mDataConText;
    private IntentFilter mIntentFilter;
    private MaterialToolbar tool_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disawar);
        intVariables();
        confRecycler(this);
        clickListeners(this);
        getTurnamentList(this);

        Utility utility = new Utility(mDataConText);
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, YourService.class);
        startService(serviceIntent);
    }
    private void intVariables() {
        galidesawarRates = new ArrayList<>();
        recSL = findViewById(R.id.recy_s_l);
        sinDV = findViewById(R.id.left_d_v);
        sinPV = findViewById(R.id.right_d_v);
        dobPV = findViewById(R.id.jodi_d_v);
        mVibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;
        bHisBtn = findViewById(R.id.bHisBtn);
        wHisBtn = findViewById(R.id.wHisBtn);
        chartTableBtn = findViewById(R.id.chartTableBtn);
        refreshLayout = findViewById(R.id.swipe_ref_lyt);
        mDataConText = findViewById(R.id.internet_text);
        tool_bar = findViewById(R.id.tool_bar);
    }
    private void clickListeners(DisawarActivity context) {
        tool_bar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
        bHisBtn.setOnClickListener(v -> {
            Intent bidHistory = new Intent(context, WonHistoryActivity.class);
            bidHistory.putExtra(context.getString(R.string.history), 500);
            startActivity(bidHistory);
        });
        wHisBtn.setOnClickListener(v -> {
            Intent winHistory = new Intent(context, WonHistoryActivity.class);
            winHistory.putExtra(context.getString(R.string.history), 600);
            startActivity(winHistory);
        });
        chartTableBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, TableActivity.class);
            intent.putExtra(context.getString(R.string.chart), stringURL);
            startActivity(intent);
        });
        refreshLayout.setOnRefreshListener(() -> {
            getTurnamentList(context);
        });

    }
    public static void confRecycler(Context context) {
        galiDesawarListAdapter = new GaliDesawarListAdapter(context, galiDesawarGameList, (galiDesawarGame, itemView) -> {
            if (!galiDesawarGame.isPlay()){
                ObjectAnimator
                        .ofFloat(itemView, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                        .setDuration(700)
                        .start();
                mVibe.vibrate(500);
            }else {
                Intent intent = new Intent(context, DesawarTurnaActivity.class);
                intent.putExtra(context.getString(R.string.game), galiDesawarGame.getId());
                context.startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recSL.setLayoutManager(layoutManager);
        recSL.setAdapter(galiDesawarListAdapter);
    }

    private void getTurnamentList(DisawarActivity activity) {
        refreshLayout.setRefreshing(true);
        Call<DataDesawarList> call = ApiClass.getClient().GaliDesawarMethod(SharPrefClass.getLoginInToken(activity), "");
        call.enqueue(new Callback<DataDesawarList>() {
            @Override
            public void onResponse(@NonNull Call<DataDesawarList> call, @NonNull Response<DataDesawarList> response) {
                if (response.isSuccessful()) {
                    DataDesawarList dataDesawarList = response.body();
                    assert dataDesawarList != null;
                    if (dataDesawarList.getCode().equalsIgnoreCase("505")) {
                        SharPrefClass.setCleaninfo(activity);
                        Toast.makeText(activity, dataDesawarList.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(activity, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    if (dataDesawarList.getStatus().equalsIgnoreCase("success")) {
                        DataDesawarList.Data data = dataDesawarList.getData();
                        stringURL = data.getGali_disawar_chart();
                        galidesawarRates = data.getGalidesawarRates();
                        for (int i = 0; i < galidesawarRates.size(); i++) {
                            String value = galidesawarRates.get(i).getCost_amount() + "-" + galidesawarRates.get(i).getEarning_amount();
                            if(i==0)sinDV.setText(value);
                            if(i==1)sinPV.setText(value);
                            if(i==2)dobPV.setText(value);
                        }

                        galiDesawarGameList = data.getGaliDesawarGame();
                        confRecycler(activity);
                    }
                }else {
                    Toast.makeText(activity, activity.getString(R.string.response_error), Toast.LENGTH_SHORT).show();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<DataDesawarList> call, @NonNull Throwable t) {
                refreshLayout.setRefreshing(false);
                System.out.println("game list Error "+t);
                Toast.makeText(activity, activity.getString(R.string.on_api_failure), Toast.LENGTH_SHORT).show();
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