package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.content.Intent;
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
import com.madhurmilan.milan.activityclass.TableActivity;
import com.madhurmilan.milan.responseclass.DataGameList;
import com.madhurmilan.milan.shareprefclass.SharPrefClass;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.ArrayList;

public class TurnamentListAdapter extends RecyclerView.Adapter<TurnamentListAdapter.ViewHolder>{

    public interface OnItemClickListener{
        void onItemClick(DataGameList.Data data, View itemView);
    }
    Context context;
    private final ArrayList<DataGameList.Data> datalArrayList;

    private final OnItemClickListener listener;

    public TurnamentListAdapter(Context context, ArrayList<DataGameList.Data> datalArrayList, OnItemClickListener listener) {
        this.context = context;
        this.datalArrayList = datalArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TurnamentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.turnament_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TurnamentListAdapter.ViewHolder holder, int position) {
        holder.attach(datalArrayList.get(position), listener, context, position);
    }

    @Override
    public int getItemCount() {
        return datalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView eventNumber;
        private final MaterialTextView openingTime;
        private final MaterialTextView closingTime;
        private final MaterialTextView marketOpen;
        private final ShapeableImageView chartTable;
        private final ShapeableImageView eventStatus;
        private final ShimmerTextView eventType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventType = itemView.findViewById(R.id.eventType);
            eventNumber = itemView.findViewById(R.id.eventNumber);
            eventStatus = itemView.findViewById(R.id.eventStatus);
            openingTime = itemView.findViewById(R.id.openingTime);
            closingTime = itemView.findViewById(R.id.closingTime);
            chartTable = itemView.findViewById(R.id.chartTable);
            marketOpen = itemView.findViewById(R.id.marketOpen);
            Shimmer shimmer = new Shimmer();

            shimmer.start(eventType);

        }

        public void attach(DataGameList.Data data, OnItemClickListener listener, Context context, int position) {
            if (!SharPrefClass.getLiveUser(context)){
                eventStatus.setImageResource(R.drawable.chart_icon);
                eventStatus.setOnClickListener(v -> {

                    Intent intent = new Intent(context, TableActivity.class);
                    intent.putExtra(context.getString(R.string.chart), data.getChart_url());
                    context.startActivity(intent);
                });
                marketOpen.setVisibility(View.GONE);

            }else {
                marketOpen.setVisibility(View.VISIBLE);
                if (data.isMarket_open() && data.isPlay()){
                    eventStatus.setImageResource(R.drawable.play_icon);
                    marketOpen.setText("Market is Running");
                    marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
                }else {
                    eventStatus.setImageResource(R.drawable.close_icon);
                    marketOpen.setText("Market Closed");
                    marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
                userDataMethod(data);
            }

            eventType.setText(data.getName());
            openingTime.setText(data.getOpen_time());
            closingTime.setText(data.getClose_time());
            eventNumber.setText(data.getResult());
            itemView.setOnClickListener(v ->{
                if(SharPrefClass.getLiveUser(context)){
                    listener.onItemClick(data, v);
                }
            });

            chartTable.setOnClickListener(v -> {
                Intent intent = new Intent(context, TableActivity.class);
                intent.putExtra(context.getString(R.string.chart), data.getChart_url());
                context.startActivity(intent);
            });


        }

        private void userDataMethod(DataGameList.Data data) {
            eventType.setText(data.getName());
            openingTime.setText(data.getOpen_time());
            closingTime.setText(data.getClose_time());
            eventNumber.setText(data.getResult());

            if (data.isMarket_open() && data.isPlay()){
                eventStatus.setImageResource(R.drawable.play_icon);
                marketOpen.setText("Market is Running");
                marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.green));
            }else {
                eventStatus.setImageResource(R.drawable.close_icon);
                marketOpen.setText("Market Closed");
                marketOpen.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                marketOpen.setTextColor(ContextCompat.getColor(context, R.color.red));
            }
            Animation  animation = AnimationUtils.loadAnimation(context, R.anim.move);
            eventNumber.setAnimation(animation);
        }
    }
}
