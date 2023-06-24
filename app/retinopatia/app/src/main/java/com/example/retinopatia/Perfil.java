package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import DataBase.BaseDeDatosHelper;

/**
 * Clase Perfil, clase donde el usuario puede ver los datos del medico, se corresponde
 * a la actividad activity_perfil.
 */
public class Perfil extends AppCompatActivity {

    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private TextView nombre;
    private TextView apellidos;
    private TextView fecha;
    private TextView DNI;
    private TextView centro;
    private TextView password;

    private String email;

    /**
     * Metodo onCreate, llamado al iniciar la actividad, en este metodo, se inicializa la vista,
     * de forma que el usuario pueda interactuar bien con la interfaz.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        inicializarVista();
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        obtenerDatosBaseDatos();
    }
    /**
     * Metodo utilizado para volver a la actividad anterior.
     * @param v
     */
    public void botonVolver(View v){
        finish();
    }
    /**
     * Metodo utilizado para cambiar la interfaz de modo oscuro a modo claro.
     * @param v
     */
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
        nombre.setTextColor(textColor);
        apellidos.setTextColor(textColor);
        fecha.setTextColor(textColor);
        DNI.setTextColor(textColor);
        centro.setTextColor(textColor);
        password.setTextColor(textColor);
    }

    /**
     * Metodo que carga los datos relacionados con el medico
     */
    public void obtenerDatosBaseDatos(){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT usuarios.nombre, usuarios.apellido, usuarios.fecha_nacimiento,usuarios.DNI,medicos.centro_medico,medicos.contrasena " +
                "FROM usuarios "+
                "LEFT JOIN medicos ON usuarios.DNI = medicos.dni_usuario " +
                "WHERE usuarios.correo = ?";
        Cursor cursor = bbdd.rawQuery(query,new String[]{email});
        List<String> datosMedico = new ArrayList<String>();
        if(cursor.moveToFirst()){
            datosMedico.add(cursor.getString(0));
            datosMedico.add(cursor.getString(1));
            datosMedico.add(cursor.getString(2));
            datosMedico.add(cursor.getString(3));
            datosMedico.add(cursor.getString(4));
            datosMedico.add(cursor.getString(5));
        }
        cursor.close();
        bbdd.close();
        mostrarDatos(datosMedico);
    }

    private void mostrarDatos(List<String> datosMedico ) {

        nombre.setText("Nombre: "+datosMedico.get(0));
        apellidos.setText("Apellidos: "+datosMedico.get(1));
        fecha.setText("Fecha de nacimiento: "+datosMedico.get(2));
        DNI.setText("DNI: "+datosMedico.get(3));
        centro.setText("Centro sanitario: "+datosMedico.get(4));
        password.setText("Contraseña: "+datosMedico.get(5));

        nombre.setVisibility(View.VISIBLE);
        apellidos.setVisibility(View.VISIBLE);
        fecha.setVisibility(View.VISIBLE);
        DNI.setVisibility(View.VISIBLE);
        centro.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
    }

    /**
     * Metodo donde se inicializan los elementos de la actividad y los colores entre los que puede cambiar
     *
     */
    private void inicializarVista(){
        root = findViewById(R.id.actividadPerfil);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        fecha = findViewById(R.id.fecha);
        DNI = findViewById(R.id.DNI);
        centro = findViewById(R.id.centro);
        password = findViewById(R.id.password);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
    }
}