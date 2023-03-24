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

    private Switch modoOscuro;
    private View root;
    private ImageButton volver;
    private ImageButton perfil;
    private EditText DNI;
    private ImageButton botonBuscar;
    private Button botonPasarSiguiente;
    private TextView mensaje;
    private BaseDeDatos baseDeDatos;
    private int numeroDNI;
    private String email;

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
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
    }
    public void botonNext (View v){
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI",numeroDNI);
        startActivity(intent);

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
        int botonOscuro = getResources().getColor(R.color.background_green);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        int botonClaro = getResources().getColor(R.color.background_blue);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            volver.setColorFilter(textoOscuro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(textoOscuro));
            perfil.setColorFilter(oscuro);
            DNI.setTextColor(textoOscuro);
            DNI.setHintTextColor(textoOscuro);
            mensaje.setTextColor(textoOscuro);
            botonBuscar.setColorFilter(textoOscuro);
            botonBuscar.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            botonPasarSiguiente.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            volver.setColorFilter(textoClaro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(textoClaro));
            perfil.setColorFilter(claro);
            DNI.setTextColor(textoClaro);
            DNI.setHintTextColor(textoClaro);
            mensaje.setTextColor(textoClaro);
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