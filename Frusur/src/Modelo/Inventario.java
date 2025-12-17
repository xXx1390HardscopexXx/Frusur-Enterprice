package Modelo;

import Utilidades.Codigo;
import java.io.Serializable;
import java.util.*;

public class Inventario implements Serializable {

    private final Map<TipoBerrie, Double> stockIQF = new EnumMap<>(TipoBerrie.class);
    private final Map<TipoBerrie, Double> stockSub = new EnumMap<>(TipoBerrie.class);

    // Mapas para trazabilidad exigida por el proceso
    private final Map<String, Caja> cajasPorCodigo = new HashMap<>();
    private final Map<String, Palet> paletsPorId = new HashMap<>();

    public Inventario() {
        for (TipoBerrie t : TipoBerrie.values()) {
            stockIQF.put(t, 0.0);
            stockSub.put(t, 0.0);
        }
    }

    public void agregarFrutaDesdeRecepcion(TipoBerrie tipo, double kilos) {
        double actual = stockIQF.getOrDefault(tipo, 0.0);
        stockIQF.put(tipo, actual + kilos);
    }

    public void agregarProduccion(ResumenProduccion resumen) {
        for (TipoBerrie t : TipoBerrie.values()) {
            stockIQF.put(t, stockIQF.get(t) + resumen.getKilosIQF().get(t));
            stockSub.put(t, stockSub.get(t) + resumen.getKilosSubproducto().get(t));
        }
    }

    /**
     * Descuenta kilos de materia prima (IQF) para pasar a producción.
     */
    public void descontarMateriaPrima(TipoBerrie tipo, double kilos) {
        double actual = stockIQF.getOrDefault(tipo, 0.0);
        // Ya validamos en el controlador que no sea negativo, así que restamos
        stockIQF.put(tipo, actual - kilos);
    }

    public double getStock(TipoBerrie tipo, ClasificacionProducto clasif) {
        return (clasif == ClasificacionProducto.IQF) ? stockIQF.get(tipo) : stockSub.get(tipo);
    }

    // --- MÉTODOS QUE FALTABAN PARA LA GUI ---

    public Palet crearPalet80DesdeStock(TipoBerrie tipo, ClasificacionProducto clasif, double kilosPorCaja) {
        // Un palet en Frusur consta de 80 cajas
        return crearPaletDesdeStock(tipo, clasif, 80, kilosPorCaja);
    }

    public Palet crearPaletDesdeStock(TipoBerrie tipo, ClasificacionProducto clasif, int cantidadCajas, double kilosPorCaja) {
        double kilosNecesarios = cantidadCajas * kilosPorCaja;
        double disponible = getStock(tipo, clasif);

        if (disponible < kilosNecesarios) {
            throw new IllegalArgumentException("Stock insuficiente de " + tipo + ". Disponible: " + disponible + "kg");
        }

        if (clasif == ClasificacionProducto.IQF) {
            stockIQF.put(tipo, stockIQF.get(tipo) - kilosNecesarios);
        } else {
            stockSub.put(tipo, stockSub.get(tipo) - kilosNecesarios);
        }

        String idPalet = "PAL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Palet palet = new Palet(idPalet);
        palet.setClasificacion(clasif);
        palet.setTipoBerrie(tipo);

        for (int i = 1; i <= cantidadCajas; i++) {
            Caja c = new Caja(tipo.name() + " " + clasif.name());
            c.setKilos(kilosPorCaja);
            c.setTipoBerrie(tipo);
            c.setClasificacion(clasif);

            // Generación de códigos para el despacho
            String info = tipo.name() + "-" + clasif.name() + "-Caja" + i;
            Codigo cod = ServicioCodigo.generarCodigo(info);
            c.setCodigoAsignado(cod);

            palet.agregarCaja(c);
            cajasPorCodigo.put(cod.getCodigoBarras(), c);
        }

        paletsPorId.put(idPalet, palet);
        return palet;
    }

    public String escanearCodigoParaDespacho(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) return "Código vacío.";
        Caja caja = cajasPorCodigo.get(codigoBarras.trim());

        if (caja == null) return "Código no encontrado.";
        if (caja.getEstado() == EstadoCaja.DESPACHADA) return "La caja ya fue despachada.";

        caja.setEstado(EstadoCaja.DESPACHADA); // El departamento de Cámara pistolea los códigos
        return "Despacho exitoso: " + caja.toString();
    }

    public Collection<Palet> getPalets() {
        return paletsPorId.values();
    }

    public int getCantidadCajasRegistradas() {
        return cajasPorCodigo.size();
    }
}
