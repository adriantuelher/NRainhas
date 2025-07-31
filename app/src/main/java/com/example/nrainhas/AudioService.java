package com.example.nrainhas;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AudioService extends Service {
    private MediaPlayer mp = null;
    private int posicao;
    private String acao;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acao = intent.getAction();

        switch (acao){
            case "PLAY":
                play();
                break;
            case "PAUSE":
                pause();
                break;
            case "STOP":
                stop();
                break;
        }
        return Service.START_NOT_STICKY;
    }

    private void play(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mp == null){
                    mp = MediaPlayer.create(getApplicationContext(), R.raw.musica);
                    mp.setLooping(true);

                } else if (!mp.isPlaying()){
                    mp.seekTo(posicao);
                }

                mp.start();
            }
        };

        new Handler().post(runnable);
    }

    private void pause(){
        if (mp != null && mp.isPlaying()) {
            mp.pause();
            posicao = mp.getCurrentPosition();
        }
    }

    private void stop(){
        if (mp != null) {
            posicao = mp.getCurrentPosition();
            mp.stop();
            mp.release();
            mp = null;
            stopSelf();
        }


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
