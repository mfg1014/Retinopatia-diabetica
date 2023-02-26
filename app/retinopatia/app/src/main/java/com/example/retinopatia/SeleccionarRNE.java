package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


public class SeleccionarRNE extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_rne);
        Button botonResultados = findViewById(R.id.botonObtenerResultados);
        botonResultados.setEnabled(false);
    }
    public void botonVolver(View v){
        finish();
    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        startActivity(intent);

    }
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        startActivity(intent);

    }
    public void opcionRNE1(View v){
        CheckBox RNE1 = findViewById(R.id.checkBoxRNE1);
        CheckBox RNE2 = findViewById(R.id.checkBoxRNE2);
        Button botonResultados = findViewById(R.id.botonObtenerResultados);
        if(RNE1.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE2.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }
    public void opcionRNE2(View v){
        CheckBox RNE1 = findViewById(R.id.checkBoxRNE1);
        CheckBox RNE2 = findViewById(R.id.checkBoxRNE2);
        Button botonResultados = findViewById(R.id.botonObtenerResultados);
        if(RNE2.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE1.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }
    public void opcionTodos(View v){
        CheckBox todos = findViewById(R.id.checkBoxTodas);
        CheckBox RNE1 = findViewById(R.id.checkBoxRNE1);
        CheckBox RNE2 = findViewById(R.id.checkBoxRNE2);
        Button botonResultados = findViewById(R.id.botonObtenerResultados);
        if(todos.isChecked()){

            RNE1.setChecked(true);
            RNE2.setChecked(true);
            RNE1.setEnabled(false);
            RNE2.setEnabled(false);
            botonResultados.setEnabled(true);

        }else{
            RNE1.setEnabled(true);
            RNE2.setEnabled(true);
        }
    }
}