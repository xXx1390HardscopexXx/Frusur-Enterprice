package Modelo;

import java.util.List;

public class Estadistico extends Persona {
    // Asociaciones del diagrama
    private Supervisor supervisorTurno;
    private Recepcion deptoRecepcion;
    private JefeDeLinea jefaLineaAsignada; // Relación con JefeDeLinea
    private Camara deptoCamara;
    private Planilla planillaActual;

    public Estadistico(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    // Setters para inyectar dependencias
    public void setSupervisorTurno(Supervisor supervisorTurno) { this.supervisorTurno = supervisorTurno; }
    public void setDeptoRecepcion(Recepcion deptoRecepcion) { this.deptoRecepcion = deptoRecepcion; }
    public void setJefaLineaAsignada(JefeDeLinea jefaLineaAsignada) { this.jefaLineaAsignada = jefaLineaAsignada; }
    public void setDeptoCamara(Camara deptoCamara) { this.deptoCamara = deptoCamara; }

    // Métodos del diagrama
    public void presentarseOficina() {
        System.out.println("Estadístico marcando entrada en oficina.");
    }

    public void recibirInstrucciones() {
        if (supervisorTurno != null) {
            System.out.println("Recibiendo: " + supervisorTurno.darInstrucciones());
        }
    }

    public void irALinea(String linea) {
        System.out.println("Moviéndose a línea: " + linea);
    }

    public void agregarInfoLinea(String info) {
        System.out.println("Agregando info: " + info);
    }

    public void verProceso() {
        System.out.println("Supervisando proceso visualmente.");
    }

    public void agregarTarjas(Tarja tarja) {
        if (planillaActual != null) {
            planillaActual.agregarTarja(tarja);
        }
    }

    public List<Tarja> recibirTarjas() {
        if (deptoRecepcion != null) {
            return deptoRecepcion.entregarTarjas(); // Asumiendo método en Recepción
        }
        return null;
    }

    public void calcularTotalKilos() {
        if (planillaActual != null) {
            planillaActual.calcularTotalKilos();
        }
    }

    // Iniciar planilla nueva
    public void desarrollarPlanilla() {
        this.planillaActual = new Planilla();
    }

    public void entregarPlanillaASupervisor() {
        if (supervisorTurno != null && planillaActual != null) {
            supervisorTurno.recibirPlanilla(planillaActual);
        }
    }
}