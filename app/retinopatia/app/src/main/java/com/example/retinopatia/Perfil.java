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

    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
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
        inicializarVista();
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
        int color;
        int textColor;

        if(modoOscuro.isChecked()){
            color = oscuro;
            textColor = textoOscuro;
        }else{
            color = claro;
            textColor = textoClaro;
        }

        root.setBackgroundColor(color);
        volver.setBackgroundTintList(ColorStateList.valueOf(color));
        volver.setColorFilter(textColor);
        nombre.setTextColor(textColor);
        apellidos.setTextColor(textColor);
        fecha.setTextColor(textColor);
        DNI.setTextColor(textColor);
        centro.setTextColor(textColor);
        password.setTextColor(textColor);

    }

    public void cargarDatos(){
        nombre.setText(baseDeDatos.getNombreMedico(email));
        apellidos.setText(baseDeDatos.getApellidoMedico(email));
        fecha.setText(baseDeDatos.getFechaNacimientoMedico(email));
        DNI.setText(Integer.toString( baseDeDatos.getDNIMedico(email)));
        password.setText(baseDeDatos.getContraseña(email));
        centro.setText(baseDeDatos.getCentroMedico(email));
    }
    private void inicializarVista(){
        root = findViewById(R.id.actividadPerfil);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        fecha = findViewById(R.id.fecha);
        DNI = findViewById(R.id.DNI);
        centro = findViewById(R.id.centro);
        password = findViewById(R.id.password);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
    }
}