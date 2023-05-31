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
import com.madhurmilan.milan.responseclass.DataWin;

import java.util.List;

public class WonHistoryAdapter extends RecyclerView.Adapter<WonHistoryAdapter.ViewHolder> {

    Context context;
     private final List<DataWin.Data> dataList;

    public WonHistoryAdapter(Context context, List<DataWin.Data> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public WonHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.won_history_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WonHistoryAdapter.ViewHolder holder, int position) {
        DataWin.Data data = dataList.get(position);
        holder.attach(data);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView gameName;
        private final MaterialTextView gameSession;
        private final MaterialTextView gameNumberOpen;
        private final MaterialTextView gameDate;
        private final MaterialTextView bidPoints;
        private final MaterialTextView winPoints;
        private final MaterialTextView gameNumberClose;
        private final LinearLayout ll_bid_history;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            gameName = itemView.findViewById(R.id.gameName);
            gameSession = itemView.findViewById(R.id.gameSession);
            gameNumberOpen = itemView.findViewById(R.id.gameNumberOpen);
            gameDate = itemView.findViewById(R.id.gameDate);
            bidPoints = itemView.findViewById(R.id.bidPoints);
            winPoints = itemView.findViewById(R.id.winPoints);
            gameNumberClose = itemView.findViewById(R.id.gameNumberClose);
            ll_bid_history = itemView.findViewById(R.id.ll_bid_history);
            winPoints.setVisibility(View.GONE);
            gameNumberClose.setVisibility(View.GONE);
        }

        public void attach(DataWin.Data data) {
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
                    gameNumberClose.setVisibility(View.GONE);
                    if(data.getSession().equalsIgnoreCase("open")){
                        gameSession.setText("Session : OPEN");
                        gameNumberOpen.setText("Open Digit : "+data.getOpenDigit());
                    }
                    else{
                        gameSession.setText("Session : CLOSE");
                        gameNumberOpen.setText("Close Digit : "+data.getCloseDigit());
                    }
                    break;
                case "jodi_digit":
                    gameNumberClose.setVisibility(View.GONE);
                    gameName.setText(gameNameStr+"( Jodi Digit )");
                    gameSession.setText("Session : OPEN");
                    String jodi = "Jodi Digit : "+data.getOpenDigit()+""+data.getCloseDigit();
                    gameNumberOpen.setText(jodi);
                    break;
                case "single_panna":
                    gameNumberClose.setVisibility(View.GONE);
                    gameName.setText(gameNameStr+"( Single Panna )");
                    if(data.getSession().equalsIgnoreCase("open")){
                        gameSession.setText("Session : OPEN");
                        gameNumberOpen.setText("Open Panna : "+data.getOpenPanna());
                    }
                    else{
                        gameSession.setText("Session : CLOSE");
                        gameNumberOpen.setText("Close Panna : "+data.getClosePanna());
                    }
                    break;
                case "double_panna":
                    gameName.setText(gameNameStr+"( Double Panna )");
                    gameNumberClose.setVisibility(View.GONE);
                    if(data.getSession().equalsIgnoreCase("open")){
                        gameSession.setText("Session : OPEN");
                        gameNumberOpen.setText("Open Panna : "+data.getOpenPanna());
                    }
                    else{
                        gameSession.setText("Session : CLOSE");
                        gameNumberOpen.setText("Close Panna : "+data.getClosePanna());
                    }
                    break;
                case "triple_panna":
                    gameName.setText(gameNameStr+"( Triple Panna )");
                    gameNumberClose.setVisibility(View.GONE);
                    if(data.getSession().equalsIgnoreCase("open")){
                        gameSession.setText("Session : OPEN");
                        gameNumberOpen.setText("Open Panna : "+data.getOpenPanna());
                    }
                    else{
                        gameSession.setText("Session : CLOSE");
                        gameNumberOpen.setText("Close Panna : "+data.getClosePanna());
                    }
                    break;
                case "half_sangam":
                    gameName.setText(gameNameStr+"( Half Sangam )");
                    if(data.getSession().equalsIgnoreCase("open")){
                        gameSession.setText("Session : OPEN");
                        gameNumberOpen.setText("Open Digit : "+data.getOpenDigit());
                        gameNumberClose.setText("Close Panna : "+data.getClosePanna());
                        gameNumberClose.setVisibility(View.VISIBLE);
                    }
                    else{
                        gameSession.setText("Session : Close");
                        gameNumberOpen.setText("Open Panna : "+data.getOpenPanna());
                        gameNumberClose.setText("Close Digit : "+data.getCloseDigit());
                        gameNumberClose.setVisibility(View.VISIBLE);
                    }
                    break;
                case "full_sangam":
                    gameName.setText(gameNameStr+"( Full Sangam )");
                    gameSession.setText("Session : OPEN");
                    gameNumberOpen.setText("Open Panna : "+data.getOpenPanna());
                    gameNumberClose.setText("Close Panna : "+data.getClosePanna());
                    gameNumberClose.setVisibility(View.VISIBLE);
                    break;

            }
        }
    }
}
