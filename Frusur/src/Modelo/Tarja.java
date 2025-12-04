package Modelo;

public class Tarja {
    // En el diagrama aparece como "intoProductoIngresado", asumimos error de OCR -> "info"
    private String infoProductoIngresado;

    public Tarja(String infoProductoIngresado) {
        this.infoProductoIngresado = infoProductoIngresado;
    }

    public String getInfoProductoIngresado() {
        return infoProductoIngresado;
    }

    public void setInfoProductoIngresado(String infoProductoIngresado) {
        this.infoProductoIngresado = infoProductoIngresado;
    }

    @Override
    public String toString() {
        return "Tarja: " + infoProductoIngresado;
    }
}