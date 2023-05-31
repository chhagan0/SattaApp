package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataStarlineGameList;

import java.util.List;

public class SLListAdapter extends RecyclerView.Adapter<SLListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataStarlineGameList.Data.StarlineGame starlineGame, View itemView);
    }
    Context context;
    private final List<DataStarlineGameList.Data.StarlineGame> starlineGameList;

    private final OnItemClickListener listener;

    public SLListAdapter(Context context, List<DataStarlineGameList.Data.StarlineGame> starlineGameList, OnItemClickListener listener) {
        this.context = context;
        this.starlineGameList = starlineGameList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SLListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.s_l_turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SLListAdapter.ViewHolder holder, int position) {
        holder.bind(starlineGameList.get(position), listener, context, position);
    }

    @Override
    public int getItemCount() {
        return starlineGameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView gameName;
        private final MaterialTextView gameResult;
        private final ShapeableImageView gamePlay;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gameName = itemView.findViewById(R.id.gameName);
            gameResult = itemView.findViewById(R.id.gameResult);
            gamePlay = itemView.findViewById(R.id.gamePlay);

        }

        public void bind(DataStarlineGameList.Data.StarlineGame starlineGame, OnItemClickListener listener, Context context, int position) {

            gameName.setText(starlineGame.getName());
            gameResult.setText(starlineGame.getResult());
            if(starlineGame.isPlay()) gamePlay.setImageResource(R.drawable.play_icon);
            else gamePlay.setImageResource(R.drawable.close_icon);
            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.move);
            gameResult.setAnimation(animation);
            itemView.setOnClickListener(v ->{
                listener.onItemClick(starlineGame, v);
            });
        }
    }
}
