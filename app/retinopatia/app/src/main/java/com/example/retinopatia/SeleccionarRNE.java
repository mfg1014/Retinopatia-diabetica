package com.example.retinopatia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

import DataBase.BaseDeDatosHelper;

/**
 * Clase SeleccionarRNE, clase donde el usuario puede seleccionar la red neuronal a utilizar
 * se corresponde a la actividad activity_seleccionar_rne.
 */
public class SeleccionarRNE extends AppCompatActivity {
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
    private Button botonResultados;
    private CheckBox RNE1;
    private CheckBox RNE2;
    private CheckBox todos;
    private TextView mensaje;
    private int DNI;
    private String email;
    private String fecha;
    private Bitmap foto;
    private String ojo;

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
        setContentView(R.layout.activity_seleccionar_rne);

        inicializarVista();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        DNI = intent.getIntExtra("DNI",-1);
        fecha = intent.getStringExtra("fecha");
        byte[] bytesImagen = intent.getByteArrayExtra("foto");
        foto = BitmapFactory.decodeByteArray(bytesImagen,0,bytesImagen.length);
        ojo = intent.getStringExtra("ojo_imagen");

        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }

        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());

        //cambiar el metodo.

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
     * Metodo usado para volver a la actividad activity_seleccionar_ojo, y crea un informe en la BBDD
     * con los datos proporcionados.
     * @param v
     */
    public void botonCrearInforme(View v){
        cargarRedes();
        Intent intent = new Intent(v.getContext(), SeleccionarOjo.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentModoOscuro(intent);
        intent.putExtra("email",email);
        intent.putExtra("DNI",DNI);
        startActivity(intent);

    }

    /**
     * Metodo que comprueba si se ha seleccionado alguna RNE para el boton 1
     * @param v
     */
    public void opcionRNE1(View v){
        if(RNE1.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE2.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }
    /**
     * Metodo que comprueba si se ha seleccionado alguna RNE para el boton 2
     * @param v
     */
    public void opcionRNE2(View v){
        if(RNE2.isChecked()){
            botonResultados.setEnabled(true);

        }else{
            if(!RNE1.isChecked()){
                botonResultados.setEnabled(false);
            }
        }
    }

    /**
     * Metodo para la opcion de todas la RNE, activando todas, y desactivandolas para que no se pueda
     * cambiar el valor.
     * En caso de que se deseleccione, se habilitan los botones.
     * @param v
     */
    public void opcionTodos(View v){
        if(todos.isChecked()){
            RNE1.setChecked(true);
            RNE2.setChecked(true);
            RNE1.setEnabled(false);
            RNE2.setEnabled(false);
            botonResultados.setEnabled(true);

        }else{
            RNE1.setEnabled(true);
            RNE2.setEnabled(true);
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
        RNE1.setTextColor(textColor);
        RNE2.setTextColor(textColor);
        todos.setTextColor(textColor);
        mensaje.setTextColor(textColor);
        botonResultados.setBackgroundTintList(ColorStateList.valueOf(buttonColor));

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
        root = findViewById(R.id.actividadSeleccionarRNE);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        botonResultados = findViewById(R.id.botonObtenerResultados);
        botonResultados.setEnabled(false);
        RNE1 = findViewById(R.id.checkBoxRNE1);
        RNE2 = findViewById(R.id.checkBoxRNE2);
        todos = findViewById(R.id.checkBoxTodas);
        mensaje = findViewById(R.id.textViewMensajeRNE);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        botonOscuro = getResources().getColor(R.color.background_green);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
        botonClaro = getResources().getColor(R.color.background_blue);
    }

    /**
     * Metodo que carga las redes neuronales
     * TODO hay que introducir las redes neuronales de forma que al pasar a la siguiente ventana,
     * se haya empezado el proceso. No refactorizo porque se cambiara en un futuro.
     *
     */
    private void cargarRedes(){
        try {
            Interpreter interpreter = new Interpreter(loadModelFile(getApplicationContext()));
            bbdd = baseDeDatosHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("dni_paciente", DNI);
            bbdd.insert("informes",null,values);

            String query = "SELECT id_informe " +
                    "FROM informes "+
                    "WHERE dni_paciente = ? " +
                    "ORDER BY id_informe DESC";
            Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNI)});
            cursor.moveToFirst();
            int idInforme = cursor.getInt(0);
            cursor.close();
            bbdd.close();
            ImageProcessor imageProcessor = new ImageProcessor(foto, interpreter,idInforme,ojo);
            imageProcessor.execute();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metodo que devulve el modelo de forma que el interprete lo pueda cargar.
     * @param context
     * @return
     * @throws IOException
     */
    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        AssetFileDescriptor fileDescriptor = context.getAssets().openFd("modeloConvertido.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        fileChannel.close();
        inputStream.close();
        fileDescriptor.close();
        return mappedByteBuffer;
    }

    /**
     * Clase para hacer que el proceso de la red neuronal sea concurrente.
     */
    private class ImageProcessor extends AsyncTask<Void, Void, Void> {
        private Bitmap bitmap;
        private Interpreter interpreter;
        private int idInforme;
        private String ojo;
        public ImageProcessor(Bitmap bitmap, Interpreter interpreter, int idInforme,String ojo) {
            this.bitmap = bitmap;
            this.interpreter = interpreter;
            this.idInforme = idInforme;
            this.ojo = ojo;
        }

        /**
         * Metodo que introduce los valores del informe ya procesado despues de la red.
         * @param voids The parameters of the task.
         *
         * @return
         */
        @Override
        protected Void doInBackground(Void... voids) {
            // Preprocesar imagen
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, false);
            float[][][][] input = redimensionarBitmap(resizedBitmap);

            // Ejecutar imagen preprocesada a través de la red neuronal VGG16
            float[][] output = new float[1][5];
            interpreter.run(input, output);

            // Interpretar resultados
            int predictedCategory = -1;
            float maxProbability = 0;
            for (int i = 0; i < output[0].length; i++) {
                System.out.println(output[0][i]);
                if (output[0][i] > maxProbability) {
                    predictedCategory = i;
                    maxProbability = output[0][i];
                }
            }
            interpreter.close();

            cargarDatosBaseDeDatos(predictedCategory);
            return null;
        }

        /**
         * Metodo que carga en la base de datos, los valores del informe.
         * @param predictedCategory
         */
        private void cargarDatosBaseDeDatos(int predictedCategory) {
            System.out.println(predictedCategory);
            bbdd = baseDeDatosHelper.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put("imagen_del_informe",bitmapToBLOB(foto,2048,2048,50));
            valores.put("ojo_imagen",ojo);
            Date currentDate = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String fecha = formatter.format(currentDate);
            valores.put("fecha", fecha);
            valores.put("resultado", predictedCategory);

            bbdd.update("informes", valores, "id_informe = ?", new String[] {String.valueOf(idInforme)});
            bbdd.close();
        }

        /**
         * Metodo utilizado para redimensionar el bitmap, devolviendo el valor que se introducira en la
         * red.
         * @param resizedBitmap
         * @return input de la red.
         */
        @NonNull
        private float[][][][] redimensionarBitmap(Bitmap resizedBitmap) {
            float[][][][] input = new float[1][224][224][3];
            for (int i = 0; i < 224; ++i) {
                for (int j = 0; j < 224; ++j) {
                    int pixelValue = resizedBitmap.getPixel(i, j);
                    input[0][i][j][2] = (Color.red(pixelValue) - 123.68f) / 58.393f;
                    input[0][i][j][1] = (Color.green(pixelValue) - 116.779f) / 57.12f;
                    input[0][i][j][0] = (Color.blue(pixelValue) - 103.939f) / 57.375f;
                }
            }
            return input;
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
}