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

    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;
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
        inicializarVista();

        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
    }

    public void pasoInicio(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarPaciente.class);

        if(comprobarUsuario()){

            usuarioIncorrecto.setVisibility(View.INVISIBLE);
            intentModoOscuro(intent);
            intent.putExtra("email",email.getText().toString());
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
        baseDeDatos.addInvitado();
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
        email.setTextColor(textColor);
        password.setTextColor(textColor);
        modoOscuro.setTextColor(textColor);
        email.setHintTextColor(textColor);
        password.setHintTextColor(textColor);
        guestMode.setTextColor(textColor);
        iniciarSesion.setBackgroundTintList(ColorStateList.valueOf(buttonColor));


    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    private void inicializarVista(){
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        usuarioIncorrecto = findViewById(R.id.error);
        modoOscuro = findViewById(R.id.switchModoOscuro);
        root = findViewById(R.id.actividadInicioSesion);
        guestMode = findViewById(R.id.botonInvitado);
        iniciarSesion = findViewById(R.id.botonIniciarSesion);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

}