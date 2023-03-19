package DataBase;

import android.graphics.Bitmap;

public class Informe {
    private int idInforme;
    private int DNIPaciente;
    private Bitmap imagenDelInforme;
    private String ojoImagen;
    private int resultado;
    public Informe(int idInforme, int DNIPaciente, Bitmap imagen, String ojo, int resultado){
        this.idInforme = idInforme;
        this.DNIPaciente = DNIPaciente;
        this.imagenDelInforme = imagen;
        this.ojoImagen = ojo;
        this.resultado = resultado;
    }
    public Informe(int idInforme, Bitmap imagen, String ojo, int resultado){
        this.idInforme = idInforme;
        this.imagenDelInforme = imagen;
        this.ojoImagen = ojo;
        this.resultado = resultado;
    }

    public void setDNIPaciente(int DNIPaciente) {
        this.DNIPaciente = DNIPaciente;
    }

    public Bitmap getImagenDelInforme() {
        return imagenDelInforme;
    }

    public int getIdInforme() {
        return idInforme;
    }

    public int getDNIPaciente() {
        return DNIPaciente;
    }

    public int getResultado() {
        return resultado;
    }


    public void setImagenDelInforme(Bitmap imagenDelInforme) {
        this.imagenDelInforme = imagenDelInforme;
    }

    public void setOjoImagen(String ojoImagen) {
        this.ojoImagen = ojoImagen;
    }

    public String getOjoImagen() {
        return ojoImagen;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
}
