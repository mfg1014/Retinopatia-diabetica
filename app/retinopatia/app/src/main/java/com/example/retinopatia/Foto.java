package com.example.retinopatia;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Foto extends AppCompatActivity {

    private ActivityResultLauncher<String> mGetContent;
    private ActivityResultLauncher<Intent> cameraLauncher;
    Button botonMandarImagen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        botonMandarImagen = findViewById(R.id.botonMandarImg);
        botonMandarImagen.setEnabled(true); //TODO cambiar cuando se implemente el RNE de calidad de la foto


        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                if (null != result) {
                    ImageView imageView = findViewById(R.id.imagenSeleccionada);
                    imageView.setImageURI(result);}
            }
        });
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();

                        if (data != null && data.getExtras() != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");

                            ImageView imageView = findViewById(R.id.imagenSeleccionada);
                            imageView.setImageBitmap(photo);

                        }
                    }
                });

    }
    public void botonVolver(View v){

        finish();

    }
    public void botonPerfil(View v){

        Intent intent = new Intent(v.getContext(), Perfil.class);
        startActivity(intent);

    }
    public void botonMandarImagen(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarRNE.class);
        startActivity(intent);

    }
    public void botonHacerFoto(View v){

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePictureIntent);

    }
    public void botonEscogerFoto(View v) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        mGetContent.launch("image/*");

    }

}