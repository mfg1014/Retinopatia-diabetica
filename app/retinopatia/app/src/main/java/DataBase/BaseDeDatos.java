package DataBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.example.retinopatia.R;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseDeDatos  {
    private static BaseDeDatos bbdd;
    private Map<String, Medico> medicoMapEmail;
    private Map<Integer, Paciente> pacienteMapDNI;
    private Context context;
    private int contadorInformes;
    private Paciente invitado;
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
        medicoMapEmail = new HashMap<String,Medico>();
        pacienteMapDNI = new HashMap<Integer, Paciente>();
        contadorInformes = 0;

        Medico m1 = new Medico("Medico1","Es el Medico 1", "medico1@gmail.com",11111111, LocalDate.of(1990,5,2),"contraseña","San Agustín");
        medicoMapEmail.put("medico1@gmail.com",m1);

        Medico m2 = new Medico ("Juan", "Gomez Perez", "juan@gmail.com",33333333, LocalDate.of(1984,12,29),"1234","Comuneros");
        medicoMapEmail.put("juan@gmail.com",m2);
        Paciente p1 = new Paciente("Paciente1","Es paciente", "paciente1@gmail.com",12345678,LocalDate.of(2005,1,25),"El paciente tiene diabetes", "NPDR");

        //Se usa una imagen que exista dentro de la applicacion, se cambiará por imagen de retina.
        Bitmap imagenHistorial = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo) ;

        Informe i1 = new Informe(contadorInformes++,12345678,imagenHistorial,"Derecho",3,LocalDate.now());
        p1.agregarInforme(i1);
        pacienteMapDNI.put(12345678,p1);
        Paciente p2 = new Paciente("Pepe", "Garcia","pepe@gmail.com",22222222, LocalDate.of(1973,6,3),"No tiene ninguna patologia", "NPDR leve");
        pacienteMapDNI.put(22222222,p2);
    }

    public String getCorreo(Usuario user){
        return  user.getCorreo();
    }
    public String getContraseña(String email){
        if(!medicoMapEmail.containsKey(email)) {
            return "";
        }

        return medicoMapEmail.get(email).getContraseña();
        }

    public String getPaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getNombre()+" "+pacienteMapDNI.get(DNI).getApellido();
    }
    public List<Informe> getInformes(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return invitado.getInformePaciente();
        }
        return pacienteMapDNI.get(DNI).getInformePaciente();
    }
    public Informe getUltimoInforme(int DNI){
        List<Informe> informes = getInformes(DNI);
        int ultimo = informes.size()-1;
        return informes.get(ultimo);
    }
    public void setResultadoUltimoInforme(int DNI,int resultado){
        List<Informe> informes = getInformes(DNI);
        int ultimo = informes.size()-1;
        informes.get(ultimo).setResultado(resultado);
    }
    public void añadirInforme(Bitmap foto,int DNI,String ojo, int resultado){
        Informe nuevoInforme = new Informe(contadorInformes++,DNI,foto,ojo,resultado,LocalDate.now());
        pacienteMapDNI.get(DNI).agregarInforme(nuevoInforme);

    }
    public String getNombrePaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getNombre();
    }
    public String getApellidoPaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getApellido();
    }
    public String getFechaNacimientoPaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getFechaNacimiento().toString();
    }
    public String getEstado(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getEstado();
    }
    public String getInformacionPaciente(int DNI){
        if(!pacienteMapDNI.containsKey(DNI)) {
            return "";
        }
        return pacienteMapDNI.get(DNI).getInformacion();
    }

    public String getNombreMedico(String email){
        if(!medicoMapEmail.containsKey(email)) {
            return "";
        }
        return medicoMapEmail.get(email).getNombre();
    }
    public String getApellidoMedico(String email){
        if(!medicoMapEmail.containsKey(email)) {
            return "";
        }
        return medicoMapEmail.get(email).getApellido();
    }
    public String getFechaNacimientoMedico(String email){
        if(!medicoMapEmail.containsKey(email)) {
            return "";
        }
        return medicoMapEmail.get(email).getFechaNacimiento().toString();
    }
    public String getCentroMedico(String email){
        if(!medicoMapEmail.containsKey(email)) {
            return "";
        }
        return medicoMapEmail.get(email).getCentroMedico();
    }
    public int getDNIMedico(String email){
        return medicoMapEmail.get(email).getDNI();
    }

    public void addInvitado(){
        invitado = new Paciente("invitado",null,null,0,null,null,null);
    }
    public void añadirInforme(Bitmap foto,String ojo, int resultado){
        Informe nuevoInforme = new Informe(contadorInformes++,foto,ojo,resultado,LocalDate.now());
        invitado.agregarInforme(nuevoInforme);

    }
}
