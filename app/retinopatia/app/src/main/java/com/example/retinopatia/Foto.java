package com.example.retinopatia;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;


import DataBase.BaseDeDatosHelper;

/**
 * Clase foto, la cual corresponde con la actividad activity_foto
 */
public class Foto extends AppCompatActivity {

    private int oscuro;
    private int textoOscuro;
    private int botonOscuro;
    private int claro;
    private int textoClaro;
    private int botonClaro;
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
    private int DNI;
    private String ojo;
    private String email;

    /**
     * Metodo onCreate, llamado al iniciar la actividad, en este metodo, se inicializa la vista,
     * de forma que el usuario pueda interactuar bien con la interfaz.
     * Además, se configura para que en caso de ser invitado, se almacene la informacion en el
     * usuario "invitado"
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);

        inicializarVista();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        ojo = intent.getStringExtra("ojo");
        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }

        inicializarGaleria();
        inicializarCamara();

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
     * Metodo que permite al usuario ir a la actividad activity_seleccionar_rne donde se selecciona
     * la red neuronal a utilizar para dar los resultados.
     * Obteniendo el Bitmap que se muestra en pantalla, y se crea el informe asignandoselo al paciente
     * en concreto.
     * @param v
     */
    public void botonMandarImagen(View v){

        Intent intent = new Intent(v.getContext(), SeleccionarRNE.class);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        Drawable drawable = imagenSeleccionada.getDrawable();
        BitmapDrawable bd = (BitmapDrawable) drawable;
        Bitmap foto = bd.getBitmap();
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = formatter.format(currentDate);
        intent.putExtra("DNI",DNI);
        intent.putExtra("fecha",fecha);
        intent.putExtra("foto", bitmapToBLOB(foto,2048,2048,50));

        intent.putExtra("ojo_imagen",ojo);
        startActivity(intent);

    }

    /**
     * Metodo que pide los permisos de camara al usuario, y en caso afirmativo, se abre, permitiendo
     * hacer la foto, mostrandola en pantalla una vez realizada.
     * @param v
     */
    public void botonHacerFoto(View v){
        int permisoGarantizado = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (permisoGarantizado != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    Foto.this,
                    new String[]{Manifest.permission.CAMERA},
                    0);
        }else{
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(takePictureIntent);
        }
    }

    /**
     * Metodo que permite al usuario obtener una foto de la galeria.
     * @param v
     */
    public void botonEscogerFoto(View v) {
        if(Build.VERSION.SDK_INT>=30){
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            mGetContent.launch("image/*");
        }else {
            int permisoGarantizado = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            if (permisoGarantizado != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        Foto.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            } else {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                mGetContent.launch("image/*");
            }
        }

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
        botonHacerFoto.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        botonEscogerFoto.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
        botonMandarImagen.setBackgroundTintList(ColorStateList.valueOf(buttonColor));
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
        botonMandarImagen = findViewById(R.id.botonMandarImg);
        botonMandarImagen.setEnabled(true); //TODO cambiar cuando se implemente el RNE de calidad de la foto
        imagenSeleccionada = findViewById(R.id.imagenSeleccionada);
        root = findViewById(R.id.actividadFoto);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonHacerFoto = findViewById(R.id.botonHacerFoto);
        botonEscogerFoto = findViewById(R.id.botonGaleria);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

    /**
     * Metodo que permite inicializar la galeria cuando se pulsa el boton correspondiente.
     *
     */
    private void inicializarGaleria(){
        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {

                if (null != result) {

                    imagenSeleccionada.setImageURI(null);
                    imagenSeleccionada.setImageURI(result);
                }
            }
        });
    }

    /**
     * Metodo que permite inicializar la camara cuando se pulsa el boton correspondiente.
     */
    private void inicializarCamara(){
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

    /**
     * Metodo que transforma el Bitmap proporcionado en un Bitmap de los tamaños introducidos,
     * con la calidad proporcionada, devolviendo un Array de bytes.
     * @param bitmap
     * @param width
     * @param height
     * @param quality
     * @return array de bytes
     */
    private byte[] bitmapToBLOB(Bitmap bitmap, int width, int height, int quality){
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        return outputStream.toByteArray();
    }
}