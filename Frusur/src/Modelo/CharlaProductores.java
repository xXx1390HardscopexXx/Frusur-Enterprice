package Modelo;

public class CharlaProductores {
    private String tema;

    public CharlaProductores(String dato1, String dato2, String dato3) {
        this.tema = dato1;
    }

    public String getDatosProduccion() { return "Datos de Producción"; }
    public String getControlPlagas() { return "Control de Plagas"; }
}