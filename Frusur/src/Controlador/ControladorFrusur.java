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

    // Listas de usuarios y entidades
    private ArrayList<CuentaAgro> cuentasAgro = new ArrayList<>();
    private ArrayList<CuentaEstadi> cuentasEstadi = new ArrayList<>();
    private ArrayList<Productor> productores = new ArrayList<>();
    private ArrayList<Supervisor> supervisores = new ArrayList<>();
    private ArrayList<Modelo.ResumenProduccion> historicoProduccion = new ArrayList<>();
    // Nuevo atributo para el inventario global de fruta recibida
    private double totalKilosAcumulados = 0;

    private ControladorFrusur() {}

    public static ControladorFrusur getInstance() {
        if (instance == null) {
            instance = new ControladorFrusur();
        }
        return instance;
    }

    // ---------------------------------------------------------
    // MÉTODOS DE CREACIÓN DE USUARIOS (Legacy)
    // ---------------------------------------------------------

    public void createCuentaAgronomo(String usuario, String contrasena) throws CFException {
        if (findAgronomo(usuario).isPresent()){
            throw new CFException("Ya existe un agronomo con ese usuario");
        }
        CuentaAgro ca = new CuentaAgro(usuario, contrasena);
        cuentasAgro.add(ca);
    }

    public void createCuentaEstadi(String usuario, String contrasena) throws CFException {
        if (findEstadistico(usuario).isPresent()){
            throw new CFException("Ya existe un estadistico con ese usuario");
        }
        CuentaEstadi ce = new CuentaEstadi(usuario, contrasena);
        cuentasEstadi.add(ce);
    }

    public void createSupervisor(Rut rut, String nombre, String contacto) throws CFException {
        if(findSupervisor(rut).isPresent()){
            throw new CFException("Ya existe un supervisor con el rut indicado");
        }
        Supervisor newSupervisor =new Supervisor(rut,nombre,contacto);
        supervisores.add(newSupervisor);
    }

    // Este método lo mantenemos por compatibilidad, pero la GUI usará 'gestionarCompraProductor'
    public void createProductor(Rut rut, String nombre, String contacto) throws CFException {
        if(findProductor(rut).isPresent()){
            throw new CFException("Ya existe un productor con el rut indicado");
        }
        Productor newProductor =new Productor(rut,nombre,contacto);
        productores.add(newProductor);
    }

    // ---------------------------------------------------------
    // MÉTODOS DE PERSISTENCIA
    // ---------------------------------------------------------

    public void guardar() throws CFException {
        Object[] controladores = {this};
        IOCF.getInstance().guardar(controladores);
    }

    public void cargar() throws CFException {
        Object[] controladores = IOCF.getInstance().cargar();
        for (Object controlador : controladores) {
            if (controlador instanceof ControladorFrusur) {
                instance = null;
                instance = (ControladorFrusur) controlador;
            }
        }
    }

    // ---------------------------------------------------------
    // MÉTODOS DE BÚSQUEDA (FINDERS)
    // ---------------------------------------------------------

    public Optional<CuentaAgro> findAgronomo(String usuario){
        for (CuentaAgro agronomo : cuentasAgro){
            if (agronomo.getUsuario().equals(usuario)){
                return Optional.of(agronomo);
            }
        }
        return Optional.empty();
    }

    // Lo dejé privado porque los métodos públicos nuevos lo usan internamente
    private Optional<Productor> findProductor(Rut rut){
        for (Productor productor : productores){
            if (productor.getRut().equals(rut)){
                return Optional.of(productor);
            }
        }
        return Optional.empty();
    }

    private Optional<Supervisor> findSupervisor(Rut rut){
        for (Supervisor supervisor : supervisores){
            if (supervisor.getRut().equals(rut)){
                return Optional.of(supervisor);
            }
        }
        return Optional.empty();
    }

    public Optional<CuentaEstadi> findEstadistico(String usuario){
        for (CuentaEstadi estadistico : cuentasEstadi){
            if (estadistico.getUsuario().equals(usuario)){
                return Optional.of(estadistico);
            }
        }
        return Optional.empty();
    }

    // ---------------------------------------------------------
    // NUEVA LÓGICA DE NEGOCIO (COMPRA DE MATERIA PRIMA)
    // ---------------------------------------------------------

    /**
     * Retorna la lista completa de productores para llenado de tablas en la GUI.
     */
    public ArrayList<Productor> getProductores() {
        return this.productores;
    }

    /**
     * Crea un productor si no existe, o actualiza uno existente, y le asigna una nueva solicitud de compra.
     */
    public void gestionarCompraProductor(Rut rut, String nombre, String contacto, TipoBerrie berrie, double kilos) throws CFException {
        if (kilos <= 0) {
            throw new CFException("La cantidad de kilos debe ser mayor a 0.");
        }

        Optional<Productor> prodOpt = findProductor(rut);
        Productor p;

        if (prodOpt.isPresent()) {
            p = prodOpt.get();
            // Validar que no tenga un encargo pendiente (Solo puede comprar si está en estado RECIBIDO o quizás vacío)
            // Asumimos que al crearse inicia en RECIBIDO o PENDIENTE, ajusta según tu lógica inicial.
            // Si el productor está ocupado (Pendiente, En Proceso, Habilitado), no se puede iniciar otra compra.
            if (p.getEstado() != EstadoProductor.RECIBIDO && p.getEstado() != null) {
                throw new CFException("El productor ya tiene una solicitud activa (" + p.getEstado() + "). Debe finalizarla primero.");
            }
            // Si existe y está libre, actualizamos sus datos base por si cambiaron
            p.setNombre(nombre);
            p.setContacto(contacto);
        } else {
            // Si no existe, lo creamos
            p = new Productor(rut, nombre, contacto);
            productores.add(p);
        }

        // Asignamos la nueva solicitud (Esto lo pone en estado PENDIENTE automáticamente)
        p.nuevaSolicitud(berrie, kilos);
    }

    /**
     * Máquina de estados para avanzar en el proceso de compra (Contrato -> Charla -> Recepción).
     */
    public String avanzarEstadoProductor(Productor p, boolean aceptado) throws CFException {
        EstadoProductor actual = p.getEstado();

        if (!aceptado) {
            return "Operación rechazada. El estado se mantiene en " + actual;
        }

        switch (actual) {
            case PENDIENTE:
                // "¿Desea firmar contrato?" -> Si
                p.setEstado(EstadoProductor.EN_PROCESO);
                return "Contrato firmado. Estado: En Proceso.";

            case EN_PROCESO:
                // "¿Desea realizar charla?" -> Si
                p.setEstado(EstadoProductor.HABILITADO);
                return "Charla realizada. Estado: Habilitado.";

            case HABILITADO:
                // "¿Recibir fruta?" -> Si
                this.totalKilosAcumulados += p.getKilosActuales(); // Guardamos en el total del controlador
                p.setEstado(EstadoProductor.RECIBIDO); // Queda libre para otra venta
                return "Fruta recibida (" + p.getKilosActuales() + "kg). Total acumulado en planta: " + totalKilosAcumulados;

            case RECIBIDO:
                return "Este productor ya entregó su carga. Inicie una nueva solicitud.";

            default:
                return "El productor no tiene acciones pendientes.";
        }
    }


    public void registrarProduccion(ResumenProduccion resumen) throws CFException {
        if (resumen == null) throw new CFException("Resumen nulo.");
        if (resumen.getTotalGeneral() <= 0) throw new CFException("Resumen sin producción.");
        historicoProduccion.add(resumen);
    }

    public ArrayList<ResumenProduccion> getHistoricoProduccion() {
        return historicoProduccion;
    }



    public double getTotalKilosAcumulados() {
        return totalKilosAcumulados;
    }
}
