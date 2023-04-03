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

public class Datos extends AppCompatActivity {
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
    private TextView nombre;
    private TextView apellidos;
    private TextView fecha;
    private TextView DNI;
    private TextView centro;
    private TextView estado;
    private TextView infoPaciente;
    private BaseDeDatos baseDeDatos;
    private int DNIPaciente;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        root = findViewById(R.id.actividadDatos);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        fecha = findViewById(R.id.fecha);
        DNI = findViewById(R.id.DNI);
        //centro = findViewById(R.id.centro);
        estado = findViewById(R.id.estado);
        infoPaciente = findViewById(R.id.infoPaciente);
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNIPaciente = intent.getIntExtra("DNI",-1);
        if(DNIPaciente == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        cargarDatos();

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

    public void botonModoOscuro(View v){
        int oscuro = getResources().getColor(R.color.background_darkmode_gray);
        int textoOscuro = getResources().getColor(R.color.background_gray);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            volver.setColorFilter(textoOscuro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            perfil.setColorFilter(textoOscuro);
            nombre.setTextColor(textoOscuro);
            apellidos.setTextColor(textoOscuro);
            fecha.setTextColor(textoOscuro);
            DNI.setTextColor(textoOscuro);
            //centro.setTextColor(textoOscuro);
            estado.setTextColor(textoOscuro);
            infoPaciente.setTextColor(textoOscuro);

        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            volver.setColorFilter(textoClaro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(textoClaro);
            nombre.setTextColor(textoClaro);
            apellidos.setTextColor(textoClaro);
            fecha.setTextColor(textoClaro);
            DNI.setTextColor(textoClaro);
            //centro.setTextColor(textoClaro);
            estado.setTextColor(textoClaro);
            infoPaciente.setTextColor(textoClaro);
        }

    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    public void cargarDatos(){
        nombre.setText(baseDeDatos.getNombrePaciente(DNIPaciente));
        apellidos.setText(baseDeDatos.getApellidoPaciente(DNIPaciente));
        fecha.setText(baseDeDatos.getFechaNacimientoPaciente(DNIPaciente));
        DNI.setText(Integer.toString(DNIPaciente));
        estado.setText(baseDeDatos.getEstado(DNIPaciente));
        infoPaciente.setText(baseDeDatos.getInformacionPaciente(DNIPaciente));



    }

}