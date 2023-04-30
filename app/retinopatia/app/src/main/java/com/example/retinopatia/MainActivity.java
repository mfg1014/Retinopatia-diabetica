package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;


import java.lang.reflect.Field;
import java.nio.ByteBuffer;

import DataBase.BaseDeDatos;
import DataBase.BaseDeDatosHelper;


/**
 * Clase MainActivity, clase de inicio de sesion, se corresponde a la actividad
 * activity_main. Es la clase que se ejecuta al iniciar la aplicacion.
 */
public class MainActivity extends AppCompatActivity {

    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;
    private EditText email;
    private EditText password;
    private TextView usuarioIncorrecto;
    private Button guestMode;
    private Button iniciarSesion;
    private Switch modoOscuro;
    private View root;
    //private BaseDeDatos baseDeDatos;
    /**
     * Metodo onCreate, llamado al iniciar la actividad, en este metodo, se inicializa la vista,
     * de forma que el usuario pueda interactuar bien con la interfaz.
     * Se inicializa la base de datos.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_main);
        inicializarVista();
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        //baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
    }

    /**
     * Metodo que se utiliza para iniciar sesion, en caso de que el usuario y la contraseña se han
     * correctos, se llama a la siguiente actividad. En caso de que no se encuentre, se muestra el
     * mensaje de error.
     * @param v
     */
    public void pasoInicio(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarPaciente.class);
        System.out.println("llega");
        if(comprobarUsuario()){
            System.out.println("llega");
            usuarioIncorrecto.setVisibility(View.INVISIBLE);
            intentModoOscuro(intent);
            intent.putExtra("email",email.getText().toString());
            startActivity(intent);
        }
        else {
            System.out.println("no llega");
            if(usuarioIncorrecto.getVisibility() == View.INVISIBLE){
                usuarioIncorrecto.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * Metodo utilizado para inciar sesion como invitado, el cual inicia un usuario en la
     * base de datos llamado invitado, y cada vez que se inicia sesion como invitado
     * se sobreescribe el usuario anterior.
     * @param v
     */
    public void iniciarSesionInvitado(View v){
        bbdd = baseDeDatosHelper.getWritableDatabase();
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intent.putExtra("DNI",-1);
        intent.putExtra("email","invitado");

        bbdd = baseDeDatosHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", "invitado");
        values.put("apellido", "invitado");
        values.put("correo", "invitado");
        values.put("DNI", -1);
        values.put("tipo_usuario", -1);
        values.put("fecha_nacimiento", "invitado");
        bbdd.insert("usuarios",null,values);

        values.clear();
        values.clear();
        values.put("id_paciente",-1);
        values.put("dni_usuario",-1);
        values.put("informacion","");
        values.put("estado","");
        bbdd.insert("pacientes",null,values);
        bbdd.close();
        intentModoOscuro(intent);
        startActivity(intent);
    }

    /**
     * Metodo utilizado para comprobar el usuario, comprobando en la base de datos.
     * Primero se comprueba si alguno de los parametros es vacio
     * Segundo se comprueba si la contraseña de la base de datos es igual a la introducida por el usuario
     * Dentro de la base de datos se comprueba si existe el usuario introducido
     * @return
     */
    public boolean comprobarUsuario(){
        String emailUsuario = email.getText().toString();
        String contraseña = password.getText().toString();
        System.out.println("llega");
        if(TextUtils.isEmpty(emailUsuario)|| TextUtils.isEmpty(contraseña)) {
            return false;
        }
        //String password = baseDeDatos.getContraseña(emailUsuario);
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT * " +
                        "FROM usuarios LEFT JOIN medicos "+
                        "ON usuarios.DNI = medicos.dni_usuario " +
                        "WHERE usuarios.correo = ?";

        Cursor cursor = bbdd.rawQuery(query,new String[]{emailUsuario});


        System.out.println(cursor.getCount());
        String password = null;
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndexOrThrow("contrasena"));

            System.out.println(password);

        }
        cursor.close();
        bbdd.close();


        if(password == null){
            return false;
        }
        if(password.equals(contraseña) ){
            return true;
        }
        return false;
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
        email.setTextColor(textColor);
        password.setTextColor(textColor);
        modoOscuro.setTextColor(textColor);
        email.setHintTextColor(textColor);
        password.setHintTextColor(textColor);
        guestMode.setTextColor(textColor);
        iniciarSesion.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
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
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);
        usuarioIncorrecto = findViewById(R.id.error);
        modoOscuro = findViewById(R.id.switchModoOscuro);
        root = findViewById(R.id.actividadInicioSesion);
        guestMode = findViewById(R.id.botonInvitado);
        iniciarSesion = findViewById(R.id.botonIniciarSesion);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

}