package com.madhurmilan.milan.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.activityclass.AddCoinActivity;
import com.madhurmilan.milan.activityclass.PMethodActivity;
import com.madhurmilan.milan.activityclass.TCoinActivity;
import com.madhurmilan.milan.activityclass.TakeOutActivity;
import com.madhurmilan.milan.adapterclass.PurseAdapter;
import com.madhurmilan.milan.responseclass.DataWalletHistory;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;

import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends Fragment {

    private Context context;
    private static Context mContext;
    private static RecyclerView recyclerView;
    private static PurseAdapter purseAdapter;
    public static List<DataWalletHistory.Data.Statement> modelWalletArrayList = new ArrayList<>();
    private static ShapeableImageView mEmptyView;
    public static MaterialTextView coins, mMinWithdCoins, mWithdOT, mWithdCT;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private MaterialCardView transBtn, addCoins,withDBtn,bankBtn;
    private Vibrator mVibe;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        mContext = view.getContext();
        intVariables(view);
        laodData(context);
        confRecycler(context);
        clickListeners(context);
        return view;
    }

    public static void confRecycler(Context context) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        purseAdapter = new PurseAdapter(context, WalletFragment.modelWalletArrayList);
        recyclerView.setAdapter(purseAdapter);

        if (!modelWalletArrayList.isEmpty()){
            mEmptyView.setVisibility(View.GONE);
        }else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    private void laodData(Context context) {
        coins.setText(SharPrefClass.getCustomerCoins(context));
        mMinWithdCoins.setText("Minimum withdrawal point ares - "+ SharPrefClass.getMaxMinObject(context, SharPrefClass.KEY_MIN_EXTRACT_COINS));
        mVibe = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE) ;
    }

    private void clickListeners(Context context) {
        transBtn.setOnClickListener(v -> {
            if (SharPrefClass.getTransmitCoins(context)){
                Intent transferCoins = new Intent(context, TCoinActivity.class);
                startActivity(transferCoins);
            }else {
                Toast.makeText(context, "Transfer is not enable in your account", Toast.LENGTH_SHORT).show();
            }
            mVibe.vibrate(100);
        });
        addCoins.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddCoinActivity.class);
            startActivity(intent);
        });
        withDBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, TakeOutActivity.class);
            startActivity(intent);
        });
        bankBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, PMethodActivity.class);
            startActivity(intent);
        });
    }

    private void intVariables(View view) {
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recyclerViewWallet);
        mEmptyView = view.findViewById(R.id.emptyImage);
        coins = view.findViewById(R.id.coins);
        mMinWithdCoins = view.findViewById(R.id.minWithdCoins);
        mWithdOT = view.findViewById(R.id.withd_ot);
        mWithdCT = view.findViewById(R.id.withd_ct);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_ref_lyt);
        transBtn = view.findViewById(R.id.transBtn);
        addCoins = view.findViewById(R.id.addCoins);
        withDBtn = view.findViewById(R.id.withDBtn);
        bankBtn = view.findViewById(R.id.bankBtn);
    }

    public static void recall() {

    }
}