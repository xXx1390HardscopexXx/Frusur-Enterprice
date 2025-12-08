package Utilidades;

import java.io.Serializable;

public class Codigo implements Serializable {
    private String infoProducto;  // [cite: 143]
    private String codigoBarras;  // [cite: 143]

    public Codigo(String infoProducto, String codigoBarras) {
        this.infoProducto = infoProducto;
        this.codigoBarras = codigoBarras;
    }

    public String getInfoProducto() {
        return infoProducto;
    }

    public void setInfoProducto(String infoProducto) {
        this.infoProducto = infoProducto;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    @Override
    public String toString() {
        return codigoBarras + " (" + infoProducto + ")";
    }
}