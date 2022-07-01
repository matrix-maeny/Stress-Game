package com.matrix_maeny.stressgame;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import java.util.ArrayList;

public class Sounds {

    private SoundPool soundPool;

    private ArrayList<Integer> soundIds;
    private ArrayList<Integer> streamIds;
    private Context context;


    public Sounds(Context context,int no_of_streams) {

        this.context = context;
        soundIds = new ArrayList<>();
        streamIds = new ArrayList<>();
        soundPool = new SoundPool(no_of_streams, AudioManager.STREAM_MUSIC, 0);
    }

    public void addSound(int... soundIds) {
        for (int i : soundIds) {
            this.soundIds.add(soundPool.load(context, i, 1));


        }
    }

    public void play(int index) {
        if(!MainActivity.enableSounds){
            return;
        }
        int id = soundPool.play(soundIds.get(index),1f,1f,0,0,1);
        streamIds.add(id);
    }

}
