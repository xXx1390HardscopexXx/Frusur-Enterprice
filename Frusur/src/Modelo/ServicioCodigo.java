package Modelo;

import Utilidades.Codigo;

import java.io.Serializable;
import java.util.UUID;

public class ServicioCodigo implements Serializable {

    public static Codigo generarCodigo(String infoProducto) {
        String barras = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return new Codigo(infoProducto, barras);
    }
}
