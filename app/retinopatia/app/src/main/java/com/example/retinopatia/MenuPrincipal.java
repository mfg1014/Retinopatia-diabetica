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

import DataBase.BaseDeDatos;

public class MenuPrincipal extends AppCompatActivity {

    private Switch modoOscuro;
    private View root;
    private TextView nombrePaciente;
    private ImageButton volver;
    private ImageButton perfil;
    private Button datosPaciente;
    private Button fotoPaciente;
    private Button resultados;
    private BaseDeDatos baseDeDatos;
    private int DNI;

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
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        if(intent.getIntExtra("DNI",-1) == -1) {
            nombrePaciente.setVisibility(View.INVISIBLE);
            perfil.setVisibility(View.INVISIBLE);
            datosPaciente.setVisibility(View.INVISIBLE);
        }else{
            DNI = intent.getIntExtra("DNI",-1);
            String nombre = baseDeDatos.getPaciente(DNI);
            nombrePaciente.setText(nombre);
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
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), Resultados.class);
        intentModoOscuro(intent);
        intent.putExtra("DNI", DNI);
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