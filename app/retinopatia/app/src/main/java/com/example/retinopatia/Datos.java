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
 * Clase Datos, la cual corresponde a la actividad activity_datos
 */
public class Datos extends AppCompatActivity {
    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
    private TextView nombre;
    private TextView apellidos;
    private TextView fecha;
    private TextView DNI;
    private TextView centro;
    private TextView estado;
    private TextView infoPaciente;
    //private BaseDeDatos baseDeDatos;
    private int DNIPaciente;
    private String email;

    /**
     * Metodo onCreate, llamado al iniciar la actividad, en este metodo, se inicializa la vista,
     * de forma que el usuario pueda interactuar bien con la interfaz.
     * Ademas, se cargan los datos del paciente sobre los textos correspondientes.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos);
        inicializarVista();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNIPaciente = intent.getIntExtra("DNI",-1);
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        //baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
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
     * Metodo que permite al usuario ir a la actividad activity_perfil donde se muestran los datos
     * del medico.
     * @param v
     */
    public void botonPerfil(View v){
        Intent intent = new Intent(v.getContext(), Perfil.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        startActivity(intent);
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
        perfil.setBackgroundTintList(ColorStateList.valueOf(color));
        perfil.setColorFilter(textColor);
        nombre.setTextColor(textColor);
        apellidos.setTextColor(textColor);
        fecha.setTextColor(textColor);
        DNI.setTextColor(textColor);
        //centro.setTextColor(textoOscuro);
        estado.setTextColor(textColor);
        infoPaciente.setTextColor(textColor);
    }

    /**
     * Metodo que comprueba antes de ir a otra actividad si el modoOscuro esta activado,
     * para activarlo en la siguiente actividad tambien.
     * @param intent
     */
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }

    /**
     * Metodo que carga los datos del paciente, en sus respectivos campos.
     */
    public void cargarDatos(){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT usuarios.nombre, usuarios.apellido, usuarios.fecha_nacimiento,usuarios.DNI,pacientes.estado,pacientes.informacion " +
                "FROM usuarios "+
                "LEFT JOIN pacientes ON usuarios.DNI = pacientes.dni_usuario " +
                "WHERE usuarios.DNI = ?";
        Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNIPaciente)});
        String nombrePaciente = null;
        String apellidosPaciente = null;
        String fechaPaciente = null;
        String DNIString = null;
        String estadoPaciente = null;
        String informacionPaciente = null;
        if(cursor.moveToFirst()){
            nombrePaciente = cursor.getString(0);
            apellidosPaciente = cursor.getString(1);
            fechaPaciente = cursor.getString(2);
            DNIString = cursor.getString(3);
            estadoPaciente = cursor.getString(4);
            informacionPaciente = cursor.getString(5);

        }
        cursor.close();
        nombre.setText(nombrePaciente);
        apellidos.setText(apellidosPaciente);
        fecha.setText(fechaPaciente);
        DNI.setText(DNIString);
        estado.setText(estadoPaciente);
        infoPaciente.setText(informacionPaciente);

        nombre.setVisibility(View.VISIBLE);
        apellidos.setVisibility(View.VISIBLE);
        fecha.setVisibility(View.VISIBLE);
        DNI.setVisibility(View.VISIBLE);
        estado.setVisibility(View.VISIBLE);
        infoPaciente.setVisibility(View.VISIBLE);
        bbdd.close();
    }

    /**
     * Metodo donde se inicializan los elementos de la actividad y los colores entre los que puede cambiar
     *
     */
    private void inicializarVista(){
        root = findViewById(R.id.actividadDatos);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        nombre = findViewById(R.id.nombre);
        apellidos = findViewById(R.id.apellidos);
        fecha = findViewById(R.id.fecha);
        DNI = findViewById(R.id.DNI);
        //centro = findViewById(R.id.centro);
        estado = findViewById(R.id.estado);
        infoPaciente = findViewById(R.id.infoPaciente);

        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);

    }

}