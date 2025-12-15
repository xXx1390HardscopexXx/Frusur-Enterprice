package Modelo;

import Utilidades.Codigo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Palet implements Serializable {
    private List<Caja> cajas;
    private String idPalet;
    private TipoBerrie tipoBerrie;
    private ClasificacionProducto clasificacion;
    private EstadoPalet estado = EstadoPalet.EN_STOCK;

    public Palet(String idPalet) {
        this.idPalet = idPalet;
        this.cajas = new ArrayList<>();
    }

    public void agregarCaja(Caja caja) {
        this.cajas.add(caja);
    }

    public List<Caja> getCajas() {
        return cajas;
    }
    public String getIdPalet() {
        return idPalet;
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

    public EstadoPalet getEstado() {
        return estado;
    }
    public void setEstado(EstadoPalet estado) {
        this.estado = estado;
    }

    public List<Codigo> obtenerCodigosDelPalet() {
        List<Codigo> listaCodigos = new ArrayList<>();
        for (Caja c : cajas) {
            if (c.getCodigoAsignado() != null) listaCodigos.add(c.getCodigoAsignado());
        }
        return listaCodigos;
    }

    @Override
    public String toString() {
        return "Palet " + idPalet + " (" + cajas.size() + " cajas) " +
                (tipoBerrie != null ? tipoBerrie : "-") + " " +
                (clasificacion != null ? clasificacion : "-") + " " + estado;
    }
}
