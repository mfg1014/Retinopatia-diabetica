package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


public class SeleccionarRNE extends AppCompatActivity {
    Switch modoOscuro;
    View root;
    ImageButton volver;
    ImageButton perfil;
    Button botonResultados;
    CheckBox RNE1;
    CheckBox RNE2;
    CheckBox todos;
    TextView mensaje;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_rne);

        root = findViewById(R.id.actividadSeleccionarRNE);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonResultados = findViewById(R.id.botonObtenerResultados);
        botonResultados.setEnabled(false);
        RNE1 = findViewById(R.id.checkBoxRNE1);
        RNE2 = findViewById(R.id.checkBoxRNE2);
        todos = findViewById(R.id.checkBoxTodas);
        mensaje = findViewById(R.id.textViewMensajeRNE);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
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
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void opcionRNE1(View v){


        if(RNE1.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE2.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }
    public void opcionRNE2(View v){
        if(RNE2.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE1.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }
    public void opcionTodos(View v){
        if(todos.isChecked()){

            RNE1.setChecked(true);
            RNE2.setChecked(true);
            RNE1.setEnabled(false);
            RNE2.setEnabled(false);
            botonResultados.setEnabled(true);

        }else{
            RNE1.setEnabled(true);
            RNE2.setEnabled(true);
        }
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
            RNE1.setTextColor(textoOscuro);
            RNE2.setTextColor(textoOscuro);
            todos.setTextColor(textoOscuro);
            mensaje.setTextColor(textoOscuro);
            botonResultados.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(claro);
            RNE1.setTextColor(textoClaro);
            RNE2.setTextColor(textoClaro);
            todos.setTextColor(textoClaro);
            mensaje.setTextColor(textoClaro);
            botonResultados.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
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