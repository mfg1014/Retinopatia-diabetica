package com.example.retinopatia;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import DataBase.BaseDeDatos;


public class Foto extends AppCompatActivity {

    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private Button botonMandarImagen;
    private ImageView imagenSeleccionada;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
    private Button botonHacerFoto;
    private Button botonEscogerFoto;
    private BaseDeDatos baseDeDatos;
    private int DNI;
    private String ojo;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        botonMandarImagen = findViewById(R.id.botonMandarImg);
        botonMandarImagen.setEnabled(true); //TODO cambiar cuando se implemente el RNE de calidad de la foto
        imagenSeleccionada = findViewById(R.id.imagenSeleccionada);
        root = findViewById(R.id.actividadFoto);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonHacerFoto = findViewById(R.id.botonHacerFoto);
        botonEscogerFoto = findViewById(R.id.botonGaleria);
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        ojo = intent.getStringExtra("ojo");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());



        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                if (null != result) {

                    imagenSeleccionada.setImageURI(null);
                    imagenSeleccionada.setImageURI(result);
                }
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.getExtras() != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            imagenSeleccionada.setImageBitmap(null);
                            imagenSeleccionada.setImageBitmap(photo);

                        }
                    }
                });

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
    public void botonMandarImagen(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarRNE.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        Drawable drawable = imagenSeleccionada.getDrawable();
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap foto = bd.getBitmap();
        if(foto == null){

        }else{
            baseDeDatos.añadirInforme(foto,DNI,ojo,0);
            intent.putExtra("DNI",DNI);
            startActivity(intent);
        }

    }
    public void botonHacerFoto(View v){
        int intentos = 3;

        int permisoGarantizado = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        while (intentos>0||permisoGarantizado != PackageManager.PERMISSION_GRANTED){
            permisoGarantizado = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
            if (permisoGarantizado != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        Foto.this,
                        new String[]{Manifest.permission.CAMERA},
                        0);
            }
            intentos-=1;
        }
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);

    }


    public void botonEscogerFoto(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mGetContent.launch("image/*");

    }
    public void botonModoOscuro(View v){
        int oscuro = getResources().getColor(R.color.background_darkmode_gray);
        int textoOscuro = getResources().getColor(R.color.background_gray);
        int botonOscuro = getResources().getColor(R.color.background_green);
        int claro = getResources().getColor(R.color.background_gray);
        int textoClaro = getResources().getColor(R.color.black);
        int botonClaro = getResources().getColor(R.color.background_blue);
        if(modoOscuro.isChecked()){
            root.setBackgroundColor(oscuro);
            volver.setBackgroundTintList(ColorStateList.valueOf(oscuro));
            volver.setColorFilter(textoOscuro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(textoOscuro));
            perfil.setColorFilter(oscuro);
            botonHacerFoto.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
            botonEscogerFoto.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));
            botonMandarImagen.setBackgroundTintList(ColorStateList.valueOf(botonOscuro));

        }else{
            root.setBackgroundColor(claro);
            volver.setBackgroundTintList(ColorStateList.valueOf(claro));
            volver.setColorFilter(textoClaro);
            perfil.setBackgroundTintList(ColorStateList.valueOf(textoClaro));
            perfil.setColorFilter(claro);
            botonHacerFoto.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
            botonEscogerFoto.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
            botonMandarImagen.setBackgroundTintList(ColorStateList.valueOf(botonClaro));
        }

    }
    public void intentModoOscuro(Intent intent){
        if(modoOscuro.isChecked()){
            intent.putExtra("modoOscuro",true);
        }else{
            intent.putExtra("modoOscuro",false);
        }
    }

}