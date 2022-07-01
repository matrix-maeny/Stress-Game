package com.matrix_maeny.stressgame;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private AppCompatButton easyLevelBtn;
    private AppCompatButton mediumLevelBtn;
    private AppCompatButton difLevelBtn;

    private int level = 1;
    private Sounds sounds;
    public static boolean enableSounds = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        easyLevelBtn.setOnClickListener(v -> {
            level = 1;
            startGame();
        });
        mediumLevelBtn.setOnClickListener(v -> {
            level = 2;
            startGame();
        });
        difLevelBtn.setOnClickListener(v -> {
            level = 3;
            startGame();
        });
    }

    private void initialize() {
        easyLevelBtn = findViewById(R.id.easyLevelBtn);
        mediumLevelBtn = findViewById(R.id.mediumLevelBtn);
        difLevelBtn = findViewById(R.id.difLevelBtn);

        sounds = new Sounds(MainActivity.this, 1);
        sounds.addSound(R.raw.click);
    }


    private void startGame() {
        sounds.play(0);
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        intent.putExtra("level", level);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_app:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.sounds:
                enableSounds = !enableSounds;
                if (enableSounds) {
                    item.setTitle("Sounds off");
                } else {
                    item.setTitle("Sounds on");
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}