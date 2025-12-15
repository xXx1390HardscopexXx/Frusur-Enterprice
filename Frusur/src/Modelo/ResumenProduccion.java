package Modelo;

import java.io.Serializable;
import java.util.EnumMap;
import java.util.Map;

public class ResumenProduccion implements Serializable {

    // Totales por tipo berrie para IQF y Subproducto
    private final Map<TipoBerrie, Double> kilosIQF = new EnumMap<>(TipoBerrie.class);
    private final Map<TipoBerrie, Double> kilosSubproducto = new EnumMap<>(TipoBerrie.class);

    private String lineaProceso;

    public ResumenProduccion(String lineaProceso) {
        this.lineaProceso = lineaProceso;
        // Inicializar en 0 para evitar nulls
        for (TipoBerrie t : TipoBerrie.values()) {
            kilosIQF.put(t, 0.0);
            kilosSubproducto.put(t, 0.0);
        }
    }

    public void sumar(TipoBerrie tipo, ClasificacionProducto clasif, double kilos) {
        if (kilos <= 0) return;
        if (clasif == ClasificacionProducto.IQF) {
            kilosIQF.put(tipo, kilosIQF.get(tipo) + kilos);
        } else {
            kilosSubproducto.put(tipo, kilosSubproducto.get(tipo) + kilos);
        }
    }

    public double getTotalIQF() {
        return kilosIQF.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalSubproducto() {
        return kilosSubproducto.values().stream().mapToDouble(Double::doubleValue).sum();
    }

    public double getTotalGeneral() {
        return getTotalIQF() + getTotalSubproducto();
    }

    public Map<TipoBerrie, Double> getKilosIQF() { return kilosIQF; }
    public Map<TipoBerrie, Double> getKilosSubproducto() { return kilosSubproducto; }
    public String getLineaProceso() { return lineaProceso; }

    @Override
    public String toString() {
        return "ResumenProduccion{" +
                "linea='" + lineaProceso + '\'' +
                ", totalIQF=" + getTotalIQF() +
                ", totalSubproducto=" + getTotalSubproducto() +
                ", totalGeneral=" + getTotalGeneral() +
                '}';
    }
}
