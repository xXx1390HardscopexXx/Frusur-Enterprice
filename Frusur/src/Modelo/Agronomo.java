package Modelo;

public class Agronomo extends Persona {

    public Agronomo(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    public void charlaParaProductores(CharlaProductores charla) {
        System.out.println("Agrónomo dando charla: " + charla.getDatosProduccion());
    }

    public void contactarProductor(Productor productor) {
        productor.serContactado(this);
    }

    public AcuerdoCompra establecerAcuerdo(Productor productor) {
        // Crea un acuerdo básico
        return new AcuerdoCompra(productor, this, "Condiciones estándar");
    }

    public CharlaProductores realizarCharla(Productor productor) {
        return new CharlaProductores("Datos 2025", "Control Bio", "Prevención Std");
    }
}