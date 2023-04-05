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

    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;

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
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        inicializarVista();

        Intent intent = getIntent();
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }

        if(DNI == -1) {
            nombrePaciente.setVisibility(View.INVISIBLE);
            perfil.setVisibility(View.INVISIBLE);
            datosPaciente.setVisibility(View.INVISIBLE);
        }else{
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
        intent.putExtra("email",email);
        startActivity(intent);

    }
    public void botonFotoUsuario(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), Resultados.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    public void botonDatos(View v){

        Intent intent = new Intent(v.getContext(), Datos.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

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
        resultados.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        fotoPaciente.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        datosPaciente.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        nombrePaciente.setTextColor(textColor);


    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    private void inicializarVista(){
        nombrePaciente = findViewById(R.id.nombrePaciente);
        datosPaciente = findViewById(R.id.datosPaciente);
        root = findViewById(R.id.actividadMenuPrincipal);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        fotoPaciente = findViewById(R.id.botonFoto);
        resultados = findViewById(R.id.botonResultados);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

}