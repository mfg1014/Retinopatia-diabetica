package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import DataBase.BaseDeDatos;
import DataBase.BaseDeDatosHelper;
import DataBase.Informe;

/**
 * Clase Resultados, clase donde el usuario puede ver los resultados del paciente, se corresponde
 * a la actividad activity_resultados.
 */
public class Resultados extends AppCompatActivity {
    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private final static int NUMERO_INFORMES_PAG = 3;
    private final static int MAX_IMAGE = 100*1024*1024;
    private int DNI;
    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
    //private BaseDeDatos baseDeDatos;
    //private List<Informe> informes;
    private Button retroceder;
    private Button avanzar;
    private TextView paginas;
    private ImageView foto1;
    private ImageView foto2;
    private ImageView foto3;
    private TextView fecha1;
    private TextView fecha2;
    private TextView fecha3;
    private TextView informacion1;
    private TextView informacion2;
    private TextView informacion3;
    private TextView resultados1;
    private TextView resultados2;
    private TextView resultados3;
    private int pestañas;
    private int pestañaActual;
    private String email;

    /**
     * Metodo onCreate, llamado al iniciar la actividad, en este metodo, se inicializa la vista,
     * de forma que el usuario pueda interactuar bien con la interfaz.
     * Tambien se cargan los informes de fecha mas cercana a fecha mas lejana.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        inicializarVista();

        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        if(intent.getBooleanExtra("modoOscuro",false)){
            modoOscuro.setChecked(true);
            botonModoOscuro(modoOscuro);
        }
        DNI = intent.getIntExtra("DNI",-1);
        if(DNI == -1){
            perfil.setVisibility(View.INVISIBLE);
        }
        baseDeDatosHelper = BaseDeDatosHelper.getBaseDeDatos(getApplicationContext());
        //baseDeDatos = BaseDeDatos.getBaseDeDatos(getApplicationContext());
        //informes = baseDeDatos.getInformes(DNI);
        iniciarInformes(DNI,0);

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

        if(modoOscuro.isChecked()){
            color = oscuro;
            textColor = textoOscuro;
        }else{
            color = claro;
            textColor = textoClaro;
        }
        root.setBackgroundColor(color);
        volver.setBackgroundTintList(ColorStateList.valueOf(color));
        volver.setColorFilter(textColor);
        perfil.setBackgroundTintList(ColorStateList.valueOf(color));
        perfil.setColorFilter(textColor);
        paginas.setTextColor(textColor);
        retroceder.setBackgroundTintList(ColorStateList.valueOf(color));
        avanzar.setBackgroundTintList(ColorStateList.valueOf(color));
        foto1.setBackgroundTintList(ColorStateList.valueOf(color));
        foto2.setBackgroundTintList(ColorStateList.valueOf(color));
        foto3.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    /**
     * Metodo utilizado cuando hay mas de 3 informes, y ocupa más de una pantalla,
     * sirve para ver los siguientes informes
     * @param v
     */
    public void botonAvanzar(View v){
        pestañaActual += 1;
        iniciarInformes(DNI, pestañaActual);
    }

