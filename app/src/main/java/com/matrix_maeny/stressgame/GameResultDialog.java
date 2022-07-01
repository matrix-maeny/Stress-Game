package com.matrix_maeny.stressgame;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;

public class GameResultDialog extends AppCompatDialogFragment {

    public static boolean win = false;

    private GameResultDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ContextThemeWrapper themeWrapper = new ContextThemeWrapper(requireContext(), androidx.appcompat.R.style.Theme_AppCompat_Dialog_Alert);
        AlertDialog.Builder builder = new AlertDialog.Builder(themeWrapper);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.game_result, null);
        builder.setView(root);


        TextView resultTv = root.findViewById(R.id.gameResultTv);
        AppCompatButton resultBtn = root.findViewById(R.id.gameResultBtn);
        ImageView resultIv = root.findViewById(R.id.gameResultIv);

        if (win) {
            root.setBackgroundColor(getResources().getColor(R.color.win_color));
            resultTv.setText(R.string.win_string);
            resultBtn.setText(R.string.play_again);
            resultIv.setImageResource(R.drawable.winner);

        } else {
            root.setBackgroundColor(getResources().getColor(R.color.lose_color));
            resultTv.setText(R.string.lose_string);
            resultBtn.setText(R.string.replay);
            resultIv.setImageResource(R.drawable.lose);
        }

        try {
            listener = (GameResultDialogListener) requireContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultBtn.setOnClickListener(v -> {
            // start game again..
            listener.restartGame();
            dismiss();
        });


        return builder.create();
    }


    public interface GameResultDialogListener {
        void restartGame();
        void dialogBackPressed();
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        listener.dialogBackPressed();
    }
}
