package Modelo;

import Utilidades.Codigo;

import java.io.Serializable;
import java.util.*;

public class Inventario implements Serializable {

    // Stock en kilos, separado por clasificación
    private final Map<TipoBerrie, Double> stockIQF = new EnumMap<>(TipoBerrie.class);
    private final Map<TipoBerrie, Double> stockSub = new EnumMap<>(TipoBerrie.class);

    // Trazabilidad
    private final Map<String, Caja> cajasPorCodigo = new HashMap<>();
    private final Map<String, Palet> paletsPorId = new HashMap<>();

    public Inventario() {
        for (TipoBerrie t : TipoBerrie.values()) {
            stockIQF.put(t, 0.0);
            stockSub.put(t, 0.0);
        }
    }

    // INGRESO DE PRODUCCIÓN
    public void agregarProduccion(ResumenProduccion resumen) {
        for (TipoBerrie t : TipoBerrie.values()) {
            stockIQF.put(t, stockIQF.get(t) + resumen.getKilosIQF().get(t));
            stockSub.put(t, stockSub.get(t) + resumen.getKilosSubproducto().get(t));
        }
    }

    public double getStock(TipoBerrie tipo, ClasificacionProducto clasif) {
        return (clasif == ClasificacionProducto.IQF) ? stockIQF.get(tipo) : stockSub.get(tipo);
    }

    // CREACIÓN DE PALETS (80 cajas)
    public Palet crearPaletDesdeStock(
            TipoBerrie tipo,
            ClasificacionProducto clasif,
            int cantidadCajas,
            double kilosPorCaja
    ) {
        if (cantidadCajas <= 0) throw new IllegalArgumentException("cantidadCajas debe ser > 0");
        if (kilosPorCaja <= 0) throw new IllegalArgumentException("kilosPorCaja debe ser > 0");

        double kilosNecesarios = cantidadCajas * kilosPorCaja;
        double disponible = getStock(tipo, clasif);
        if (disponible < kilosNecesarios) {
            throw new IllegalArgumentException("Stock insuficiente. Disponible: " + disponible + "kg, necesario: " + kilosNecesarios + "kg");
        }

        // Descontar stock
        if (clasif == ClasificacionProducto.IQF) {
            stockIQF.put(tipo, stockIQF.get(tipo) - kilosNecesarios);
        } else {
            stockSub.put(tipo, stockSub.get(tipo) - kilosNecesarios);
        }

        // Crear palet
        String idPalet = "PAL-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        Palet palet = new Palet(idPalet);
        palet.setClasificacion(clasif);
        palet.setTipoBerrie(tipo);
        palet.setEstado(EstadoPalet.EN_STOCK);

        // Crear cajas con códigos
        for (int i = 1; i <= cantidadCajas; i++) {
            Caja c = new Caja(tipo.name() + " " + clasif.name());
            c.setTipoBerrie(tipo);
            c.setClasificacion(clasif);
            c.setKilos(kilosPorCaja);
            c.setEstado(EstadoCaja.EN_STOCK);

            String info = tipo.name() + "-" + clasif.name() + "-Caja" + i + "-" + idPalet;
            Codigo cod = ServicioCodigo.generarCodigo(info);
            c.setCodigoAsignado(cod);

            palet.agregarCaja(c);
            cajasPorCodigo.put(cod.getCodigoBarras(), c);
        }

        paletsPorId.put(idPalet, palet);
        return palet;
    }

    // Conveniencia: 80 cajas por defecto
    public Palet crearPalet80DesdeStock(TipoBerrie tipo, ClasificacionProducto clasif, double kilosPorCaja) {
        return crearPaletDesdeStock(tipo, clasif, 80, kilosPorCaja);
    }

    // ESCANEO / DESPACHO
    public String escanearCodigoParaDespacho(String codigoBarras) {
        if (codigoBarras == null || codigoBarras.isBlank()) return "Código vacío.";

        Caja caja = cajasPorCodigo.get(codigoBarras.trim());
        if (caja == null) return "Código no encontrado en inventario.";

        if (caja.getEstado() == EstadoCaja.DESPACHADA) {
            return "La caja ya estaba despachada (no se descuenta de nuevo).";
        }

        caja.setEstado(EstadoCaja.DESPACHADA);
        return "Caja despachada correctamente: " + caja.toString();
    }

    // CONSULTAS
    public Map<TipoBerrie, Double> getStockIQF() { return stockIQF; }
    public Map<TipoBerrie, Double> getStockSub() { return stockSub; }

    public Collection<Palet> getPalets() { return paletsPorId.values(); }
    public int getCantidadCajasRegistradas() { return cajasPorCodigo.size(); }
}
