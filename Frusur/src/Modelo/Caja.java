package Modelo;

import Utilidades.Codigo;
import java.io.Serializable;

public class Caja implements Serializable {
    private String producto;
    private Codigo codigoAsignado;
    private TipoBerrie tipoBerrie;
    private ClasificacionProducto clasificacion;
    private double kilos;
    private EstadoCaja estado = EstadoCaja.EN_STOCK;

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


    public TipoBerrie getTipoBerrie() {
        return tipoBerrie;
    }
    public void setTipoBerrie(TipoBerrie tipoBerrie) {
        this.tipoBerrie = tipoBerrie;
    }

    public ClasificacionProducto getClasificacion() {
        return clasificacion;
    }
    public void setClasificacion(ClasificacionProducto clasificacion) {
        this.clasificacion = clasificacion;
    }

    public double getKilos() {
        return kilos;
    }
    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    public EstadoCaja getEstado() {
        return estado;
    }
    public void setEstado(EstadoCaja estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        String codStr = (codigoAsignado != null) ? codigoAsignado.getCodigoBarras() : "S/C";
        return "Caja: " + producto + " [" + codStr + "] " +
                (tipoBerrie != null ? tipoBerrie : "-") + " " +
                (clasificacion != null ? clasificacion : "-") + " " +
                kilos + "kg " + estado;
    }
}
