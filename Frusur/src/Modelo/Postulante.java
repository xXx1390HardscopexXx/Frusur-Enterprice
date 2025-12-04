package Modelo;

public class Postulante extends Persona {
    private Curriculum curriculum;
    private String infoSalud;
    private String infoAFP;

    public Postulante(Rut rut, String nombre, String contacto, Curriculum curriculum) {
        super(rut, nombre, contacto);
        this.curriculum = curriculum;
    }

    // Getters y Setters específicos
    public Curriculum getCurriculum() { return curriculum; }
    public void setCurriculum(Curriculum curriculum) { this.curriculum = curriculum; }
    public String getInfoSalud() { return infoSalud; }
    public void setInfoSalud(String infoSalud) { this.infoSalud = infoSalud; }
    public String getInfoAFP() { return infoAFP; }
    public void setInfoAFP(String infoAFP) { this.infoAFP = infoAFP; }

    // Métodos del diagrama
    public void asistirCharla(CharlaInduccion charla) {
        System.out.println("Postulante " + nombre + " asistiendo a inducción.");
    }

    public void entregarInformacionPersonal() {
        System.out.println("Entregando info salud: " + infoSalud + " y AFP: " + infoAFP);
    }
}