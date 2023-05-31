package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataStarlineWin;

import java.util.List;

public class SLWonHistoryAdapter extends RecyclerView.Adapter<SLWonHistoryAdapter.ViewHolder> {

    Context context;
    private final List<DataStarlineWin.Data> dataList;

    public SLWonHistoryAdapter(Context context, List<DataStarlineWin.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public SLWonHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.won_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SLWonHistoryAdapter.ViewHolder holder, int position) {
        DataStarlineWin.Data data = dataList.get(position);
        holder.bind(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView gameName;
        private final MaterialTextView gameDate;
        private final MaterialTextView bidPoints;
        private final MaterialTextView winPoints;
        private final MaterialTextView gameNumberOpen;
        private final LinearLayout ll_bid_history;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gameName = itemView.findViewById(R.id.gameName);
            gameDate = itemView.findViewById(R.id.gameDate);
            bidPoints = itemView.findViewById(R.id.bidPoints);
            winPoints = itemView.findViewById(R.id.winPoints);
            MaterialTextView session = itemView.findViewById(R.id.gameSession);
            gameNumberOpen = itemView.findViewById(R.id.gameNumberOpen);
            MaterialTextView gameNumberClose = itemView.findViewById(R.id.gameNumberClose);
            ll_bid_history = itemView.findViewById(R.id.ll_bid_history);
            winPoints.setVisibility(View.GONE);
            gameNumberClose.setVisibility(View.GONE);
            session.setVisibility(View.GONE);
        }

        public void bind(DataStarlineWin.Data data) {
            String gameNameStr = data.getGameName();
            bidPoints.setText(data.getBidPoints()+" Points");
            gameDate.setText(data.getBiddedAt());
            ll_bid_history.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.teal_200)));
            if(!TextUtils.isEmpty(data.getWinPoints())){
                ll_bid_history.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.green)));
                winPoints.setText(data.getWinPoints()+" Points");
                winPoints.setVisibility(View.VISIBLE);
                gameDate.setText(data.getWonAt());
            }
            switch (data.getGameType()){
                case "single_digit":
                    gameName.setText(gameNameStr+"( Single Digit )");
                    gameNumberOpen.setText("Game Number : "+data.getDigit());
                    break;
                case "single_panna":
                    gameName.setText(gameNameStr+"( Single Panna )");
                    gameNumberOpen.setText("Game Number : "+data.getPanna());
                    break;
                case "double_panna":
                    gameName.setText(gameNameStr+"( Double Panna )");
                    gameNumberOpen.setText("Game Number : "+data.getPanna());
                    break;
                case "triple_panna":
                    gameName.setText(gameNameStr+"( Triple Panna )");
                    gameNumberOpen.setText("Game Number : "+data.getPanna());
                    break;
            }
        }
    }
}
