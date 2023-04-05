package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import DataBase.BaseDeDatos;

public class SeleccionarPaciente extends AppCompatActivity {

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
    private BaseDeDatos baseDeDatos;
    private int numeroDNI;
    private String email;

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
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        botonPasarSiguiente.setEnabled(false);
    }
    public void botonNext (View v){
        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI",numeroDNI);
        startActivity(intent);

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
    public void botonBuscarPaciente(View v){
        try{
            int DNIProporcionado;
            String DNIPaciente = DNI.getText().toString();
            DNIProporcionado = Integer.parseInt(DNIPaciente);
            String nombrePaciente = baseDeDatos.getPaciente(DNIProporcionado);
            if(nombrePaciente.equals("")){
                mensaje.setTextColor(getResources().getColor(R.color.error_red));
                mensaje.setText("El paciente no se ha encontrado, ejemplo  de DNI: 12345678");

            }else{
                if(modoOscuro.isChecked()){
                    mensaje.setTextColor(getResources().getColor(R.color.background_gray));
                }else{
                    mensaje.setTextColor(getResources().getColor(R.color.black));
                }
                numeroDNI = DNIProporcionado;
                mensaje.setText("Paciente: "+nombrePaciente);
                botonPasarSiguiente.setEnabled(true);
            }

        }catch (NumberFormatException ex){
            mensaje.setText("Inserte solo los numeros del DNI, ejemplo  de DNI: 12345678");
        }
    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }
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