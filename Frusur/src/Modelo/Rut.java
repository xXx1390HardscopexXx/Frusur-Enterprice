package Modelo;

import java.util.Arrays;

public class Rut {
    private  int numero;
    private char dv;
    public Rut(int numero, char dv) {
        this.numero = numero;
        this.dv = dv;
    }

    public Rut of(String rutConDv){
        char dv = rutConDv.charAt(rutConDv.length() - 1);
        int numero =  Integer.parseInt(rutConDv.substring(0, rutConDv.length() - 2));
        Rut nuevoRut = new Rut(numero, dv);
        return nuevoRut;
    }
}
