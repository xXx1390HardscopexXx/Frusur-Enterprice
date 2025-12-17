package Controlador;

import Excepciones.CFException;
import Modelo.*;
import Persistencia.IOCF;
import Utilidades.Rut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class ControladorFrusur implements Serializable {
    private static ControladorFrusur instance;

    private ArrayList<CuentaAgro> cuentasAgro = new ArrayList<>();
    private ArrayList<CuentaEstadi> cuentasEstadi = new ArrayList<>();
    private ArrayList<Productor> productores = new ArrayList<>();
    private ArrayList<Supervisor> supervisores = new ArrayList<>();
    private ArrayList<Modelo.ResumenProduccion> historicoProduccion = new ArrayList<>();

    private Modelo.Inventario inventario = new Modelo.Inventario();
    private double totalKilosAcumulados = 0;

    private ControladorFrusur() {}

    public static ControladorFrusur getInstance() {
        if (instance == null) {
            instance = new ControladorFrusur();
        }
        return instance;
    }

    // ---------------------------------------------------------
    // GESTIÓN DE PRODUCTORES Y COMPRA (Agrónomo)
    // ---------------------------------------------------------

    public void gestionarCompraProductor(Rut rut, String nombre, String contacto, TipoBerrie berrie, double kilos) throws CFException {
        if (kilos <= 0) {
            throw new CFException("La cantidad de kilos debe ser mayor a 0.");
        }

        Optional<Productor> prodOpt = findProductor(rut);
        Productor p;

        if (prodOpt.isPresent()) {
            p = prodOpt.get();
            if (p.getEstado() != EstadoProductor.RECIBIDO && p.getEstado() != null) {
                throw new CFException("El productor ya tiene una solicitud activa (" + p.getEstado() + ").");
            }
            p.setNombre(nombre);
            p.setContacto(contacto);
        } else {
            p = new Productor(rut, nombre, contacto);
            productores.add(p);
        }

        p.nuevaSolicitud(berrie, kilos);
    }

    public String avanzarEstadoProductor(Productor p, boolean aceptado) throws CFException {
        EstadoProductor actual = p.getEstado();

        if (!aceptado) {
            return "Operación rechazada. El estado se mantiene en " + actual;
        }

        switch (actual) {
            case PENDIENTE: // Establecer acuerdo y contrato
                p.setEstado(EstadoProductor.EN_PROCESO);
                return "Contrato firmado. Estado: En Proceso.";

            case EN_PROCESO: // Charla técnica (prevención de plagas, etc.)
                p.setEstado(EstadoProductor.HABILITADO);
                return "Charla realizada. Estado: Habilitado.";

            case HABILITADO: // Recepción de materia prima en planta [cite: 19, 7]
                TipoBerrie tipo = p.getTipoBerrieActual();
                double kilos = p.getKilosActuales();

                this.inventario.agregarFrutaDesdeRecepcion(tipo, kilos);
                this.totalKilosAcumulados += kilos;

                p.setEstado(EstadoProductor.RECIBIDO);
                return "Fruta recibida: " + kilos + "kg de " + tipo + ". Guardado en Inventario.";

            case RECIBIDO:
                return "La fruta ya fue ingresada al sistema.";

            default:
                return "Sin acciones pendientes.";
        }
    }

    // ---------------------------------------------------------
    // GESTIÓN DE PRODUCCIÓN (Estadístico)
    // ---------------------------------------------------------

    /**
     * Valida stock y descuenta para procesar según las tarjas de recepción.
     */
    public void consumirMateriaPrimaPorTipo(TipoBerrie tipo, double kilosRequeridos) throws CFException {
        double stockDisponible = this.inventario.getStock(tipo, ClasificacionProducto.IQF);

        if (kilosRequeridos > stockDisponible) {
            throw new CFException("Stock insuficiente de " + tipo + ". " +
                    "Disponible: " + stockDisponible + " kg.");
        }

        // LÍNEA CORREGIDA: Usamos el método de la clase Inventario
        this.inventario.descontarMateriaPrima(tipo, kilosRequeridos);

        this.totalKilosAcumulados -= kilosRequeridos;
    }

    public void registrarProduccion(ResumenProduccion resumen) throws CFException {
        if (resumen == null) throw new CFException("Resumen nulo.");
        // Ingresa la producción final (IQF y Subproductos) al inventario
        this.inventario.agregarProduccion(resumen);
        historicoProduccion.add(resumen);
    }

    // ---------------------------------------------------------
    // GETTERS Y PERSISTENCIA
    // ---------------------------------------------------------

    public Modelo.Inventario getInventario() { return inventario; }

    public ArrayList<Productor> getProductores() { return this.productores; }

    public void guardar() throws CFException {
        Object[] controladores = {this};
        IOCF.getInstance().guardar(controladores);
    }

    public void cargar() throws CFException {
        Object[] controladores = IOCF.getInstance().cargar();
        for (Object controlador : controladores) {
            if (controlador instanceof ControladorFrusur) {
                // Esta línea es la clave: recupera todo el objeto, incluyendo el inventario
                instance = (ControladorFrusur) controlador;
            }
        }
    }

    public void createCuentaAgronomo(String usuario, String contrasena) throws CFException {
        if (findAgronomo(usuario).isPresent()) throw new CFException("Usuario ya existe.");
        cuentasAgro.add(new CuentaAgro(usuario, contrasena));
    }

    public void createCuentaEstadi(String usuario, String contrasena) throws CFException {
        if (findEstadistico(usuario).isPresent()) throw new CFException("Usuario ya existe.");
        cuentasEstadi.add(new CuentaEstadi(usuario, contrasena));
    }

    public Optional<CuentaAgro> findAgronomo(String usuario){
        return cuentasAgro.stream().filter(a -> a.getUsuario().equals(usuario)).findFirst();
    }

    public Optional<CuentaEstadi> findEstadistico(String usuario){
        return cuentasEstadi.stream().filter(e -> e.getUsuario().equals(usuario)).findFirst();
    }

    private Optional<Productor> findProductor(Rut rut){
        return productores.stream().filter(p -> p.getRut().equals(rut)).findFirst();
    }
}
