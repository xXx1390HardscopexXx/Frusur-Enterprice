package Modelo;

public class JefeDeLinea extends Persona {
    private String linea; // Ej: "Línea de Congelado 1"

    public JefeDeLinea(Rut rut, String nombre, String contacto, String linea) {
        super(rut, nombre, contacto);
        this.linea = linea;
    }

    public String getLinea() {
        return linea;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public String entregarInformacion() {
        return "Reporte de estado de " + linea;
    }
}