package com.example.isonidos;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.VideoView;

import java.lang.reflect.Field;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    HashMap<String, String> listaSonidos=new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout principal =  (LinearLayout) findViewById(R.id.botones);
        int numeroLinea = 0;
        LinearLayout auxiliar = creaLineaBotones(numeroLinea);
        principal.addView(auxiliar);

        Field[] listaCanciones = R.raw.class.getFields();//creamos un array con la cantidad de archivos que halla en el raw

        int columnas = 5;

        for (int i=0; i< listaCanciones.length; i++) {

            Button b = creaBoton(i, listaCanciones);//creamos un botón por código y lo añadimos a la pantalla principal

            auxiliar.addView(b);//añadimos el botón al layout

            if ( i % columnas == columnas-1){
                auxiliar = creaLineaBotones(i);
                principal.addView(auxiliar);
            }
            listaSonidos.put(b.getTag().toString(), b.getText().toString());
            b.setText(acortaEtiquetaBoton((b.getText().toString())));
        }
    }

    private LinearLayout creaLineaBotones(int numeroLinea){
        LinearLayout.LayoutParams parametros = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT
                ,LinearLayout.LayoutParams.WRAP_CONTENT);
        parametros.weight = 1;
        LinearLayout linea = new LinearLayout(this);

        linea.setOrientation(LinearLayout.HORIZONTAL);
        linea.setLayoutParams(parametros);
        linea.setId(numeroLinea);
        return linea;
    }

    private Button creaBoton(int i, Field[] _listaCanciones){
        LinearLayout.LayoutParams parametrosBotones = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT);
        parametrosBotones.weight = 1;
        parametrosBotones.setMargins(5, 5, 5, 5);
        parametrosBotones.gravity = Gravity.CENTER_HORIZONTAL;
        Button b = new Button(this);
        b.setLayoutParams(parametrosBotones);
        b.setText(_listaCanciones[i].getName());
        b.setTextColor(Color.WHITE);//pongo color blanco a las letras
        b.setTextSize(10);
        b.setBackgroundColor(Color.BLUE);//si es sonido pongo color azul
        b.setAllCaps(true); //todas las letras del botón en mayúscula/minúscula
        int id = this.getResources().getIdentifier(_listaCanciones[i].getName(), "raw", this.getPackageName());
        String nombreLargo =  _listaCanciones[i].getName();

        if (nombreLargo!= null && nombreLargo.substring(0,2).contains("v_")) {//si es video le pongo color naranja
            b.setBackgroundColor(Color.rgb(255,140,0));
        }
        b.setTag(id);
        b.setId(i+50);
        b.setOnClickListener(new View.OnClickListener() {
            @Override

                public void onClick(View view) {
                    sonido(view);
                }
            });

        return b;
    }

    public void sonido(View view){//reproducimos los archivos

        Button b = (Button) findViewById(view.getId());
        String nombre = listaSonidos.get(view.getTag().toString());

        if (nombre.substring(0,2).contains("v_")) {//si es un video entra aqui

            VideoView videoview = (VideoView) findViewById(R.id.videoView);
            Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+view.getTag());
            videoview.setVideoURI(uri);
            videoview.start();
        } else {//si no entra aqui ya que es un sonido

            MediaPlayer m = new MediaPlayer();
            m = MediaPlayer.create(this, (int) findViewById(view.getId()).getTag());
            m.start();
            m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override

                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    if (mediaPlayer != null) {
                        mediaPlayer.release();//libera la memoria del sonido
                    }
                }
            });
        }
    }

    private String acortaEtiquetaBoton(String s){//quita las v_ del principio
        if (s.substring(0,2).contains("v_")) {
            s = s.substring(s.indexOf('_')+1);
        }
        if (s.contains("_")) {s = s.substring(s.indexOf('_'));}//cambia los guines por espacios
        s = s.replace('_',' ');
        return s;
    }

}
