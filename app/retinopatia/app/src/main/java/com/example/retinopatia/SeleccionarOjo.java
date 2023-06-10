package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.Switch;
import android.widget.TextView;

import DataBase.BaseDeDatosHelper;

/**
 * Clase SeleccionarOjo, clase donde el usuario puede seleccionar el ojo al que se va a realizar un estudio,
 * se corresponde a la actividad activity_seleccionar_ojo.
 */
public class SeleccionarOjo extends AppCompatActivity {

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
    private TextView mensaje;
    private TextView textoOjoDerecho;
    private TextView textoOjoIzquierdo;
    private ImageButton volver;
    private ImageButton perfil;
    private ImageButton botonOjoDerecho;
    private ImageButton botonOjoIzquierdo;
    private Button botonCambiarOrden;
    private int DNI;
    private String email;
    private boolean izquierdoPrimero;
    private int DNIMedico;


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
        setContentView(R.layout.activity_seleccionar_ojo);
        inicializarVista();
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }

        prioridadOjo();
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
     * Metodo que permite al usuario indicar que la foto que se va a realizar es del ojo derecho,
     * manda al usuario a la actividad activity_foto
     * @param v
     */
    public void botonDerecho(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("ojo","derecho");
        intent.putExtra("DNI", DNI);
        startActivity(intent);

    }
    /**
     * Metodo que permite al usuario indicar que la foto que se va a realizar es del ojo izquierdo,
     * manda al usuario a la actividad activity_foto
     * @param v
     */
    public void botonIzquierdo(View v){

        Intent intent = new Intent(v.getContext(), Foto.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("ojo","izquierdo");
        intent.putExtra("DNI", DNI);
        startActivity(intent);
    }
    /**
     * Metodo que permite al usuario guardar el orden de los ojos, de forma que sea mas facil
     * mentalmente seleccionar el ojo.
     * @param v
     */

    public void botonCambiarOrden(View v){
        izquierdoPrimero = !izquierdoPrimero;
        int valor = (izquierdoPrimero ? 1 : 0);
        bbdd = baseDeDatosHelper.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("prioridad_ojo",valor);
        bbdd.update("medicos", valores, "dni_usuario = ?", new String[] {String.valueOf(DNIMedico)});
        valores.clear();
        bbdd.close();

        cambiarOrden();

    }

    /**
     * Metodo que cambia el orden de los botones y sus respectivos textos.
     * Utilizado cuando se ha pulsado el boton de cambiar orden.
     */
    private void cambiarOrden() {
        ViewGroup.LayoutParams LP = botonOjoDerecho.getLayoutParams();
        botonOjoDerecho.setLayoutParams(botonOjoIzquierdo.getLayoutParams());
        botonOjoIzquierdo.setLayoutParams(LP);
        ViewGroup.LayoutParams LP2 = textoOjoDerecho.getLayoutParams();
        textoOjoDerecho.setLayoutParams(textoOjoIzquierdo.getLayoutParams());
        textoOjoIzquierdo.setLayoutParams(LP2);
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
        mensaje.setTextColor(textColor);
        textoOjoDerecho.setTextColor(textColor);
        textoOjoIzquierdo.setTextColor(textColor);
        botonCambiarOrden.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
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
        root = findViewById(R.id.actividadSeleccionarOjo);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        mensaje = findViewById(R.id.elegirOjo);
        textoOjoDerecho = findViewById(R.id.ojoDerechoTexto);
        textoOjoIzquierdo = findViewById(R.id.ojoIzquierdoTexto);
        botonOjoDerecho = findViewById(R.id.botonOjoDerecho);
        botonOjoIzquierdo = findViewById(R.id.botonOjoIzquierdo);
        botonCambiarOrden = findViewById(R.id.botonCambiarOrden);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

    /**
     * Metodo llamado en el inicio de sesion, utilizado para comprobar que prefiere el medico.
     */
    private void prioridadOjo(){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT medicos.prioridad_ojo,medicos.dni_usuario " +
                "FROM usuarios LEFT JOIN medicos "+
                "ON usuarios.DNI = medicos.dni_usuario "+
                "WHERE usuarios.correo = ? ";
        Cursor cursor = bbdd.rawQuery(query,new String[]{email});
        int ordenOjo = 0;
        if (cursor.moveToFirst()) {
            ordenOjo = cursor.getInt(0);
            DNIMedico = cursor.getInt(1);
        }
        cursor.close();
        bbdd.close();
        izquierdoPrimero = (ordenOjo == 1);
        if(izquierdoPrimero){
            cambiarOrden();
        }

    }

}