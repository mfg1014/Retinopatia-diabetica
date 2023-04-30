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

import DataBase.BaseDeDatos;
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
    //private BaseDeDatos baseDeDatos;
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
        //baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        cargarDatos();


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
    public void cargarDatos(){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT usuarios.nombre, usuarios.apellido, usuarios.fecha_nacimiento,usuarios.DNI,medicos.centro_medico,medicos.contrasena " +
                "FROM usuarios "+
                "LEFT JOIN medicos ON usuarios.DNI = medicos.dni_usuario " +
                "WHERE usuarios.correo = ?";
        Cursor cursor = bbdd.rawQuery(query,new String[]{email});
        String nombrePaciente = null;
        String apellidosPaciente = null;
        String fechaPaciente = null;
        String DNIString = null;
        String centroMedico = null;
        String contrasena = null;
        if(cursor.moveToFirst()){
            nombrePaciente = cursor.getString(0);
            apellidosPaciente = cursor.getString(1);
            fechaPaciente = cursor.getString(2);
            DNIString = cursor.getString(3);
            centroMedico = cursor.getString(4);
            contrasena = cursor.getString(5);

        }
        cursor.close();
        nombre.setText(nombrePaciente);
        apellidos.setText(apellidosPaciente);
        fecha.setText(fechaPaciente);
        DNI.setText(DNIString);
        centro.setText(centroMedico);
        password.setText(contrasena);

        nombre.setVisibility(View.VISIBLE);
        apellidos.setVisibility(View.VISIBLE);
        fecha.setVisibility(View.VISIBLE);
        DNI.setVisibility(View.VISIBLE);
        centro.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        bbdd.close();


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