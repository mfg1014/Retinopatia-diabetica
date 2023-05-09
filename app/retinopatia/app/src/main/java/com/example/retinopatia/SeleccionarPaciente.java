package com.example.retinopatia;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;


import DataBase.BaseDeDatosHelper;

/**
 * Clase SeleccionarPaciente, clase donde el usuario puede seleccionar el paciente introduciendo su DNI
 * se corresponde a la actividad activity_seleccionar_paciente.
 */
public class SeleccionarPaciente extends AppCompatActivity {

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
    private ImageButton volver;
    private ImageButton perfil;
    private EditText DNI;
    private ImageButton botonBuscar;
    private Button botonPasarSiguiente;
    private TextView mensaje;
    private int numeroDNI;
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
        setContentView(R.layout.activity_seleccionar_paciente);
        inicializarVista();
        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        botonPasarSiguiente.setEnabled(false);
    }

    /**
     * Metodo que permite al usuario ir a la actividad activity_menu_principal.
     * Con el DNI del paciente introducido
     * @param v
     */
    public void botonNext (View v){
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI",numeroDNI);
        startActivity(intent);

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
        DNI.setTextColor(textColor);
        DNI.setHintTextColor(textColor);
        mensaje.setTextColor(textColor);
        botonBuscar.setColorFilter(textColor);
        botonBuscar.setBackgroundTintList(ColorStateList.valueOf(color));
        botonPasarSiguiente.setBackgroundTintList(ColorStateList.valueOf(buttonColor));

    }

    /**
     * Metodo usado para buscar el paciente en la base de datos. Para ello se comprueba si el usuario
     * ha introducido un valor y si el DNI esta almacenado en la BBDD, mostrando un error, si no se encuentra
     * En caso de que se encuentre se habilita el boton para pasar a la siguiente actividad.
     * @param v
     */
    public void botonBuscarPaciente(View v){
        try{
            int DNIProporcionado;
            String DNIPaciente = DNI.getText().toString();
            DNIProporcionado = Integer.parseInt(DNIPaciente);
            String nombrePaciente = getNombrePaciente(DNIProporcionado);

            if(nombrePaciente == null){
                mensaje.setTextColor(getResources().getColor(R.color.error_red));
                mensaje.setText("El paciente no se ha encontrado, ejemplo  de DNI: 12345678");
            }else{
                if(modoOscuro.isChecked()){
                    mensaje.setTextColor(textoOscuro);
                }else{
                    mensaje.setTextColor(textoClaro);
                }
                numeroDNI = DNIProporcionado;
                mensaje.setText("Paciente: " + nombrePaciente);
                botonPasarSiguiente.setEnabled(true);
            }

        }catch (NumberFormatException ex){
            mensaje.setText("Inserte solo los numeros del DNI, ejemplo  de DNI: 12345678");
        }
    }

    /**
     * Metodo que obtiene el nombre y apellidos del paciente a traves de el DNI.
     * @param DNIProporcionado
     * @return String con nombre y apellidos.
     */
    @Nullable
    private String getNombrePaciente(int DNIProporcionado) {
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT * " +
                "FROM usuarios LEFT JOIN pacientes "+
                "ON usuarios.DNI = pacientes.dni_usuario " +
                "WHERE usuarios.DNI = ?";

        Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNIProporcionado)});
        String nombrePaciente = null;
        if (cursor.moveToFirst()) {
            nombrePaciente = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            nombrePaciente +=" " + cursor.getString(cursor.getColumnIndexOrThrow("apellido"));
        }
        cursor.close();
        bbdd.close();
        return nombrePaciente;
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
        root = findViewById(R.id.actividadSeleccionarPaciente);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonBuscar = findViewById(R.id.botonBuscar);
        botonPasarSiguiente = findViewById(R.id.botonEntrarMenu);
        DNI = findViewById(R.id.editTextDNI);
        mensaje = findViewById(R.id.textViewMensajeDNI);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }
}