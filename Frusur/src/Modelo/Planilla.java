package Modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Planilla implements Serializable {
    private String lineaProceso;
    // Lista para guardar las tarjas
    private List<Tarja> tarjas = new ArrayList<>();
    private double totalKilos;

    // --- Métodos que reclama tu Estadístico y Supervisor ---

    public void setLineaProceso(String linea) {
        this.lineaProceso = linea;
    }

    // Este es el método que no encontraba el error 1
    public void agregarTarja(Tarja t) {
        this.tarjas.add(t);
    }

    // Este es el método que no encontraba el error 2
    public void calcularTotalKilos() {
        // Simulación: supongamos 100 kilos por tarja para probar
        this.totalKilos = tarjas.size() * 100.0;
        System.out.println("    [Sistema] Planilla calculada: " + totalKilos + " kg.");
    }

    // Este es el método que no encontraba el Supervisor (Error 3)
    public double getTotalKilos() {
        return totalKilos;
    }

    public List<Tarja> getTarjas() {
        return tarjas;
    }
}