package Modelo;

import Utilidades.Codigo;

import java.io.Serializable;

public class Caja implements Serializable {
    private String producto;      // Ej: "Frambuesa IQF"
    private Codigo codigoAsignado; // La etiqueta pegada en la caja

    public Caja(String producto) {
        this.producto = producto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public Codigo getCodigoAsignado() {
        return codigoAsignado;
    }

    public void setCodigoAsignado(Codigo codigoAsignado) {
        this.codigoAsignado = codigoAsignado;
    }

    @Override
    public String toString() {
        // Si tiene código lo muestra, si no, avisa.
        String codStr = (codigoAsignado != null) ? codigoAsignado.getCodigoBarras() : "S/C";
        return "Caja: " + producto + " [" + codStr + "]";
    }
}