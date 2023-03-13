package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import DataBase.BaseDeDatos;

public class SeleccionarPaciente extends AppCompatActivity {

    Switch modoOscuro;
    View root;
    ImageButton volver;
    ImageButton perfil;
    EditText DNI;
    ImageButton botonBuscar;
    Button botonPasarSiguiente;
    TextView mensaje;
    BaseDeDatos baseDeDatos;
    int numeroDNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_paciente);
        root = findViewById(R.id.actividadSeleccionarPaciente);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonBuscar = findViewById(R.id.botonBuscar);
        botonPasarSiguiente = findViewById(R.id.botonEntrarMenu);
        DNI = findViewById(R.id.editTextDNI);
        mensaje = findViewById(R.id.textViewMensajeDNI);
        Intent intent = getIntent();
        intent.putExtra("email",intent.getStringExtra("email"));
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
    }
    public void botonNext (View v){
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intent.putExtra("DNI",numeroDNI);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonVolver(View v){
        finish();
    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
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
            DNI.setTextColor(textoOscuro);
            DNI.setHintTextColor(textoOscuro);
            botonBuscar.setColorFilter(textoOscuro);
            botonBuscar.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            botonPasarSiguiente.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(claro);
            DNI.setTextColor(textoClaro);
            DNI.setHintTextColor(textoClaro);
            botonBuscar.setColorFilter(textoClaro);
            botonBuscar.setBackgroundTintList(ColorStateList.valueOf(claro));
            botonPasarSiguiente.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
        }

    }
    public void botonBuscarPaciente(View v){
        try{
            String DNIPaciente = DNI.getText().toString();
            numeroDNI = Integer.parseInt(DNIPaciente);
            String nombrePaciente = baseDeDatos.getPaciente(numeroDNI);
            if(nombrePaciente.equals("")){
                mensaje.setTextColor(getResources().getColor(R.color.error_red));
                mensaje.setText("El paciente no se ha encontrado, ejemplo  de DNI: 12345678");

            }else{
                if(modoOscuro.isChecked()){
                    mensaje.setTextColor(getResources().getColor(R.color.background_gray));
                }else{
                    mensaje.setTextColor(getResources().getColor(R.color.black));
                }

                mensaje.setText("Paciente: "+nombrePaciente);
            }

        }catch (NumberFormatException ex){
            mensaje.setText("Inserte solo los numeros del DNI, ejemplo  de DNI: 12345678");
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