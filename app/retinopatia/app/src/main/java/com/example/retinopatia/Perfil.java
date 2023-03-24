package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import DataBase.BaseDeDatos;

public class Perfil extends AppCompatActivity {

    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private TextView nombre;
    private TextView apellidos;
    private TextView fecha;
    private TextView DNI;
    private TextView centro;
    private TextView password;
    private BaseDeDatos baseDeDatos;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        root = findViewById(R.id.actividadPerfil);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        fecha = findViewById(R.id.fecha);
        DNI = findViewById(R.id.DNI);
        centro = findViewById(R.id.centro);
        password = findViewById(R.id.password);
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        cargarDatos();


    }
    public void botonVolver(View v){
        finish();
    }

    public void botonModoOscuro(View v){
        int oscuro = getResources().getColor(R.color.background_darkmode_gray);
        int textoOscuro = getResources().getColor(R.color.background_gray);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            volver.setColorFilter(textoOscuro);
            nombre.setTextColor(textoOscuro);
            apellidos.setTextColor(textoOscuro);
            fecha.setTextColor(textoOscuro);
            DNI.setTextColor(textoOscuro);
            centro.setTextColor(textoOscuro);
            password.setTextColor(textoOscuro);
        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            volver.setColorFilter(textoClaro);
            nombre.setTextColor(textoClaro);
            apellidos.setTextColor(textoClaro);
            fecha.setTextColor(textoClaro);
            DNI.setTextColor(textoClaro);
            centro.setTextColor(textoClaro);
            password.setTextColor(textoClaro);
        }

    }

    public void cargarDatos(){
        nombre.setText(baseDeDatos.getNombreMedico(email));
        apellidos.setText(baseDeDatos.getApellidoMedico(email));
        fecha.setText(baseDeDatos.getFechaNacimientoMedico(email));
        DNI.setText(Integer.toString( baseDeDatos.getDNIMedico(email)));
        password.setText(baseDeDatos.getContraseña(email));
        centro.setText(baseDeDatos.getCentroMedico(email));


    }
}