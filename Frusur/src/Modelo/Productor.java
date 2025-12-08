package Modelo;

import Utilidades.Rut;

import java.io.Serializable;

public class Productor extends Persona implements Serializable {
    EstadoProductor estado;
    public Productor(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
        estado = EstadoProductor.PENDIENTE;
    }

    // Retorna true si acepta, false si no. Se asume true para el ejemplo.
    public boolean aceptarAcuerdo() {
        estado = EstadoProductor.EN_PROCESO;
        return true;
    }
    
    public boolean recibirCharla(){
        estado = EstadoProductor.HABILITADO;
        return true;
    }

    public MateriaPrima llevarMateriaPrima(String tipoFruta, double kilos) {
        // Crea la materia prima asociándose a sí mismo como origen
        return new MateriaPrima(tipoFruta, kilos, this);
    }
}