package com.madhurmilan.milan.adapterclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;
import com.madhurmilan.milan.responseclass.DataPlaying;

import java.util.List;

public class PlayingAdapter extends RecyclerView.Adapter<PlayingAdapter.ViewHolder> {

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public Context context;
    private final List<DataPlaying> dataPlayingList;
    private final OnItemClickListener listener;

    public PlayingAdapter(Context context, List<DataPlaying> dataPlayingList, OnItemClickListener listener) {
        this.context = context;
        this.dataPlayingList = dataPlayingList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlayingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.playing_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayingAdapter.ViewHolder holder, int position) {
        holder.bind(dataPlayingList.get(position),position, listener);
    }

    @Override
    public int getItemCount() {
        return dataPlayingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final MaterialTextView digit;
        private final MaterialTextView panna;
        private final MaterialTextView points;
        private final MaterialTextView session;
        private final MaterialTextView digitsText;
        private final MaterialTextView pannaText;
        private final LinearLayout ll_panna;
        private final LinearLayout ll_session;
        private final ShapeableImageView crossBtn;
        private final View viewSession;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            panna = itemView.findViewById(R.id.panna);
            digit = itemView.findViewById(R.id.digit);
            crossBtn = itemView.findViewById(R.id.crossBtn);
            session = itemView.findViewById(R.id.session);
            points = itemView.findViewById(R.id.points);
            viewSession = itemView.findViewById(R.id.viewSession);
            ll_session = itemView.findViewById(R.id.ll_session);
            pannaText = itemView.findViewById(R.id.pannaText);
            digitsText = itemView.findViewById(R.id.digitsText);
            ll_panna = itemView.findViewById(R.id.ll_panna);
        }

        public void bind(DataPlaying dataPlaying, int position, OnItemClickListener listener) {
            points.setText(dataPlaying.getBid_points());
            session.setText(dataPlaying.getSession());
            switch (dataPlaying.getGame_type()){
                case "single_digit":
                    ll_panna.setVisibility(View.GONE);
                    digitsText.setText("Single Digit");
                    if(dataPlaying.getSession().equalsIgnoreCase("open")){
                        digit.setText(dataPlaying.getOpen_digit());
                    }
                    else{
                        digit.setText(dataPlaying.getClose_digit());
                    }
                    break;
                case "jodi_digit":
                    ll_panna.setVisibility(View.GONE);
                    viewSession.setVisibility(View.GONE);
                    ll_session.setVisibility(View.GONE);
                    digitsText.setText("Jodi Digit");
                    String jodi = dataPlaying.getOpen_digit()+""+ dataPlaying.getClose_digit();
                    digit.setText(jodi);
                    break;
                case "single_panna":
                    digitsText.setText("Single Panna");
                    ll_panna.setVisibility(View.GONE);
                    if(dataPlaying.getSession().equalsIgnoreCase("open")){
                        digit.setText(dataPlaying.getOpen_panna());
                    }
                    else{
                        digit.setText(dataPlaying.getClose_panna());
                    }
                    break;
                case "double_panna":
                    digitsText.setText("Double Panna");
                    ll_panna.setVisibility(View.GONE);
                    if(dataPlaying.getSession().equalsIgnoreCase("open")){
                        digit.setText(dataPlaying.getOpen_panna());
                    }
                    else{
                        digit.setText(dataPlaying.getClose_panna());
                    }
                    break;
                case "triple_panna":
                    digitsText.setText("Triple Panna");
                    ll_panna.setVisibility(View.GONE);
                    if(dataPlaying.getSession().equalsIgnoreCase("open")){
                        digit.setText(dataPlaying.getOpen_panna());
                    }
                    else{
                        digit.setText(dataPlaying.getClose_panna());
                    }
                    break;
                case "half_sangam":
                    if(dataPlaying.getSession().equalsIgnoreCase("open")){
                        digit.setText(dataPlaying.getOpen_digit());
                        panna.setText(dataPlaying.getClose_panna());
                        digitsText.setText("Open Digit");
                        pannaText.setText("Close Panna");
                    }
                    else{
                        digit.setText(dataPlaying.getOpen_panna());
                        panna.setText(dataPlaying.getClose_digit());
                        digitsText.setText("Open Panna");
                        pannaText.setText("Close Digit");
                    }
                    break;
                case "full_sangam":
                    digitsText.setText("Open Panna");
                    pannaText.setText("Close Panna");
                    viewSession.setVisibility(View.GONE);
                    ll_session.setVisibility(View.GONE);
                    digit.setText(dataPlaying.getOpen_panna());
                    panna.setText(dataPlaying.getClose_panna());
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
