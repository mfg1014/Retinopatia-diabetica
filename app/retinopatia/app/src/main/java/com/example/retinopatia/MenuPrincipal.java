package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import DataBase.BaseDeDatos;
import DataBase.BaseDeDatosHelper;

/**
 * Clase MenuPrincipal, clase donde el usuario elige la opcion a realizar, se corresponde
 * a la actividad activity_menu_principal.
 */
public class MenuPrincipal extends AppCompatActivity {

    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;

    private Switch modoOscuro;
    private View root;
    private TextView nombrePaciente;
    private ImageButton volver;
    private ImageButton perfil;
    private Button datosPaciente;
    private Button fotoPaciente;
    private Button resultados;
    //private BaseDeDatos baseDeDatos;
    private int DNI;
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
        setContentView(R.layout.activity_menu_principal);

        inicializarVista();

        Intent intent = getIntent();
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        //baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        if(DNI == -1) {
            nombrePaciente.setVisibility(View.INVISIBLE);
            perfil.setVisibility(View.INVISIBLE);
            datosPaciente.setVisibility(View.INVISIBLE);
        }else{
            bbdd = baseDeDatosHelper.getReadableDatabase();
            String query = "SELECT * " +
                    "FROM usuarios LEFT JOIN pacientes "+
                    "ON usuarios.DNI = pacientes.dni_usuario " +
                    "WHERE usuarios.DNI = ?";

            Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNI)});


            System.out.println(cursor.getCount());
            String nombre = null;
            if (cursor.moveToFirst()) {
                nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                //nombre +=" " + cursor.getString(cursor.getColumnIndexOrThrow("apellido"));

            }
            cursor.close();
            bbdd.close();

            nombrePaciente.setText(nombre);
        }
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
     * Metodo que permite al usuario ir a la actividad activity_seleccionar_ojo donde el usuario
     * seguira el procedimiento para realizar un nuevo informe
     * @param v
     */
    public void botonFotoUsuario(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }

    /**
     * Metodo que permite al usuario ir a la actividad activity_resultados donde el usuario
     * podra ver los informes del paciente que se ha introducido anteriormente.
     * @param v
     */
    public void botonResultados(View v){

        Intent intent = new Intent(v.getContext(), Resultados.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }

    /**
     * Metodo que permite al usuario ir a la actividad activity_datos, donde se muestran los datos
     * del paciente introducido anteriormente.
     * @param v
     */
    public void botonDatos(View v){

        Intent intent = new Intent(v.getContext(), Datos.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    /**
     * Metodo utilizado para cambiar la interfaz de modo oscuro a modo claro.
     * @param v
     */
    public void botonModoOscuro(View v){
        int color;
        int textColor;
        int buttonColor;

        if(modoOscuro.isChecked()){
            color = oscuro;
            textColor = textoOscuro;
            buttonColor = botonOscuro;
        }else{
            color = claro;
            textColor = textoClaro;
            buttonColor = botonClaro;
        }
        root.setBackgroundColor(color);
        volver.setBackgroundTintList(ColorStateList.valueOf(color));
        volver.setColorFilter(textColor);
        perfil.setBackgroundTintList(ColorStateList.valueOf(color));
        perfil.setColorFilter(textColor);
        resultados.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        fotoPaciente.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        datosPaciente.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        nombrePaciente.setTextColor(textColor);
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
     * Metodo donde se inicializan los elementos de la actividad y los colores entre los que puede cambiar
     *
     */
    private void inicializarVista(){
        nombrePaciente = findViewById(R.id.nombrePaciente);
        datosPaciente = findViewById(R.id.datosPaciente);
        root = findViewById(R.id.actividadMenuPrincipal);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        fotoPaciente = findViewById(R.id.botonFoto);
        resultados = findViewById(R.id.botonResultados);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

}