package DataBase;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class BaseDeDatos {
    private static BaseDeDatos bbdd;
    private Map<String, Usuario> usuarioMapEmail;
    private Map<Integer, Paciente> pacienteMapDNI;

    public static BaseDeDatos getBaseDeDatos(){
        if(bbdd == null){
            bbdd = new BaseDeDatos();
            return  bbdd;
        }
        return bbdd;

    }
    private BaseDeDatos()
    {
        usuarioMapEmail = new HashMap<String,Usuario>();
        pacienteMapDNI = new HashMap<Integer, Paciente>();

        Medico m1 = new Medico("Medico1","Es el Medico 1", "medico1@gmail.com",11111111, LocalDateTime.MIN,"contraseña","San Agustín");
        usuarioMapEmail.put("medico1@gmail.com",m1);

        Paciente p1 = new Paciente("Paciente1","Es paciente", "paciente1@gmail.com",12345678,LocalDateTime.MIN,"El paciente tiene diabetes", "NPDR");
        usuarioMapEmail.put("paciente1@gmail.com",p1);
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
}
