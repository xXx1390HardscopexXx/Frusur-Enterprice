package Modelo;

import Utilidades.Rut; // Asegúrate que tu clase Rut esté en el paquete Utilidades, si no, borra este import

import java.io.Serializable;

public class Supervisor extends Persona implements Serializable {



    public Supervisor(Rut rut, String nombre, String contacto) {
        super(rut, nombre, contacto);
    }

    public String darInstrucciones() {
        return "Instrucciones del turno: Prioridad a la línea de Congelado.";
    }

    public void recibirPlanilla(Planilla planilla) {
        ResumenProduccion resumen = planilla.generarResumen();

        System.out.println(
                "Supervisor " + this.nombre +
                        " valida planilla - Total: " +
                        resumen.getTotalGeneral() + " kg " +
                        "(IQF: " + resumen.getTotalIQF() +
                        " | Subproducto: " + resumen.getTotalSubproducto() + ")"
        );
    }


    public void digitalizarInformacion(Planilla planilla) {
        System.out.println("Guardando planilla en base de datos...");
    }
}