package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataDesawarList;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.List;

public class GaliDesawarListAdapter extends RecyclerView.Adapter<GaliDesawarListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataDesawarList.Data.GaliDesawarGame starlineGame, View itemView);
    }
    Context context;
    private final List<DataDesawarList.Data.GaliDesawarGame> galiDesawarGameList;

    private final OnItemClickListener listener;

    public GaliDesawarListAdapter(Context context, List<DataDesawarList.Data.GaliDesawarGame> galiDesawarGameList, OnItemClickListener listener) {
        this.context = context;
        this.galiDesawarGameList = galiDesawarGameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.g_d_turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(galiDesawarGameList.get(position), listener, context, position);
    }

    @Override
    public int getItemCount() {
        return galiDesawarGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ShimmerTextView gameName;
        private final ShimmerTextView marketOpen;
        private final MaterialTextView gameResult;
        private final MaterialTextView closeTime;
        private final ShapeableImageView gamePlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
            gameResult = itemView.findViewById(R.id.gameResult);
            gamePlay = itemView.findViewById(R.id.gamePlay);
            closeTime = itemView.findViewById(R.id.closeTime);
            marketOpen = itemView.findViewById(R.id.marketOpen);

            Shimmer shimmer = new Shimmer();
            shimmer.start(gameName);
            shimmer.start(marketOpen);

        }

        public void bind(DataDesawarList.Data.GaliDesawarGame galiDesawarGame, OnItemClickListener listener, Context context, int position) {

            closeTime.setText("Close : " +galiDesawarGame.getTime());
            gameName.setText(galiDesawarGame.getName());
            gameResult.setText(galiDesawarGame.getResult());
            if(galiDesawarGame.isPlay()) {
                Animation  rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
                gamePlay.startAnimation(rotate);
                gamePlay.setImageResource(R.drawable.play_icon);
                marketOpen.setText("Market is Running");
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
            }
            else {
                gamePlay.setImageResource(R.drawable.close_icon);
                marketOpen.setText("Market is Closed");
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.run);
            gameResult.setAnimation(animation);
            itemView.setOnClickListener(v ->{
                listener.onItemClick(galiDesawarGame, v);
            });
        }
    }
}
