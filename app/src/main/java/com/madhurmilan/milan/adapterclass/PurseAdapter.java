package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataWalletHistory;

import java.util.List;

public class PurseAdapter extends RecyclerView.Adapter<PurseAdapter.ViewHolder> {

    Context context;
    private final List<DataWalletHistory.Data.Statement> modelWalletArrayList;

    public PurseAdapter(Context context, List<DataWalletHistory.Data.Statement> modelWalletArrayList) {
        this.context = context;
        this.modelWalletArrayList = modelWalletArrayList;
    }

    @NonNull
    @Override
    public PurseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.purse_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurseAdapter.ViewHolder holder, int position) {
        DataWalletHistory.Data.Statement statement = modelWalletArrayList.get(position);
        holder.attach(statement);
    }

    @Override
    public int getItemCount() {
        return modelWalletArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView bonusName;
        private final MaterialTextView amount;
        private final MaterialTextView dateTime;
        private final MaterialTextView tranStatus;
        private final LinearLayout ll_wallet;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bonusName = itemView.findViewById(R.id.bonusName);
            amount = itemView.findViewById(R.id.coins);
            dateTime = itemView.findViewById(R.id.dateTime);
            ll_wallet = itemView.findViewById(R.id.ll_wallet);
            tranStatus = itemView.findViewById(R.id.tranStatus);
        }

        public void attach(DataWalletHistory.Data.Statement recyclerModelWallet) {
            bonusName.setText(recyclerModelWallet.getTransMsg());
            tranStatus.setText(recyclerModelWallet.getTransStatus());
            if (recyclerModelWallet.getTransStatus().equalsIgnoreCase("pending")){
                tranStatus.setTextColor(ContextCompat.getColor(context, R.color.yellow));
            }else {
                tranStatus.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            String amountStr = null;
            if (recyclerModelWallet.getTransType().equalsIgnoreCase("credit")){
                amountStr = "+";
                amount.setTextColor(ContextCompat.getColor(context, R.color.green));
                ll_wallet.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.green )));
            }else if (recyclerModelWallet.getTransType().equalsIgnoreCase("debit")){
                amountStr = "-";
                amount.setTextColor(ContextCompat.getColor(context, R.color.red));
                ll_wallet.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.red )));
            }
            amount.setText(amountStr+recyclerModelWallet.getPoints());
            dateTime.setText(recyclerModelWallet.getCreatedAt());
        }
    }
}
