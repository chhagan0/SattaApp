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
import com.madhurmilan.milan.responseclass.DataDesawarBid;

import java.util.List;

public class DesawarBAdapter extends RecyclerView.Adapter<DesawarBAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    Context context;
    private final List<DataDesawarBid> dataDesawarBidList;
    private final OnItemClickListener listener;

    public DesawarBAdapter(Context context, List<DataDesawarBid> dataDesawarBidList, OnItemClickListener listener) {
        this.context = context;
        this.dataDesawarBidList = dataDesawarBidList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.s_l_playing_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(dataDesawarBidList.get(position),position, listener);
    }

    @Override
    public int getItemCount() {
        return dataDesawarBidList.size();
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

        public void bind(DataDesawarBid dataDesawarBid, int position, OnItemClickListener listener) {
            points.setText(dataDesawarBid.getBid_points());
            switch (dataDesawarBid.getGame_type()){
                case "left_digit":
                    digitsText.setText("Left Digit");
                    digit.setText(dataDesawarBid.getLeft_digit());

                    break;
                case "right_digit":
                    digitsText.setText("Right Digit");
                    digit.setText(dataDesawarBid.getRight_digit());

                    break;
                case "jodi_digit":
                    digitsText.setText("Jodi Digit");
                    digit.setText(dataDesawarBid.getLeft_digit()+ dataDesawarBid.getRight_digit());

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
