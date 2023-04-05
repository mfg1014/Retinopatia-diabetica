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
    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
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
        inicializarVista();
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
        perfil.setBackgroundTintList(ColorStateList.valueOf(color));
        perfil.setColorFilter(textColor);
        nombre.setTextColor(textColor);
        apellidos.setTextColor(textColor);
        fecha.setTextColor(textColor);
        DNI.setTextColor(textColor);
        //centro.setTextColor(textoOscuro);
        estado.setTextColor(textColor);
        infoPaciente.setTextColor(textColor);
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
    private void inicializarVista(){
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
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);

    }

}