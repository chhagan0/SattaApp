package com.madhurmilan.milan.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.activityclass.SLTurnamentActivity;
import com.madhurmilan.milan.activityclass.TableActivity;
import com.madhurmilan.milan.activityclass.WonHistoryActivity;
import com.madhurmilan.milan.adapterclass.SLListAdapter;
import com.madhurmilan.milan.responseclass.DataStarlineGameList;

import java.util.ArrayList;
import java.util.List;

public class StarlineFragment extends Fragment {

    private Context context;
    private static Context mContext;
    private static RecyclerView recSL;
    private static SLListAdapter SLListAdapter;
    public static MaterialTextView sinDV, sinPV, dobPV, triPV;
    public static String stringURL = "";
    public static List<DataStarlineGameList.Data.StarlineGame> starlineGameList = new ArrayList<>();
    public static List<DataStarlineGameList.Data.StarlineRates> starlineRatesList;
    private static Vibrator mVibe;
    private MaterialButton chartTableBtn;
    TextView wHisBtn, bHisBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_starline, container, false);
        mContext = view.getContext();
        intVariables(view);
        confRecycler(context);
        clickListeners(context);
        return view;
    }

    private void intVariables(View view) {
        context = view.getContext();
        starlineRatesList = new ArrayList<>();
        recSL = view.findViewById(R.id.recy_s_l);
        sinDV = view.findViewById(R.id.single_d_v);
        sinPV = view.findViewById(R.id.single_p_v);
        dobPV = view.findViewById(R.id.double_p_v);
        triPV = view.findViewById(R.id.triple_p_v);
        mVibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
        bHisBtn = view.findViewById(R.id.bHisBtn);
        wHisBtn = view.findViewById(R.id.wHisBtn);
        chartTableBtn = view.findViewById(R.id.chartTableBtn);

    }
    private void clickListeners(Context context) {

        bHisBtn.setOnClickListener(v -> {
            Intent bidHistory = new Intent(context, WonHistoryActivity.class);
            bidHistory.putExtra(getString(R.string.history), 400);
            startActivity(bidHistory);
        });
        wHisBtn.setOnClickListener(v -> {
            Intent winHistory = new Intent(context, WonHistoryActivity.class);
            winHistory.putExtra(getString(R.string.history), 300);
            startActivity(winHistory);
        });
        chartTableBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, TableActivity.class);
            intent.putExtra(getString(R.string.chart), stringURL);
            startActivity(intent);
        });
    }

    public static void confRecycler(Context context) {
        SLListAdapter = new SLListAdapter(context, starlineGameList, (starlineGame, itemView) -> {
            if (!starlineGame.isPlay()){
                ObjectAnimator
                        .ofFloat(itemView, "translationX", 0, 25, -25, 25, -25,15, -15, 6, -6, 0)
                        .setDuration(700)
                        .start();
                mVibe.vibrate(500);
            }else {
                Intent intent = new Intent(context, SLTurnamentActivity.class);
                intent.putExtra(context.getString(R.string.game), starlineGame.getId());
                context.startActivity(intent);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recSL.setLayoutManager(layoutManager);
        recSL.setAdapter(SLListAdapter);
    }

    public static void recall() {
    }
}