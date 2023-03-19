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

public class SeleccionarOjo extends AppCompatActivity {

    private Switch modoOscuro;
    private View root;
    private TextView mensaje;
    private TextView textoOjoDerecho;
    private TextView textoOjoIzquierdo;
    private ImageButton volver;
    private ImageButton perfil;
    private ImageButton botonOjoDerecho;
    private ImageButton botonOjoIzquierdo;
    private int DNI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccionar_ojo);
        root = findViewById(R.id.actividadSeleccionarOjo);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        mensaje = findViewById(R.id.elegirOjo);
        textoOjoDerecho = findViewById(R.id.ojoDerechoTexto);
        textoOjoIzquierdo = findViewById(R.id.ojoIzquierdoTexto);
        botonOjoDerecho = findViewById(R.id.botonOjoDerecho);
        botonOjoIzquierdo = findViewById(R.id.botonOjoIzquierdo);

        Intent intent = getIntent();
        DNI = intent.getIntExtra("DNI",-1);
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
    }
    public void botonVolver(View v){

        finish();
    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        intentModoOscuro(intent);
        startActivity(intent);

    }
    public void botonDerecho(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("ojo","derecho");
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    public void botonIzquierdo(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("ojo","izquierdo");
        intent.putExtra("DNI", DNI);
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
            mensaje.setTextColor(textoOscuro);
            textoOjoDerecho.setTextColor(textoOscuro);
            textoOjoIzquierdo.setTextColor(textoOscuro);

        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(claro);
            mensaje.setTextColor(textoClaro);
            textoOjoDerecho.setTextColor(textoClaro);
            textoOjoIzquierdo.setTextColor(textoClaro);
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