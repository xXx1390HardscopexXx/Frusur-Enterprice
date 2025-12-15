package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planilla implements Serializable {
    private String lineaProceso;
    private final List<Tarja> tarjas = new ArrayList<>();

    public void setLineaProceso(String lineaProceso) {
        this.lineaProceso = lineaProceso;
    }

    public String getLineaProceso() {
        return lineaProceso;
    }

    public void agregarTarja(Tarja t) {
        tarjas.add(t);
    }

    public List<Tarja> getTarjas() {
        return tarjas;
    }

    // Esto reemplaza el mock de 100kg por tarja
    public ResumenProduccion generarResumen() {
        ResumenProduccion resumen = new ResumenProduccion(lineaProceso);
        for (Tarja t : tarjas) {
            resumen.sumar(t.getTipoBerrie(), t.getClasificacion(), t.getKilos());
        }
        return resumen;
    }
}
