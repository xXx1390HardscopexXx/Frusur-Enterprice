package Modelo;

import java.io.Serializable;

public class Camara implements Serializable {
    private Inventario inventario;

    public Camara(Inventario inventario) {
        this.inventario = inventario;
    }

    public String escanear(String codigoBarras) {
        return inventario.escanearCodigoParaDespacho(codigoBarras);
    }
}