    /**
     * Metodo utilizado cuando hay mas de 3 informes, y se quiere ir a la patalla anterior.
     * @param v
     */
    public void botonRetroceder(View v){
        pestañaActual -= 1;
        iniciarInformes(DNI, pestañaActual);
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
     * Metodo usado para cargar los informes en pantalla, hay 3 informes por pantalla, y
     * cada informe contiene la foto, la fecha, la informacion del ojo que se corresponde y los resultados
     * Como cada informe tiene un objeto de la interfaz diferente, se hace un switch tanto para visibilizar
     * los datos, como para ocultarlos.
     * @param pestañaActual
     */
    /*public void cargarInformes(int pestañaActual){
        int primerInforme = (pestañaActual - 1) * 3;
        for(int i = 0; i < 3;i++){
            if(informes.size() > primerInforme + i) {
                switchInforme(primerInforme, i);
            }else{
                switch(i){
                    case 0:
                        foto1.setVisibility(View.INVISIBLE);
                        fecha1.setVisibility(View.INVISIBLE);
                        informacion1.setVisibility(View.INVISIBLE);
                        resultados1.setVisibility(View.INVISIBLE);
                        break;
                    case 1:
                        foto2.setVisibility(View.INVISIBLE);
                        fecha2.setVisibility(View.INVISIBLE);
                        informacion2.setVisibility(View.INVISIBLE);
                        resultados2.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        foto3.setVisibility(View.INVISIBLE);
                        fecha3.setVisibility(View.INVISIBLE);
                        informacion3.setVisibility(View.INVISIBLE);
                        resultados3.setVisibility(View.INVISIBLE);
                        break;
                }
            }
        }
    }*/

    /**
     * Metodo que hace el switch determinando el informe correspondiente
     * @param primerInforme
     * @param n
     */
/*    public void switchInforme(int primerInforme, int n){
        switch (n){
            case 0:
                visibilizarInforme1(informes.size()-primerInforme-1);
                break;
            case 1:
                visibilizarInforme2(informes.size()-primerInforme-2);
                break;
            case 2:
                visibilizarInforme3(informes.size()-primerInforme-3);
                break;
        }
    }*/

    /**
     * Metodo que visibiliza el informe 1, cargando la imagen, la fecha, el ojo y los resultados.
     * @param informe
     */
/*    public void visibilizarInforme1(int informe){
        Bitmap imagen = informes.get(informe).getImagenDelInforme();
        foto1.setImageBitmap(imagen);
        foto1.setVisibility(View.VISIBLE);
        fecha1.setText(informes.get(informe).getFecha().toString());
        informacion1.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados1.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha1.setVisibility(View.VISIBLE);
        informacion1.setVisibility(View.VISIBLE);
        resultados1.setVisibility(View.VISIBLE);
    }*/
    /**
     * Metodo que visibiliza el informe 2, cargando la imagen, la fecha, el ojo y los resultados.
     * @param informe
     */
  /*  public void visibilizarInforme2(int informe){

        Bitmap imagen2 = informes.get(informe).getImagenDelInforme();
        foto2.setImageBitmap(imagen2);
        foto2.setVisibility(View.VISIBLE);
        fecha2.setText(informes.get(informe).getFecha().toString());
        informacion2.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados2.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha2.setVisibility(View.VISIBLE);
        informacion2.setVisibility(View.VISIBLE);
        resultados2.setVisibility(View.VISIBLE);

    }*/
    /**
     * Metodo que visibiliza el informe 3, cargando la imagen, la fecha, el ojo y los resultados.
     * @param informe
     */
   /* public void visibilizarInforme3(int informe){
        Bitmap imagen3 = informes.get(informe).getImagenDelInforme();
        foto3.setImageBitmap(imagen3);
        foto3.setVisibility(View.VISIBLE);
        fecha3.setText(informes.get(informe).getFecha().toString());
        informacion3.setText("ojo "+informes.get(informe).getOjoImagen());
        resultados3.setText(Integer.toString(informes.get(informe).getResultado()));
        fecha3.setVisibility(View.VISIBLE);
        informacion3.setVisibility(View.VISIBLE);
        resultados3.setVisibility(View.VISIBLE);
    }*/
    /**
     * Metodo donde se inicializan los elementos de la actividad y los colores entre los que puede cambiar
     *
     */
    private void inicializarVista(){
        root = findViewById(R.id.actividadResultados);
        modoOscuro = findViewById(R.id.switchModoOscuro2);
        volver = findViewById(R.id.returnButton3);
        perfil = findViewById(R.id.profileButton3);
        retroceder = findViewById(R.id.botonRetroceder);
        avanzar = findViewById(R.id.botonAvanzar);
        paginas = findViewById(R.id.textoPaginas);
        foto1 = findViewById(R.id.imagen1);
        foto2 = findViewById(R.id.imagen2);
        foto3 = findViewById(R.id.imagen3);
        fecha1 = findViewById(R.id.fecha1);
        informacion1 = findViewById(R.id.info1);
        resultados1 = findViewById(R.id.result1);
        fecha2 = findViewById(R.id.fecha2);
        informacion2 = findViewById(R.id.info2);
        resultados2 = findViewById(R.id.result2);
        fecha3 = findViewById(R.id.fecha3);
        informacion3 = findViewById(R.id.info3);
        resultados3 = findViewById(R.id.result3);
        oscuro = getResources().getColor(R.color.background_darkmode_gray);
        textoOscuro = getResources().getColor(R.color.background_gray);
        claro = getResources().getColor(R.color.background_gray);
        textoClaro = getResources().getColor(R.color.black);
    }

    /**
     * Metodo utilizado al iniciar la actividad, sirve para calcular las pestañas existentes
     * poniendo la vista con respecto este numero.
     */
    /*private void inicializarInformes(){
        if(informes == null){
            paginas.setText("1/1");
            paginas.setVisibility(View.INVISIBLE);
            retroceder.setVisibility(View.INVISIBLE);
            avanzar.setVisibility(View.INVISIBLE);
        }else{
            pestañas = (int) (informes.size()-1)/3;
            pestañas += 1;
            pestañaActual = 1;
            if(pestañas == 1){
                paginas.setText("1/1");
                paginas.setVisibility(View.INVISIBLE);
                retroceder.setVisibility(View.INVISIBLE);
                avanzar.setVisibility(View.INVISIBLE);

            }else{
                paginas.setText("1/"+Integer.toString(pestañas));
            }
            cargarInformes(pestañaActual);
        }
    }*/
    private void iniciarInformes(int DNI,int npag){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT informes.id_informe, informes.ojo_imagen, informes.resultado,informes.fecha " +
                "FROM usuarios "+
                "LEFT JOIN pacientes ON usuarios.DNI = pacientes.dni_usuario " +
                "LEFT JOIN informes ON pacientes.dni_usuario = informes.dni_paciente " +
                "WHERE usuarios.DNI = ? " +
                "ORDER BY informes.id_informe ASC";
        Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNI)});
        int contador = 0;
        int primerInforme = (npag) * NUMERO_INFORMES_PAG;
        int ultimoInforme = primerInforme + NUMERO_INFORMES_PAG;
        cursor.moveToLast();
            while(cursor.getCount() > contador && contador < ultimoInforme ){

                System.out.println(contador);
                System.out.println(primerInforme);
                if(contador >= primerInforme){
                    Bitmap bitmap = null;
                    String id = cursor.getString(cursor.getColumnIndexOrThrow("id_informe"));
                    Cursor cursorBLOB = bbdd.query(
                            "informes",
                            new String[]{"imagen_del_informe"},
                            "id_informe = ?",
                            new String[]{id},
                            null,
                            null,
                            null
                    );
                    if (cursorBLOB.moveToFirst()) {
                        System.out.println("imagen");
                        byte[] bytesImagen = cursorBLOB.getBlob(0);
                        System.out.println(bytesImagen.length);
                        bitmap = BitmapFactory.decodeByteArray(bytesImagen,0,bytesImagen.length);

                        cursorBLOB.close();
                    }
                    String ojo = cursor.getString(cursor.getColumnIndexOrThrow("ojo_imagen"));
                    String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                    int resultado = cursor.getInt(cursor.getColumnIndexOrThrow("resultado"));
                    switchInforme2(bitmap,ojo,fecha,resultado,contador-primerInforme);
                }
                contador++;
                cursor.moveToPrevious() ;
            }

        for(int i = contador - primerInforme; i < ultimoInforme;i++){
            switchOcultarInforme(i);
        }
        cursor.close();
        bbdd.close();
    }
    public void switchInforme2(Bitmap bitmap, String ojo,String fecha,int resultado, int n){
        switch (n){
            case 0:
                foto1.setImageBitmap(bitmap);
                foto1.setVisibility(View.VISIBLE);
                fecha1.setText(fecha);
                informacion1.setText("ojo "+ ojo);
                if(Integer.valueOf(resultado) != null){
                    resultados1.setText(String.valueOf( resultado));
                }

                fecha1.setVisibility(View.VISIBLE);
                informacion1.setVisibility(View.VISIBLE);
                resultados1.setVisibility(View.VISIBLE);
                break;
            case 1:
                foto2.setImageBitmap(bitmap);
                foto2.setVisibility(View.VISIBLE);
                fecha2.setText(fecha);
                informacion2.setText("ojo "+ ojo);
                if(Integer.valueOf(resultado) != null){
                    resultados2.setText(String.valueOf(resultado));
                }
                fecha2.setVisibility(View.VISIBLE);
                informacion2.setVisibility(View.VISIBLE);
                resultados2.setVisibility(View.VISIBLE);
                break;
            case 2:
                foto3.setImageBitmap(bitmap);
                foto3.setVisibility(View.VISIBLE);
                fecha3.setText(fecha);
                informacion3.setText("ojo "+ ojo);
                if(Integer.valueOf(resultado) != null){
                    resultados3.setText(String.valueOf(resultado));
                }
                fecha3.setVisibility(View.VISIBLE);
                informacion3.setVisibility(View.VISIBLE);
                resultados3.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void switchOcultarInforme(int n) {
        switch (n) {
            case 0:
                foto1.setVisibility(View.INVISIBLE);
                fecha1.setVisibility(View.INVISIBLE);
                informacion1.setVisibility(View.INVISIBLE);
                resultados1.setVisibility(View.INVISIBLE);
                break;
            case 1:
                foto2.setVisibility(View.INVISIBLE);
                fecha2.setVisibility(View.INVISIBLE);
                informacion2.setVisibility(View.INVISIBLE);
                resultados2.setVisibility(View.INVISIBLE);
                break;
            case 2:
                foto3.setVisibility(View.INVISIBLE);
                fecha3.setVisibility(View.INVISIBLE);
                informacion3.setVisibility(View.INVISIBLE);
                resultados3.setVisibility(View.INVISIBLE);
                break;
        }
    }
}