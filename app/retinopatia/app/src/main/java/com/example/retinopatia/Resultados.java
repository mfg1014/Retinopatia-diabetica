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
    private int pestañas;
    private int pestañaActual;
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
        foto2 = findViewById(R.id.imagen2);
        foto3 = findViewById(R.id.imagen3);

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
            cargarFotos(pestañaActual);

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
    public void botonAvanzar(View v){
        pestañaActual += 1;
        cargarFotos(pestañaActual);

    }
    public void botonRetroceder(View v){
        pestañaActual -= 1;
        cargarFotos(pestañaActual);

    }

    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
    public void cargarFotos(int pestañaActual){
        int primeraFoto = (pestañaActual - 1) * 3;
        if(informes.size() > primeraFoto) {
            Bitmap imagen = informes.get(informes.size()-primeraFoto-1).getImagenDelInforme();
            foto1.setImageBitmap(imagen);
            foto1.setVisibility(View.VISIBLE);
            if(informes.size()>(pestañaActual - 1) * 3 + 1) {
                Bitmap imagen2 = informes.get(informes.size()-primeraFoto-2).getImagenDelInforme();
                foto2.setImageBitmap(imagen2);
                foto2.setVisibility(View.VISIBLE);
                if(informes.size()>(pestañaActual - 1) * 3 + 2) {
                    Bitmap imagen3 = informes.get(informes.size()-primeraFoto-3).getImagenDelInforme();
                    foto3.setImageBitmap(imagen3);
                    foto3.setVisibility(View.VISIBLE);
                }else{
                    foto3.setVisibility(View.INVISIBLE);
                }
            }else{
                foto2.setVisibility(View.INVISIBLE);
                foto3.setVisibility(View.INVISIBLE);
            }
        }else{
            foto1.setVisibility(View.INVISIBLE);
            foto2.setVisibility(View.INVISIBLE);
            foto3.setVisibility(View.INVISIBLE);
        }
    }
}