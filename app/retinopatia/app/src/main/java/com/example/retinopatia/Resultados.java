package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
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

    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
    private BaseDeDatos baseDeDatos;
    private List<Informe> informes;
    private Button retroceder;
    private Button avanzar;
    private TextView paginas;
    private ImageView foto1;
    private ImageView foto2;
    private ImageView foto3;
    private TextView fecha1;
    private TextView fecha2;
    private TextView fecha3;
    private TextView informacion1;
    private TextView informacion2;
    private TextView informacion3;
    private TextView resultados1;
    private TextView resultados2;
    private TextView resultados3;
    private int pestañas;
    private int pestañaActual;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        inicializarVista();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        int DNI = intent.getIntExtra("DNI",-1);
        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        informes = baseDeDatos.getInformes(DNI);
        inicializarInformes();

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
        paginas.setTextColor(textColor);
        retroceder.setBackgroundTintList(ColorStateList.valueOf(color));
        avanzar.setBackgroundTintList(ColorStateList.valueOf(color));
    }
    public void botonAvanzar(View v){
        pestañaActual += 1;
        cargarInformes(pestañaActual);


    }
    public void botonRetroceder(View v){
        pestañaActual -= 1;
        cargarInformes(pestañaActual);


    }

    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    public void cargarInformes(int pestañaActual){
        int primerInforme = (pestañaActual - 1) * 3;
        for(int i = 0; i < 3;i++){
            if(informes.size() > primerInforme + i) {
                switchInforme(primerInforme, i);
            }else{
                switch(i){
                    case 0:
                        foto1.setVisibility(View.INVISIBLE);
                        fecha1.setVisibility(View.INVISIBLE);
                        informacion1.setVisibility(View.INVISIBLE);
                        resultados1.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        foto2.setVisibility(View.INVISIBLE);
                        fecha2.setVisibility(View.INVISIBLE);
                        informacion2.setVisibility(View.INVISIBLE);
                        resultados2.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        foto3.setVisibility(View.INVISIBLE);
                        fecha3.setVisibility(View.INVISIBLE);
                        informacion3.setVisibility(View.INVISIBLE);
                        resultados3.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }
    }
    public void switchInforme(int primerInforme, int n){
        switch (n){
            case 0:
                visibilizarInforme1(informes.size()-primerInforme-1);
                break;
            case 1:
                visibilizarInforme2(informes.size()-primerInforme-2);
                break;
            case 2:
                visibilizarInforme3(informes.size()-primerInforme-3);
                break;
        }
    }
    public void visibilizarInforme1(int informe){
        Bitmap imagen = informes.get(informe).getImagenDelInforme();
        foto1.setImageBitmap(imagen);
        foto1.setVisibility(View.VISIBLE);
        fecha1.setText(informes.get(informe).getFecha().toString());
        informacion1.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados1.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha1.setVisibility(View.VISIBLE);
        informacion1.setVisibility(View.VISIBLE);
        resultados1.setVisibility(View.VISIBLE);
    }
    public void visibilizarInforme2(int informe){

        Bitmap imagen2 = informes.get(informe).getImagenDelInforme();
        foto2.setImageBitmap(imagen2);
        foto2.setVisibility(View.VISIBLE);
        fecha2.setText(informes.get(informe).getFecha().toString());
        informacion2.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados2.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha2.setVisibility(View.VISIBLE);
        informacion2.setVisibility(View.VISIBLE);
        resultados2.setVisibility(View.VISIBLE);

    }
    public void visibilizarInforme3(int informe){
        Bitmap imagen3 = informes.get(informe).getImagenDelInforme();
        foto3.setImageBitmap(imagen3);
        foto3.setVisibility(View.VISIBLE);
        fecha3.setText(informes.get(informe).getFecha().toString());
        informacion3.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados3.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha3.setVisibility(View.VISIBLE);
        informacion3.setVisibility(View.VISIBLE);
        resultados3.setVisibility(View.VISIBLE);
    }

    private void inicializarVista(){
        root = findViewById(R.id.actividadResultados);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        retroceder = findViewById(R.id.botonRetroceder);
        avanzar = findViewById(R.id.botonAvanzar);
        paginas = findViewById(R.id.textoPaginas);
        foto1 = findViewById(R.id.imagen1);
        foto2 = findViewById(R.id.imagen2);
        foto3 = findViewById(R.id.imagen3);
        fecha1 = findViewById(R.id.fecha1);
        informacion1 = findViewById(R.id.info1);
        resultados1 = findViewById(R.id.result1);
        fecha2 = findViewById(R.id.fecha2);
        informacion2 = findViewById(R.id.info2);
        resultados2 = findViewById(R.id.result2);
        fecha3 = findViewById(R.id.fecha3);
        informacion3 = findViewById(R.id.info3);
        resultados3 = findViewById(R.id.result3);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
    }
    private void inicializarInformes(){
        if(informes == null){
            paginas.setText("1/1");
            paginas.setVisibility(View.INVISIBLE);
            retroceder.setVisibility(View.INVISIBLE);
            avanzar.setVisibility(View.INVISIBLE);
        }else{
            pestañas = (int) (informes.size()-1)/3;
            pestañas += 1;
            pestañaActual = 1;
            if(pestañas == 1){
                paginas.setText("1/1");
                paginas.setVisibility(View.INVISIBLE);
                retroceder.setVisibility(View.INVISIBLE);
                avanzar.setVisibility(View.INVISIBLE);

            }else{
                paginas.setText("1/"+Integer.toString(pestañas));
            }
            cargarInformes(pestañaActual);
        }
    }
}