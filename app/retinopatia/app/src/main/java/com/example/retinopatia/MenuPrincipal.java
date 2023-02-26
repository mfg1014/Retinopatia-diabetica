package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        Intent intent = getIntent();
        TextView nombrePaciente = (TextView) findViewById(R.id.nombrePaciente);
        if(intent.getStringExtra("DNI").equals("invitado")) {
            nombrePaciente.setVisibility(View.INVISIBLE);
            Button datosPaciente = (Button) findViewById(R.id.datosPaciente);
            datosPaciente.setVisibility(View.INVISIBLE);
        }else{
            nombrePaciente.setText(intent.getStringExtra("usuario"));
        }
    }

    public void botonVolver(View v){

        finish();

    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        startActivity(intent);

    }
    public void botonFotoUsuario(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        startActivity(intent);

    }
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), Resultados.class);
        startActivity(intent);

    }
    public void botonDatos(View v){

        Intent intent = new Intent(v.getContext(), Datos.class);
        startActivity(intent);

    }

}