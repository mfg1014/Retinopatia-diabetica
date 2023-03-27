package DataBase;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import DataBase.Usuario;

public class Medico extends Usuario {

    private String contraseña;
    private String centroMedico;
    public Medico(String nombre, String apellido, String correo, int DNI,
                  LocalDate fechaNacimiento, String contraseña, String centroMedico) {
        super(nombre, apellido, correo, DNI, fechaNacimiento);
        super.setTipoUsuario(true);
        this.contraseña = contraseña;
        this.centroMedico = centroMedico;
    }

    public String getCentroMedico() {
        return centroMedico;
    }

    public void setCentroMedico(String centroMedico) {
        this.centroMedico = centroMedico;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }




}
