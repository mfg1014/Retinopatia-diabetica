package com.example.retinopatia;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;



import android.Manifest;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;



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
    private Button botonOmitir;
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
    private Interpreter interpreter;

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
        interpreter = new Interpreter(OpenFile.loadModelFile(getApplicationContext(), "modelo2-0.003int2_0.9166666720476415_20230603-003149.tflite"));


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

        interpreter.close();
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

            for (int intentos = 0; intentos < 3 ;intentos++){
                int permisoGarantizado = ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
                if (permisoGarantizado != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            Foto.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                    break;
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    mGetContent.launch("image/*");
                }
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
        botonOmitir = findViewById(R.id.botonOmitir);
        botonOmitir.setVisibility(View.INVISIBLE);
        botonMandarImagen.setEnabled(false); //TODO cambiar cuando se implemente el RNE de calidad de la foto
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
                    Drawable drawable = imagenSeleccionada.getDrawable();
                    BitmapDrawable bd = (BitmapDrawable) drawable;
                    Bitmap foto = bd.getBitmap();
                    ImageProcessor imageProcessor = new ImageProcessor(foto, interpreter);
                    imageProcessor.execute();
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
                            ImageProcessor imageProcessor = new ImageProcessor(photo, interpreter);
                            imageProcessor.execute();


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

    private class ImageProcessor extends AsyncTask<Void, Void, Integer> {
        private Bitmap bitmap;
        private Interpreter interpreter;
        public ImageProcessor(Bitmap bitmap, Interpreter interpreter) {
            this.bitmap = bitmap;
            this.interpreter = interpreter;

        }

        /**
         * Metodo que introduce los valores del informe ya procesado despues de la red.
         * @param voids The parameters of the task.
         *
         * @return
         */
        @Override
        protected Integer doInBackground(Void... voids) {
            // Preprocesar imagen
            float[][][][] input = preprocesado(bitmap);

            // Ejecutar imagen preprocesada a través de la red neuronal VGG16
            float[][] output = new float[1][1];
            interpreter.run(input, output);

            // Interpretar resultados
            int predictedCategory = Math.round(output[0][0]) ;


            System.out.println(predictedCategory);

            return predictedCategory;
        }
        @Override
        protected void onPostExecute(Integer predictedCategory) {
            // Actualizar la visibilidad de la vista en el hilo principal
            if (predictedCategory == 1) {
                botonOmitir.setVisibility(View.VISIBLE);
                botonMandarImagen.setEnabled(false);

            } else if (predictedCategory == 0) {
                botonOmitir.setVisibility(View.INVISIBLE);
                botonMandarImagen.setEnabled(true);
            }
        }

        /**
         * Metodo utilizado para redimensionar el bitmap, devolviendo el valor que se introducira en la
         * red.
         * @param bitmap
         * @return input de la red.
         */
        @NonNull
        private float[][][][] preprocesado(Bitmap bitmap) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
            float[][][][] input = new float[1][224][224][3];
            for (int i = 0; i < 224; ++i) {
                for (int j = 0; j < 224; ++j) {
                    int pixelValue = resizedBitmap.getPixel(i, j);

                    input[0][i][j][2] =  (Color.red(pixelValue) - 123.68f);
                    input[0][i][j][1] =  (Color.green(pixelValue) - 116.779f);
                    input[0][i][j][0] =  (Color.blue(pixelValue)- 103.939f);
                }
            }
            return input;
        }


    }
}
