package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void pasoInicio(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarPaciente.class);
        EditText email = findViewById(R.id.editTextTextEmailAddress);

        EditText password =findViewById(R.id.editTextTextPassword);

        if(comprobarUsuario(email,password)){
            startActivity(intent);
        }
        else {


            TextView usuarioIncorrecto = findViewById(R.id.error);
            if(usuarioIncorrecto.getVisibility() == View.INVISIBLE){
                usuarioIncorrecto.setVisibility(View.VISIBLE);

            }

        }
    }
    public void iniciarSesionInvitado(View v){

        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        intent.putExtra("DNI","invitado");
        startActivity(intent);
    }
    public boolean comprobarUsuario(EditText email,EditText password){
        if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
            return false;
        }
        return true;
    }
}