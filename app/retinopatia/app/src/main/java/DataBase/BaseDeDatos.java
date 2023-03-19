package DataBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.retinopatia.R;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeDatos  {
    private static BaseDeDatos bbdd;
    private Map<String, Usuario> usuarioMapEmail;
    private Map<Integer, Paciente> pacienteMapDNI;
    private Context context;
    private int contadorInformes;

    public static BaseDeDatos getBaseDeDatos(Context context){
        if(bbdd == null){
            bbdd = new BaseDeDatos(context);
            return  bbdd;
        }
        return bbdd;

    }
    private BaseDeDatos(Context context)
    {
        this.context=context;
        usuarioMapEmail = new HashMap<String,Usuario>();
        pacienteMapDNI = new HashMap<Integer, Paciente>();
        contadorInformes = 0;

        Medico m1 = new Medico("Medico1","Es el Medico 1", "medico1@gmail.com",11111111, LocalDateTime.MIN,"contraseña","San Agustín");
        usuarioMapEmail.put("medico1@gmail.com",m1);

        Paciente p1 = new Paciente("Paciente1","Es paciente", "paciente1@gmail.com",12345678,LocalDateTime.MIN,"El paciente tiene diabetes", "NPDR");
        usuarioMapEmail.put("paciente1@gmail.com",p1);


        Bitmap imagenHistorial = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo) ;

        Informe i1 = new Informe(contadorInformes++,12345678,imagenHistorial,"Derecho",3);
        p1.agregarInforme(i1);
        pacienteMapDNI.put(12345678,p1);
    }

    public String getCorreo(Usuario user){
        return  user.getCorreo();
    }
    public String getContraseña(String email){
        if(!usuarioMapEmail.containsKey(email)) {
            return "";
        }
        Usuario user = usuarioMapEmail.get(email);

        if(user.getTipoUsuario() == "Medico"){
            Medico medico = user.getMedico();
            return medico.getContraseña();
        }
    return "";
    }
    public String getPaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getNombre()+" "+pacienteMapDNI.get(DNI).getApellido();
    }
    public List<Informe> getInformes(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return null;
        }
        return pacienteMapDNI.get(DNI).getInformePaciente();
    }
    public void añadirInforme(Bitmap foto,int DNI,String ojo, int resultado){
        Informe nuevoInforme = new Informe(contadorInformes++,DNI,foto,ojo,resultado);
        pacienteMapDNI.get(DNI).agregarInforme(nuevoInforme);

    }
}
