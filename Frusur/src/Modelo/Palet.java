package Modelo;

import Utilidades.Codigo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Palet implements Serializable {
    // El diagrama muestra una relación de 1 a muchos con Caja
    private List<Caja> cajas;
    private String idPalet; // Identificador del palet completo

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

    // obtenemos codigos de las cajas dentro del paalet
    public List<Codigo> obtenerCodigosDelPalet() {
        List<Codigo> listaCodigos = new ArrayList<>();
        for (Caja c : cajas) {
            if (c.getCodigoAsignado() != null) {
                listaCodigos.add(c.getCodigoAsignado());
            }
        }
        return listaCodigos;
    }

    @Override
    public String toString() {
        return "Palet " + idPalet + " (Contiene " + cajas.size() + " cajas)";
    }
}