package com.matrix_maeny.stressgame.gameboard;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.matrix_maeny.stressgame.R;
import com.matrix_maeny.stressgame.Sounds;

public class GameBoard extends View {

    private final int borderColor;
    private final int diskColor;
    private final int diskClickedColor;
    private final int backgroundColor;

    private final Paint borderColorPaint = new Paint();
    private final Paint diskColorPaint = new Paint();
    private final Paint diskClickedColorPaint = new Paint();
    private final Paint backgroundColorPaint = new Paint();

    private final RowColumns rowColumns = new RowColumns();

    private int cellSize;

    private final GameBoardListener listener;
    //    private  GameBoardListener listener;
    private Sounds sounds;


    public GameBoard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GameBoard, 0, 0);

        try {
            borderColor = a.getInteger(R.styleable.GameBoard_borderColor, 0);
            diskColor = a.getInteger(R.styleable.GameBoard_diskColor, 0);
            diskClickedColor = a.getInteger(R.styleable.GameBoard_diskClickedColor, 0);
            backgroundColor = a.getInteger(R.styleable.GameBoard_backgroundColor, 0);
            listener = (GameBoardListener) context;

            sounds = new Sounds(context, 2);
            sounds.addSound(R.raw.touched_sound, R.raw.win_sound, R.raw.lose);

        } finally {
            a.recycle();
        }

//        try {
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(context, "error Listener", Toast.LENGTH_SHORT).show();
//        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dimension = Math.min(getMeasuredHeight(), getMeasuredWidth());

        cellSize = dimension / 9;

        setMeasuredDimension(dimension, dimension);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        borderColorPaint.setStyle(Paint.Style.STROKE);
        borderColorPaint.setStrokeWidth(10);
        borderColorPaint.setAntiAlias(true);

        borderColorPaint.setColor(borderColor);

        diskColorPaint.setStyle(Paint.Style.FILL);
        diskColorPaint.setStrokeWidth(8);
        diskColorPaint.setAntiAlias(true);
        diskColorPaint.setColor(diskColor);

        diskClickedColorPaint.setStyle(Paint.Style.FILL);
        diskClickedColorPaint.setStrokeWidth(8);
        diskClickedColorPaint.setAntiAlias(true);

        diskClickedColorPaint.setColor(diskClickedColor);

        backgroundColorPaint.setStyle(Paint.Style.FILL);
        backgroundColorPaint.setStrokeWidth(8);
        backgroundColorPaint.setAntiAlias(true);

        backgroundColorPaint.setColor(backgroundColor);

        canvas.drawRect(0, 0, getWidth(), getWidth(), backgroundColorPaint);
        canvas.drawRect(0, 0, getWidth(), getWidth(), borderColorPaint);
        colorCell(canvas, rowColumns.getSelectedRow(), rowColumns.getSelectedColumn(), rowColumns.isFromUser());

    }

    @Override
    public boolean onFilterTouchEventForSecurity(MotionEvent event) {

        boolean isValid = false;

        float x = event.getX();
        float y = event.getY();

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {
//            if (soundsEnabled) {
//                playButtonSound();
//            }
            int row = (int) Math.ceil(y / cellSize);
            int column = (int) Math.ceil(x / cellSize);
//            rowColumns.setSelectedRow(rowColumns.getSelectedRow());
//            rowColumns.setSelectedColumn(rowColumns.getSelectedColumn());

            if (!rowColumns.isFromUser() && row == rowColumns.getSelectedRow() && column == rowColumns.getSelectedColumn()) {
                rowColumns.setFromUser(true);
                rowColumns.setScore(rowColumns.getScore() + 1);
                listener.scoreListener(rowColumns.getScore());
                isValid = true;

                sounds.play(0);
            }

//            colorCell(canvas2, 3, 3, true);


//            solver.setSelectedMatrixRow((int) Math.ceil(y / matrixSize));
//            solver.setSelectedMatrixColumn((int) Math.ceil(x / matrixSize));

        }


        return isValid;
    }

    private void colorCell(Canvas canvas, int row, int column, boolean fromUser) {


        if (row != -1 && column != -1) {

            float height, width;
            height = (row - 1) * cellSize + (cellSize / 2);
            width = (column - 1) * cellSize + (cellSize / 2);

            float radius = ((float) cellSize / 2 - 5);
            if (fromUser) {
                canvas.drawCircle(width, height, radius, diskClickedColorPaint);
            } else {
                canvas.drawCircle(width, height, radius, diskColorPaint);

            }
        }

        invalidate();

    }

    public RowColumns getRowColumns() {
        return rowColumns;
    }


    public interface GameBoardListener {
        void scoreListener(int score);
    }
}
