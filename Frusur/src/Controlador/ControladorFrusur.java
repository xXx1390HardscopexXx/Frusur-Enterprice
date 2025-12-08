package Controlador;

import Excepciones.CFException;
import Modelo.*;
import Persistencia.IOCF;
import Utilidades.Rut;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Optional;

public class ControladorFrusur implements Serializable {
    private static ControladorFrusur instance;
    private ArrayList<CuentaAgro> cuentasAgro = new ArrayList<>();
    private ArrayList<CuentaEstadi> cuentasEstadi = new ArrayList<>();
    private ArrayList<Productor> productores = new ArrayList<>();
    private ArrayList<Supervisor> supervisores = new ArrayList<>();

    private ControladorFrusur() {}

    public static ControladorFrusur getInstance() {
        if (instance == null) {
            instance = new ControladorFrusur();
        }
        return instance;
    }

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

    public void createProductor(Rut rut, String nombre, String contacto) throws CFException {
        if(findProductor(rut).isPresent()){
            throw new CFException("Ya existe un productor con el rut indicado");
        }
        Productor newProductor =new Productor(rut,nombre,contacto);
        productores.add(newProductor);
    }

    public void createSupervisor(Rut rut, String nombre, String contacto) throws CFException {
        if(findSupervisor(rut).isPresent()){
            throw new CFException("Ya existe un supervisor con el rut indicado");
        }
        Supervisor newSupervisor =new Supervisor(rut,nombre,contacto);
        supervisores.add(newSupervisor);
    }

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

    public Optional<CuentaAgro> findAgronomo(String usuario){
        for (CuentaAgro agronomo : cuentasAgro){
            if (agronomo.getUsuario().equals(usuario)){
                return Optional.of(agronomo);
            }
        }
        return Optional.empty();
    }
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
}
