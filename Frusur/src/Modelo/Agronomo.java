package Modelo;

import Utilidades.Rut;

import java.io.Serializable;

public class Agronomo extends Persona implements Serializable {

    public Agronomo(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    public AcuerdoCompra establecerAcuerdo(Productor productor) {
        // Crea un acuerdo básico
        return new AcuerdoCompra(productor, this, "Condiciones estándar");
    }


}