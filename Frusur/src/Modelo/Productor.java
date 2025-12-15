package Modelo;
import Utilidades.Rut;
import java.io.Serializable;

public class Productor extends Persona implements Serializable {
    private EstadoProductor estado;
    private TipoBerrie tipoBerrieActual;
    private double kilosActuales;

    public Productor(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
        this.estado = EstadoProductor.RECIBIDO;
        this.kilosActuales = 0;
    }

    public void nuevaSolicitud(TipoBerrie berrie, double kilos) {
        this.tipoBerrieActual = berrie;
        this.kilosActuales = kilos;
        this.estado = EstadoProductor.PENDIENTE;
    }

    public EstadoProductor getEstado() { return estado; }
    public void setEstado(EstadoProductor estado) { this.estado = estado; }
    public TipoBerrie getTipoBerrieActual() { return tipoBerrieActual; }
    public double getKilosActuales() { return kilosActuales; }
}
