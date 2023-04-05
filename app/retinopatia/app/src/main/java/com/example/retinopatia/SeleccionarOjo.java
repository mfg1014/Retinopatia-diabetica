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


public class SeleccionarOjo extends AppCompatActivity {

    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;
    private Switch modoOscuro;
    private View root;
    private TextView mensaje;
    private TextView textoOjoDerecho;
    private TextView textoOjoIzquierdo;
    private ImageButton volver;
    private ImageButton perfil;
    private ImageButton botonOjoDerecho;
    private ImageButton botonOjoIzquierdo;
    private Button botonCambiarOrden;
    private int DNI;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ojo);

        inicializarVista();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
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
        intent.putExtra("email",email);
        startActivity(intent);

    }
    public void botonDerecho(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("ojo","derecho");
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    public void botonIzquierdo(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("ojo","izquierdo");
        intent.putExtra("DNI", DNI);
        startActivity(intent);


    }

    public void botonCambiarOrden(View v){
        int margenDerecho = botonOjoDerecho.getRight();;
        int margenIzquierdo = botonOjoDerecho.getLeft();
        botonOjoDerecho.setLeft(botonOjoIzquierdo.getLeft());
        botonOjoDerecho.setRight(botonOjoIzquierdo.getRight());
        textoOjoDerecho.setLeft(botonOjoIzquierdo.getLeft());
        textoOjoDerecho.setRight(botonOjoIzquierdo.getRight());
        botonOjoIzquierdo.setLeft(margenIzquierdo);
        botonOjoIzquierdo.setRight(margenDerecho);
        textoOjoIzquierdo.setLeft(margenIzquierdo);
        textoOjoIzquierdo.setRight(margenDerecho);

    }
    public void botonModoOscuro(View v){
        int color;
        int textColor;
        int buttonColor;

        if(modoOscuro.isChecked()){
            color = oscuro;
            textColor = textoOscuro;
            buttonColor = botonOscuro;
        }else{
            color = claro;
            textColor = textoClaro;
            buttonColor = botonClaro;
        }
        root.setBackgroundColor(color);
        volver.setBackgroundTintList(ColorStateList.valueOf(color));
        volver.setColorFilter(textColor);
        perfil.setBackgroundTintList(ColorStateList.valueOf(color));
        perfil.setColorFilter(textColor);
        mensaje.setTextColor(textColor);
        textoOjoDerecho.setTextColor(textColor);
        textoOjoIzquierdo.setTextColor(textColor);
        botonCambiarOrden.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    private void inicializarVista(){
        root = findViewById(R.id.actividadSeleccionarOjo);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        mensaje = findViewById(R.id.elegirOjo);
        textoOjoDerecho = findViewById(R.id.ojoDerechoTexto);
        textoOjoIzquierdo = findViewById(R.id.ojoIzquierdoTexto);
        botonOjoDerecho = findViewById(R.id.botonOjoDerecho);
        botonOjoIzquierdo = findViewById(R.id.botonOjoIzquierdo);
        botonCambiarOrden = findViewById(R.id.botonCambiarOrden);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

}