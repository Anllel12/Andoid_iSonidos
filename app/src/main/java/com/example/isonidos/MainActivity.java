package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void reproduceSonido (View vista){
        MediaPlayer m = new MediaPlayer();
        int idSonido = this.getResources().getIdentifier(vista.getTag().toString(), "raw", this.getPackageName());
        m = MediaPlayer.create(this, idSonido);
        m.start();
        m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {//cuando termina el sonido
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.stop();
                if (mediaPlayer != null) {
                    mediaPlayer.release();//lo libera de la memoria
                }
            }
        });
    }
}
