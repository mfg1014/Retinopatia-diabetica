package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import DataBase.BaseDeDatos;


public class MainActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private TextView usuarioIncorrecto;
    private Button guestMode;
    private Button iniciarSesion;
    private Switch modoOscuro;
    private View root;
    private BaseDeDatos baseDeDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        usuarioIncorrecto = findViewById(R.id.error);
        modoOscuro = findViewById(R.id.switchModoOscuro);
        root = findViewById(R.id.actividadInicioSesion);
        guestMode = findViewById(R.id.botonInvitado);
        iniciarSesion = findViewById(R.id.botonIniciarSesion);
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
    }

    public void pasoInicio(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarPaciente.class);

        if(comprobarUsuario()){
            intent.putExtra("email",email.getText().toString());
            usuarioIncorrecto.setVisibility(View.INVISIBLE);
            intentModoOscuro(intent);
            startActivity(intent);
        }
        else {
            if(usuarioIncorrecto.getVisibility() == View.INVISIBLE){
                usuarioIncorrecto.setVisibility(View.VISIBLE);

            }

        }
    }
    public void iniciarSesionInvitado(View v){

        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intent.putExtra("DNI","invitado");
        intent.putExtra("email","invitado");
        intentModoOscuro(intent);
        startActivity(intent);
    }
    public boolean comprobarUsuario(){
        String emailUsuario = email.getText().toString();
        String contraseña = password.getText().toString();
        if(TextUtils.isEmpty(emailUsuario)|| TextUtils.isEmpty(contraseña)){
            return false;
        }
        if(baseDeDatos.getContraseña(emailUsuario).equals(contraseña) ){
            return true;
        }
        return false;
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
            email.setTextColor(textoOscuro);
            password.setTextColor(textoOscuro);
            modoOscuro.setTextColor(textoOscuro);
            email.setHintTextColor(textoOscuro);
            password.setHintTextColor(textoOscuro);
            guestMode.setTextColor(textoOscuro);
            iniciarSesion.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));



        }else{
            root.setBackgroundColor(claro);
            email.setTextColor(textoClaro);
            password.setTextColor(textoClaro);
            modoOscuro.setTextColor(textoClaro);
            email.setHintTextColor(textoClaro);
            password.setHintTextColor(textoClaro);
            guestMode.setTextColor(textoClaro);
            iniciarSesion.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
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