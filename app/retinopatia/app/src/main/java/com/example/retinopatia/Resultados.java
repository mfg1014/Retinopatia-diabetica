package com.example.retinopatia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
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

import DataBase.BaseDeDatosHelper;

/**
 * Clase Resultados, clase donde el usuario puede ver los resultados del paciente, se corresponde
 * a la actividad activity_resultados.
 */
public class Resultados extends AppCompatActivity {
    private BaseDeDatosHelper baseDeDatosHelper;
    private SQLiteDatabase bbdd;
    private final static int NUMERO_INFORMES_PAG = 3;
    private int DNI;
    private int oscuro;
    private int textoOscuro;
    private int claro;
    private int textoClaro;
    private View root;
    private Switch modoOscuro;
    private ImageButton volver;
    private ImageButton perfil;
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
    private void iniciarInformes(int DNI,int npag){
        bbdd = baseDeDatosHelper.getReadableDatabase();
        String query = "SELECT informes.id_informe, informes.ojo_imagen, informes.resultado,informes.fecha " +
                "FROM informes "+
                "WHERE informes.dni_paciente = ? " +
                "ORDER BY informes.id_informe ASC";
        Cursor cursor = bbdd.rawQuery(query,new String[]{String.valueOf(DNI)});
        int contador = 0;

        pestañas = (int)(cursor.getCount()/NUMERO_INFORMES_PAG);
        paginas.setText(Integer.toString(pestañaActual+1)+"/"+Integer.toString(pestañas+1));
        ocultarCambiodePagina(npag,pestañas);
        int primerInforme = (npag) * NUMERO_INFORMES_PAG;
        int ultimoInforme = primerInforme + NUMERO_INFORMES_PAG;
        if(cursor.moveToLast()){
            while(cursor.getCount() > contador && contador < ultimoInforme ){
                if(contador >= primerInforme){
                    mostrarInforme(cursor, contador, primerInforme);
                }
                contador++;
                cursor.moveToPrevious() ;
            }
        }
        for(int i = contador - primerInforme; i < ultimoInforme; i++){
            switchOcultarInforme(i);
        }
        cursor.close();
        bbdd.close();
    }

    /**
     * Metodo utilizado para mostrar los distintos informes en la pantalla.
     * @param cursor cursor de todos los informes.
     * @param contador contador del informe por el que se encuentra.
     * @param primerInforme numero relativo al primer informe a mostrar en pantalla.
     */
    private void mostrarInforme(Cursor cursor, int contador, int primerInforme) {
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
            byte[] bytesImagen = cursorBLOB.getBlob(0);
            bitmap = BitmapFactory.decodeByteArray(bytesImagen,0,bytesImagen.length);
        }
        cursorBLOB.close();
        String ojo = cursor.getString(cursor.getColumnIndexOrThrow("ojo_imagen"));
        String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
        int resultado = cursor.getInt(cursor.getColumnIndexOrThrow("resultado"));
        switchInforme(bitmap,ojo,fecha,resultado, contador - primerInforme);
    }

    /**
     * Metodo usado para que el usuario no pueda acceder a ventanas que no existen.
     * @param npag numero de la pagina actual.
     * @param pestañas numero de pestañas totales.
     */
    public void ocultarCambiodePagina(int npag, int pestañas){
        if(npag <= 0){
            retroceder.setVisibility(View.INVISIBLE);
        }else{
            retroceder.setVisibility(View.VISIBLE);
        }
        if(npag >= pestañas){
            avanzar.setVisibility(View.INVISIBLE);
        }else{
            avanzar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Metodo que permite mostrar el informe en su respectivo hueco.
     * @param bitmap
     * @param ojo
     * @param fecha
     * @param resultado
     * @param n
     */
    public void switchInforme(Bitmap bitmap, String ojo, String fecha, int resultado, int n){
        switch (n){
            case 0:
                foto1.setImageBitmap(bitmap);
                foto1.setVisibility(View.VISIBLE);
                fecha1.setText(fecha);
                informacion1.setText("ojo "+ ojo);
                if(Integer.valueOf(resultado) != null){
                    resultados1.setText(switchResultado( resultado));
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
                    resultados2.setText(switchResultado(resultado));
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
                    resultados3.setText(switchResultado(resultado));
                }
                fecha3.setVisibility(View.VISIBLE);
                informacion3.setVisibility(View.VISIBLE);
                resultados3.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * Metodo que oculta aquellos informes que no existen.
     * @param n
     */
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

    public String switchResultado(int resultado){
        switch (resultado){
            case 0:
                return "NPDR";

            case 1:
                return "NPDR leve";
            case 2:
                return "NPDR moderada";
            case 3:
                return "NPDR severa";
            case 4:
                return "PDR";
            default:
                return "Error";
        }
    }
}