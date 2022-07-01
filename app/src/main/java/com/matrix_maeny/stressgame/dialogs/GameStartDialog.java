package com.matrix_maeny.stressgame.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;

import com.matrix_maeny.stressgame.R;


public class GameStartDialog extends AppCompatDialogFragment {

    public static String targetC = null;
    public static String time = null;

    private TextView targetCountTv, timeTv;
    private AppCompatButton startBtn;
    private GameStartDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(requireContext(), com.google.android.material.R.style.Theme_AppCompat_Dialog_Alert);

        AlertDialog.Builder builder = new AlertDialog.Builder(contextThemeWrapper);
        View root = requireActivity().getLayoutInflater().inflate(R.layout.game_start, null);
        builder.setView(root);

        try {
            listener = (GameStartDialogListener) requireContext();
            initialize(root);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return builder.create();
    }

    @SuppressLint("SetTextI18n")
    private void initialize(View root) {
        targetCountTv = root.findViewById(R.id.gameStartTargetCountTv);
        timeTv = root.findViewById(R.id.gameStartTimeTv);
        startBtn = root.findViewById(R.id.gameStartBtn);
        startBtn.setOnClickListener(startBtnListener);

        if (targetC != null && time != null) {
            targetCountTv.setText("Target Count : " + targetC);
            timeTv.setText("Time Limit : " + time);

        }
    }

    View.OnClickListener startBtnListener = v -> {
        listener.start();
        dismiss();
    };

    public interface GameStartDialogListener {
        void start();
        void dialogBackPressed();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        listener.dialogBackPressed();
    }
}
