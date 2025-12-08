package Modelo;

// Asegúrate de importar tu clase Rut correctamente.

import Utilidades.Rut;

import java.io.Serializable;

public class Estadistico extends Persona implements Serializable {

    // Dependencias: A quién reporta y qué documento está trabajando
    private Supervisor supervisorTurno;
    private Planilla planillaActual;

    public Estadistico(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    // --- Configuración (Inyección de Dependencias) ---
    public void setSupervisorTurno(Supervisor supervisorTurno) {
        this.supervisorTurno = supervisorTurno;
    }

    // --- Lógica de Negocio (Inventario y Producción) ---

    /**
     * Inicia una nueva planilla digital para el turno.
     * Requisito: Registrar la línea de proceso (Congelado, Selección, etc.).
     * * @param nombreLinea El nombre de la línea donde se está trabajando.
     */
    public void desarrollarPlanilla(String nombreLinea) {
        this.planillaActual = new Planilla();
        this.planillaActual.setLineaProceso(nombreLinea);
        System.out.println("Estadístico " + this.nombre + " inició planilla en: " + nombreLinea);
    }

    /**
     * Registra el ingreso de una Tarja (Materia Prima) en la planilla actual.
     * Cumple con: "Ingreso de datos desde las tarjas entregadas por recepción".
     */
    public void agregarTarjas(Tarja tarja) {
        if (planillaActual != null) {
            planillaActual.agregarTarja(tarja);
            System.out.println("   -> Tarja registrada: " + tarja.toString());
        } else {
            System.out.println("ERROR: No ha iniciado una planilla. Use desarrollarPlanilla() primero.");
        }
    }

    /**
     * Realiza el cierre de cálculos (suma de kilos procesados).
     */
    public void calcularTotalKilos() {
        if (planillaActual != null) {
            planillaActual.calcularTotalKilos();
        }
    }

    /**
     * Envía la información digitalizada al supervisor.
     * Cumple con: "agilizar la entrega de información al supervisor"[cite: 223].
     */
    public void entregarPlanillaASupervisor() {
        if (supervisorTurno != null && planillaActual != null) {
            System.out.println("Entregando reporte al supervisor " + supervisorTurno.getNombre() + "...");
            supervisorTurno.recibirPlanilla(planillaActual);
        } else {
            System.out.println("ERROR: No se puede entregar (Falta Supervisor o Planilla vacía).");
        }
    }

    // Getter útil para que el Controlador pueda mostrar los datos en la Pantalla
    public Planilla getPlanillaActual() {
        return planillaActual;
    }
}