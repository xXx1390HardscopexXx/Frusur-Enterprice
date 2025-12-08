package Modelo;
import Utilidades.Codigo;

import java.io.Serializable;
import java.util.List;

public class Camara implements Serializable {
    public void recibirCodigos(List<Codigo> codigos) {
        System.out.println("Cámara: Códigos recibidos.");
    }
}