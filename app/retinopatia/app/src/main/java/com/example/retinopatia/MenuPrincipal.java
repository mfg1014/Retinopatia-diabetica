package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

public class MenuPrincipal extends AppCompatActivity {

    Switch modoOscuro;
    View root;
    TextView nombrePaciente;
    ImageButton volver;
    ImageButton perfil;
    Button datosPaciente;
    Button fotoPaciente;
    Button resultados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Intent intent = getIntent();
        nombrePaciente = findViewById(R.id.nombrePaciente);
        datosPaciente = findViewById(R.id.datosPaciente);
        root = findViewById(R.id.actividadMenuPrincipal);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        fotoPaciente = findViewById(R.id.botonFoto);
        resultados = findViewById(R.id.botonResultados);
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        if(intent.getStringExtra("DNI").equals("invitado")) {
            nombrePaciente.setVisibility(View.INVISIBLE);

            datosPaciente.setVisibility(View.INVISIBLE);
        }else{
            nombrePaciente.setText(intent.getStringExtra("usuario"));
        }
    }

    public void botonVolver(View v){

        finish();

    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonFotoUsuario(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), Resultados.class);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonDatos(View v){

        Intent intent = new Intent(v.getContext(), Datos.class);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonModoOscuro(View v){
        int oscuro = getResources().getColor(R.color.background_darkmode_gray);
        int textoOscuro = getResources().getColor(R.color.background_gray);
        int botonOscuro = getResources().getColor(R.color.background_green);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        int botonClaro = getResources().getColor(R.color.background_blue);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            perfil.setColorFilter(oscuro);
            resultados.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
            fotoPaciente.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
            datosPaciente.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
            nombrePaciente.setTextColor(textoOscuro);

        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(claro);
            resultados.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
            fotoPaciente.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
            datosPaciente.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
            nombrePaciente.setTextColor(textoClaro);
        }

    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }

}