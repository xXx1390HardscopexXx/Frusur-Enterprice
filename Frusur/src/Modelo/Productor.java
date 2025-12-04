package Modelo;

public class Productor extends Persona {

    public Productor(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    // Métodos según diagrama
    public void serContactado(Agronomo agronomo) {
        System.out.println("El productor " + this.nombre + " ha sido contactado por " + agronomo.getNombre());
    }

    // Retorna true si acepta, false si no. Se asume true para el ejemplo.
    public boolean aceptarAcuerdo(AcuerdoCompra acuerdo) {
        System.out.println("Productor acepta el acuerdo.");
        return true;
    }

    public void asistirCharla(CharlaProductores charla) {
        System.out.println("Productor asistiendo a charla sobre: " + charla.getControlPlagas());
    }

    public MateriaPrima llevarMateriaPrima(String tipoFruta, double kilos) {
        // Crea la materia prima asociándose a sí mismo como origen
        return new MateriaPrima(tipoFruta, kilos, this);
    }
}