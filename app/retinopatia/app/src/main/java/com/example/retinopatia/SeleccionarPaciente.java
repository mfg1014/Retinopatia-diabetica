package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SeleccionarPaciente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_paciente);
    }
    public void botonNext (View v){
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        startActivity(intent);

    }
    public void botonVolver(View v){
        finish();
    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        startActivity(intent);

    }
}