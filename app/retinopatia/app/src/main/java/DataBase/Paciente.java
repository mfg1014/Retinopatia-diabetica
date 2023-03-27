package DataBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Paciente extends Usuario {

    private List<Informe> informePaciente;
    private String informacion;
    private String estado;
    public Paciente(String nombre, String apellido, String correo, int DNI, LocalDate fechaNacimiento, String informacion, String estado) {
        super(nombre, apellido, correo, DNI, fechaNacimiento);
        super.setTipoUsuario(false);
        informePaciente = new ArrayList<Informe>();
        this.informacion=informacion;
        this.estado=estado;


    }
    public void agregarInforme(Informe nuevoInforme){
        informePaciente.add(nuevoInforme);
    }
    public List<Informe> getInformePaciente(){return informePaciente;}
    public void setInformacion(String informacion){
        this.informacion=informacion;
    }
    public String getInformacion(){return informacion;}
    public void setEstado(String estado){
        this.estado=estado;
    }
    public String getEstado(){return estado;}

}
