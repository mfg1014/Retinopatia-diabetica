package DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.retinopatia.R;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

public class BaseDeDatosHelper extends SQLiteOpenHelper {
    private static final int VERSION_BASE_DE_DATOS = 4;
    private static final String NOMBRE_BASE_DE_DATOS = "BaseDeDatosRetinopatia.sqlite";
    private static BaseDeDatosHelper bbddHelper;
    private Context context;

    public static BaseDeDatosHelper getBaseDeDatos(Context context){
       if(bbddHelper == null){
            bbddHelper = new BaseDeDatosHelper(context);
       }
        return bbddHelper;
    }
    private BaseDeDatosHelper(Context context)
    {
        super(context, NOMBRE_BASE_DE_DATOS, null, VERSION_BASE_DE_DATOS);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crea la tabla usuarios
        String CREATE_TABLE_USUARIOS = "CREATE TABLE usuarios (" +
                "id_usuario INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT NOT NULL," +
                "apellido TEXT NOT NULL," +
                "correo TEXT NOT NULL UNIQUE," +
                "DNI INTEGER NOT NULL UNIQUE," +
                "tipo_usuario INTEGER NOT NULL," +
                "fecha_nacimiento TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_USUARIOS);

        // Crea la tabla pacientes
        String CREATE_TABLE_PACIENTES = "CREATE TABLE pacientes (" +
                "id_paciente INTEGER PRIMARY KEY," +
                "dni_usuario INTEGER NOT NULL," +
                "informacion TEXT," +
                "estado TEXT," +
                "FOREIGN KEY(dni_usuario) REFERENCES usuarios(DNI)" +
                ")";
        db.execSQL(CREATE_TABLE_PACIENTES);

        // Crea la tabla medico
        String CREATE_TABLE_MEDICO = "CREATE TABLE medicos (" +
                "id_medico INTEGER PRIMARY KEY," +
                "dni_usuario INTEGER NOT NULL," +
                "contrasena TEXT," +
                "centro_medico TEXT," +
                "prioridad_ojo INTEGER," +
                "FOREIGN KEY(dni_usuario) REFERENCES usuarios(DNI)" +
                ")";
        db.execSQL(CREATE_TABLE_MEDICO);

        // Crea la tabla informe
        String CREATE_TABLE_INFORME = "CREATE TABLE informes (" +
                "id_informe INTEGER PRIMARY KEY AUTOINCREMENT," +
                "dni_paciente INTEGER," +
                "imagen_del_informe BLOB," +
                "ojo_imagen TEXT," +
                "resultado INTEGER," +
                "fecha TEXT," +
                "FOREIGN KEY(DNI_paciente) REFERENCES pacientes(DNI)" +
                ")";
        db.execSQL(CREATE_TABLE_INFORME);
        ContentValues valores = new ContentValues();
        valores.put("nombre", "Medico1");
        valores.put("apellido", "Es el medico 1");
        valores.put("correo", "medico1@gmail.com");
        valores.put("DNI", 11111111);
        valores.put("tipo_usuario", 0);
        valores.put("fecha_nacimiento", "1990-05-02");
        db.insert("usuarios", null, valores);
        valores.clear();
        valores.put("nombre", "Paciente1");
        valores.put("apellido", "Es el paciente 1");
        valores.put("correo", "paciente1@gmail.com");
        valores.put("DNI", 12345678);
        valores.put("tipo_usuario", 1);
        valores.put("fecha_nacimiento", "2005-01-25");
        db.insert("usuarios", null, valores);

        valores.clear();
        valores.put("id_medico", 1);
        valores.put("dni_usuario",11111111);
        valores.put("contrasena","contrasena");
        valores.put("centro_medico","San Agustin");
        valores.put("prioridad_ojo",0);
        db.insert("medicos",null,valores);

        valores.clear();
        valores.put("id_paciente",2);
        valores.put("dni_usuario",12345678);
        valores.put("informacion","Tiene diabetes");
        valores.put("estado","NPDR");
        db.insert("pacientes",null,valores);

        valores.clear();
        valores.put("dni_paciente",12345678);
        valores.put("imagen_del_informe",bitmapToBLOB(BitmapFactory.decodeResource(context.getResources(), R.drawable.logo),2048,2048,50));
        valores.put("ojo_imagen","derecho");
        valores.put("resultado", 0);
        valores.put("fecha","2023-04-23");
        db.insert("informes",null,valores);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS informes");
        db.execSQL("DROP TABLE IF EXISTS medicos");
        db.execSQL("DROP TABLE IF EXISTS pacientes");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS informes");
        db.execSQL("DROP TABLE IF EXISTS medicos");
        db.execSQL("DROP TABLE IF EXISTS pacientes");
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    private byte[] bitmapToBLOB(Bitmap bitmap, int width, int height, int quality){
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        return outputStream.toByteArray();
    }
}
