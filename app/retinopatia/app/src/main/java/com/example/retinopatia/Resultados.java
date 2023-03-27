package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import DataBase.BaseDeDatos;
import DataBase.Informe;

public class Resultados extends AppCompatActivity {

    View root;
    Switch modoOscuro;
    ImageButton volver;
    ImageButton perfil;
    BaseDeDatos baseDeDatos;
    List<Informe> informes;
    Button retroceder;
    Button avanzar;
    TextView paginas;
    ImageView foto1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        root = findViewById(R.id.actividadResultados);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        retroceder = findViewById(R.id.botonRetroceder);
        avanzar = findViewById(R.id.botonAvanzar);
        paginas = findViewById(R.id.textoPaginas);
        foto1 = findViewById(R.id.imagen1);

        Intent intent = getIntent();
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        informes = baseDeDatos.getInformes(intent.getIntExtra("DNI",-1));

        if(informes == null){
            paginas.setText("1/1");
            paginas.setVisibility(View.INVISIBLE);
            retroceder.setVisibility(View.INVISIBLE);
            avanzar.setVisibility(View.INVISIBLE);
        }else{
            int pestañas = (int) informes.size()/3;
            pestañas += 1;
            if(pestañas == 1){
                paginas.setText("1/1");
                paginas.setVisibility(View.INVISIBLE);
                retroceder.setVisibility(View.INVISIBLE);
                avanzar.setVisibility(View.INVISIBLE);
//                Bitmap imagen = informes.get(0).getImagenDelInforme();
//                foto1.setImageBitmap(imagen);
            }else{
                paginas.setText("1/"+Integer.toString(pestañas));
            }
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
    public void botonModoOscuro(View v){
        int oscuro = getResources().getColor(R.color.background_darkmode_gray);
        int textoOscuro = getResources().getColor(R.color.background_gray);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            perfil.setColorFilter(oscuro);
            paginas.setTextColor(textoOscuro);
            retroceder.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            avanzar.setBackgroundTintList(ColorStateList.valueOf(oscuro));



        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            perfil.setColorFilter(claro);
            paginas.setTextColor(textoClaro);
            retroceder.setBackgroundTintList(ColorStateList.valueOf(claro));
            avanzar.setBackgroundTintList(ColorStateList.valueOf(claro));

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