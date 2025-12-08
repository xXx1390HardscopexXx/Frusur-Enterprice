package Modelo;

import Utilidades.Rut;

import java.io.Serializable;

public abstract class Persona implements Serializable {
    protected Rut rut;
    protected String nombre;
    protected String contacto; // En el diagrama aparece como contacto

    public Persona(Rut rut, String nombre, String contacto) {
        this.rut = rut;
        this.nombre = nombre;
        this.contacto = contacto;
    }

    public Rut getRut() {
        return rut;
    }

    public void setRut(Rut rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    @Override
    public String toString() {
        return nombre + " (" + rut.toString() + ")";
    }
}