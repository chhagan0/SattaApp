package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataStarlineBid;

import java.util.List;

public class SLBAdapter extends RecyclerView.Adapter<SLBAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    Context context;
    private final List<DataStarlineBid> dataStarlineBidList;
    private final OnItemClickListener listener;

    public SLBAdapter(Context context, List<DataStarlineBid> dataStarlineBidList, OnItemClickListener listener) {
        this.context = context;
        this.dataStarlineBidList = dataStarlineBidList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SLBAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.s_l_playing_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SLBAdapter.ViewHolder holder, int position) {
        holder.bind(dataStarlineBidList.get(position),position, listener);
    }

    @Override
    public int getItemCount() {
        return dataStarlineBidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView digit;
        private final MaterialTextView points;
        private final MaterialTextView digitsText;
        private final ShapeableImageView crossBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            digit = itemView.findViewById(R.id.digit);
            points = itemView.findViewById(R.id.points);
            digitsText = itemView.findViewById(R.id.digitsText);
            crossBtn = itemView.findViewById(R.id.crossBtn);
        }

        public void bind(DataStarlineBid dataStarlineBid, int position, OnItemClickListener listener) {
            points.setText(dataStarlineBid.getBid_points());
            switch (dataStarlineBid.getGame_type()){
                case "single_digit":
                    digitsText.setText("Single Digit");
                    digit.setText(dataStarlineBid.getDigit());

                    break;
                case "single_panna":
                    digitsText.setText("Single Panna");
                    digit.setText(dataStarlineBid.getPanna());

                    break;
                case "double_panna":
                    digitsText.setText("Double Panna");
                    digit.setText(dataStarlineBid.getPanna());

                    break;
                case "triple_panna":
                    digitsText.setText("Triple Panna");
                    digit.setText(dataStarlineBid.getPanna());
                    break;

            }

            crossBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position);
                }
            });
        }
    }
}
