package com.madhurmilan.milan.shareprefclass;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.kalyankuber.alpha.R;

public class MyBSTransferCoins extends BottomSheetDialogFragment {

    public interface OnConformClick{
        void onConformClick();
    }
    private View view;
    private MaterialButton cancel, conform;
    private MaterialTextView mtv_bottom_msg;
    private BottomSheetDialog bottomSheetDialog;
    private OnConformClick conformClick;

    public MyBSTransferCoins(OnConformClick conformClick) {
        this.conformClick = conformClick;
    }

    @NonNull @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnShowListener(dialogInterface -> bottomSheetDialog = (BottomSheetDialog) dialogInterface);
        return  dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.b_s_layout, container, false);
        intVariables();
        listeners();

        return view;

    }

    private void listeners() {
        cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        conform.setOnClickListener(v -> {
            conformClick.onConformClick();
            bottomSheetDialog.dismiss();
        });
    }

    private void intVariables() {
        cancel = view.findViewById(R.id.cancel);
        conform = view.findViewById(R.id.conform);
        mtv_bottom_msg = view.findViewById(R.id.mtv_bottom_msg);
        mtv_bottom_msg.setText("Confirm Points Transfer ?");

    }

}
