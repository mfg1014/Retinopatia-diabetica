package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Foto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
    }
    public void botonVolver(View v){
        finish();
    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        startActivity(intent);

    }
    public void botonDerecho(View v){

        Intent intent = new Intent(v.getContext(), Ojos.class);
        startActivity(intent);

    }
    public void botonIzquierdo(View v){

        Intent intent = new Intent(v.getContext(), Ojos.class);
        startActivity(intent);

    }
    public void botonAmbos(View v){

        Intent intent = new Intent(v.getContext(), Ojos.class);
        startActivity(intent);

    }

}