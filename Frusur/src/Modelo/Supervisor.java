package Modelo;

import java.util.List;

public class Supervisor extends Persona {
    private String cargoBuscado;
    private CharlaInduccion charla;

    public Supervisor(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    // Métodos de lógica de negocio del diagrama
    public Postulante revisarCurriculums(List<Curriculum> lista) {
        // Lógica simplificada
        System.out.println("Revisando " + lista.size() + " curriculums.");
        return null; // Retornaría el postulante seleccionado
    }

    public Postulante seleccionaPostulante(List<Curriculum> lista) {
        System.out.println("Seleccionando mejor candidato...");
        return null;
    }

    public void realizarCharlaInduccion(List<Postulante> postulantes) {
        System.out.println("Realizando inducción a " + postulantes.size() + " postulantes.");
    }

    public String getCargoBuscado() {
        return cargoBuscado;
    }

    public void setCargoBuscado(String cargo) {
        this.cargoBuscado = cargo;
    }

    public String darInstrucciones() {
        return "Instrucciones del turno actual...";
    }

    public void recibirPlanilla(Planilla planilla) {
        System.out.println("Planilla recibida. Total Kilos: " + planilla.getTotalKilos());
    }

    public void digitalizarInformacion(Planilla planilla) {
        System.out.println("Guardando planilla en base de datos...");
    }
}