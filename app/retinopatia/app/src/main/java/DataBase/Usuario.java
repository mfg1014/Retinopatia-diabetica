package DataBase;

import java.time.LocalDateTime;

public class Usuario {

    private String nombre;
    private String apellido;
    private String correo;
    private int DNI;
    private String tipoUsuario;
    private LocalDateTime fechaNacimiento;

    public Usuario(String nombre, String apellido, String correo,int DNI, LocalDateTime fechaNacimiento) {

        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public void setTipoUsuario(boolean esMedico){
        if( esMedico){
            this.tipoUsuario = "Medico";
        } else{
            this.tipoUsuario = "Paciente";
        }
    }
    public String getTipoUsuario(){return tipoUsuario;}
    public int getDNI(){return  DNI;}
    public LocalDateTime getFechaNacimiento(){return  fechaNacimiento;}
    public void setFechaNacimiento(LocalDateTime fechaNacimiento){this.fechaNacimiento = fechaNacimiento;}
    public Medico getMedico(){
        if(this instanceof Medico){
            Medico medico = (Medico) this;
            return medico;
        }
        return null;
    }
}
