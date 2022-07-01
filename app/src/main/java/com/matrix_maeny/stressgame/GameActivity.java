package com.matrix_maeny.stressgame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.matrix_maeny.stressgame.dialogs.GameStartDialog;
import com.matrix_maeny.stressgame.gameboard.GameBoard;
import com.matrix_maeny.stressgame.gameboard.RowColumns;

import java.util.Objects;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements GameBoard.GameBoardListener, GameStartDialog.GameStartDialogListener, GameResultDialog.GameResultDialogListener {

    private int level = 1;
    private GameBoard gameBoard;
    private TextView gameTimeTv;
    private TextView userCountTv;
    private RowColumns rowColumns;

    private final Handler handler = new Handler();
    private final Handler timeHandler = new Handler();
    private final Random random = new Random();
    private long delayTime = 800;
    private int targetC = 0;
    private int gameTime = 0;
    private int gameTimeTemp = 0;
    private volatile boolean gameOver = false;

    private int score = 0;

//    private final int SIM_SOUND = 3;
//    private final float LEFT_VOLUME = 1.0f;
//    private final float RIGHT_VOLUME = 1.0f;
//    private final int LOOP = 1;
//    private final int PRTY = 1;
//    private final float NORMAL_PLAY_RATE = 1;
//
//    private SoundPool soundPool;
//    private int click_sound;
//    private int win_sound;
//    private int touched_sound;

    private Sounds sounds;
    private static MediaPlayer mediaPlayer;// = MediaPlayer.create(GameActivity.this, R.raw.win_sound);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.win_sound);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initialize();


    }

    @SuppressLint("SetTextI18n")
    private void initialize() {
        level = getIntent().getIntExtra("level", 1);
        setLevelDelayTime();

        gameBoard = findViewById(R.id.gameBoard);
        gameTimeTv = findViewById(R.id.timeTv);
        TextView targetCountTv = findViewById(R.id.targetCountTv);
        userCountTv = findViewById(R.id.userCountTv);

        rowColumns = gameBoard.getRowColumns();

//        new Thread() {
//            public void run() {
        targetCountTv.setText("Target Count: " + targetC);
        gameTimeTv.setText(String.valueOf(gameTime));
        userCountTv.setText(String.valueOf(0));

        sounds = new Sounds(GameActivity.this, 3);
        sounds.addSound(R.raw.click, R.raw.win_sound, R.raw.lose);


        startGame();
//            }
//        }.start();
    }

    private void startGame() {
        GameStartDialog dialog = new GameStartDialog();
        GameStartDialog.targetC = String.valueOf(targetC);
        GameStartDialog.time = String.valueOf(gameTime);
        dialog.show(getSupportFragmentManager(), "Game Start");

    }


    private void setLevelDelayTime() {

        Random random = new Random();
        int[] arrTime = {1, 2, 3, 4, 5, 6, 9};
        int rNum = random.nextInt(7);

        gameTime = arrTime[rNum] * 60;
        gameTimeTemp = gameTime;
        double temp;

        switch (level) {
            case 1:
                delayTime = 800; // leve 1
                break;
            case 2:
                delayTime = 600;
                break;
            case 3:
                delayTime = 450;
                break;
        }

        temp = delayTime / 1000.0;

        double tempC = (gameTime) / temp;

        targetC = (int) tempC - 10;


    }

    private void startMoving() {

        timeHandler.postDelayed(new Runnable() { // for time
            @SuppressLint("SetTextI18n")
            @Override
            public void run() {
                if (!gameOver && gameTime >= 0) {
                    gameTimeTv.setText(gameTime + "");
                    gameTime--;
                    timeHandler.postDelayed(this, 1000);
                }
            }
        }, 1000);


        handler.postDelayed(new Runnable() { // for game
            @Override
            public void run() {
                if (gameTime >= 0 && score < targetC) {
                    int randomX = random.nextInt(8) + 1;
                    int randomY = random.nextInt(8) + 1;

                    rowColumns.setSelectedRow(randomX);
                    rowColumns.setSelectedColumn(randomY);
                    rowColumns.setFromUser(false);


                    gameBoard.invalidate();

                    handler.postDelayed(this, delayTime);
                } else {
                    // do the things when game is completed or when user wins..
                    gameOver = true;
                    if (score == targetC) {
                        // user won
                        playWin();
                    } else {
                        //user failed
//                        sounds.play(2);
                        playLose();

                    }

                    startResultActions(score == targetC);

                }
            }
        }, delayTime);

    }


    private void startResultActions(boolean win) {
        GameResultDialog dialog = new GameResultDialog();
        GameResultDialog.win = win;
        dialog.show(getSupportFragmentManager(), "Game Won");
    }


    private void stopSound() {
        try {
            mediaPlayer.stop();
            mediaPlayer.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playWin() {
        if(!MainActivity.enableSounds){
            return;
        }
        try {
            stopSound();
            mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.win_sound);
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void playLose() {
        if(!MainActivity.enableSounds){
            return;
        }
        try {
            stopSound();
            mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.lose);
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void scoreListener(int score) {
        this.score = score;
        userCountTv.setText(String.valueOf(score));
    }

    @Override
    public void start() {
        sounds.play(0);
        startMoving();
    }

    @Override
    public void dialogBackPressed() { // called when you press back button in the presence ofo dialog
        stopSound();
        finish(); // finish the activity because he doesn't started yet;
    }

    @Override
    public void onBackPressed() { // called when you press back button
        stopSound();
        super.onBackPressed();
        finish();
    }


    @Override
    public void restartGame() {
        stopSound();
        sounds.play(0);
        gameOver = false;
        rowColumns.setScore(0);


        if (score == targetC) {
            setLevelDelayTime();
            score = 0;
            startGame();
        } else {
            gameTime = gameTimeTemp;
            score = 0;
            startMoving();
        }


    }
}