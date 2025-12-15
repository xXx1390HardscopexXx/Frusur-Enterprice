package Modelo;

import java.io.Serializable;

public class Tarja implements Serializable {
    private TipoBerrie tipoBerrie;
    private ClasificacionProducto clasificacion;
    private double kilos;
    private String detalle; // opcional: lote, observación, etc.

    public Tarja(TipoBerrie tipoBerrie, ClasificacionProducto clasificacion, double kilos, String detalle) {
        this.tipoBerrie = tipoBerrie;
        this.clasificacion = clasificacion;
        this.kilos = kilos;
        this.detalle = detalle;
    }

    public TipoBerrie getTipoBerrie() { return tipoBerrie; }
    public ClasificacionProducto getClasificacion() { return clasificacion; }
    public double getKilos() { return kilos; }
    public String getDetalle() { return detalle; }

    @Override
    public String toString() {
        return "Tarja: " + tipoBerrie + " - " + clasificacion + " - " + kilos + " kg" +
                (detalle != null && !detalle.isBlank() ? " (" + detalle + ")" : "");
    }
}
