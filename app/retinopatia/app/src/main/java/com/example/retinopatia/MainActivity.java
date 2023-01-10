package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void pasoInicio(View v){

        Intent intent = new Intent(v.getContext(), MenuPrincipal.class);
        EditText email = (EditText) findViewById(R.id.editTextTextEmailAddress);

        EditText password = (EditText) findViewById(R.id.editTextTextPassword);

        if(comprobarUsuario(email,password)){
            startActivity(intent);
        }
        else {

            Button olvPass = (Button) findViewById(R.id.BotonPassOlv);
            TextView usuarioIncorrecto = (TextView) findViewById(R.id.error);
            if(olvPass.getVisibility() == View.INVISIBLE){
                olvPass.setVisibility(View.VISIBLE);
            }
            if(usuarioIncorrecto.getVisibility() == View.INVISIBLE){
                usuarioIncorrecto.setVisibility(View.VISIBLE);

            }

        }
    }
    public boolean comprobarUsuario(EditText email,EditText password){
        if(TextUtils.isEmpty(email.getText().toString()) || TextUtils.isEmpty(password.getText().toString())){
            return false;
        }
        return true;
    }
}