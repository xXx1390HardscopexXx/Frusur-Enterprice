package Utilidades;

import java.io.Serializable;

public class Rut implements Serializable {
    private int num;
    private char dv;

    // Constructor privado
    private Rut(int num, char dv) {
        this.num = num;
        this.dv = Character.toUpperCase(dv);
    }

    // Getters
    public int getNum() {
        return num;
    }

    public char getDv() {
        return dv;
    }

    // Formato utilidades.Rut: 99.999.999-X o 9.999.999-X
    @Override
    public String toString() {
        String numStr = String.format("%,d", num).replace(",", ".");
        return numStr + "-" + dv;
    }

    // Comparar si num y dv son equivalentes a otro objeto utilidades.Rut
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Rut)) return false;
        Rut otherRut = (Rut) object;
        return this.num == otherRut.num && this.dv == otherRut.dv;
    }

    // Crear un objeto Rut a partir de un String con formato válido
    public static Rut of(String rutConDv) {
        if (rutConDv == null || rutConDv.trim().isEmpty()) {
            return null; // No se permite un RUT nulo o vacío
        }

        // Limpiar el input (quitar espacios en blanco)
        rutConDv = rutConDv.trim();

        // Validar formato: 9.999.999-K o 99.999.999-0
        if (!rutConDv.matches("\\d{1,2}\\.\\d{3}\\.\\d{3}-[kK\\d]")) {
            return null;
        }

        String[] partes = rutConDv.split("-");
        String numStr = partes[0].replace(".", ""); // Quitar puntos
        String dvStr = partes[1];

        // Validar que el RUT sea razonable (ejemplo: mayor a 1 millón)
        int num = Integer.parseInt(numStr);
        if (num < 1000000) {
            return null; // RUTs menores a 1 millón no son válidos
        }

        // Validar si el RUT es correcto antes de crear el objeto
        if (!isValid(num, dvStr.charAt(0))) {
            return null;
        }

        // Crear y retornar el objeto Rut
        return new Rut(num, dvStr.charAt(0));
    }

    // Validar un RUT dado su número y DV
    private static boolean isValid(int num, char dv) {
        dv = Character.toUpperCase(dv);
        int factor = 2;
        int suma = 0;

        // Calcular suma ponderada
        while (num > 0) {
            int digito = num % 10;
            suma += digito * factor;
            factor = (factor == 7) ? 2 : factor + 1;
            num /= 10;
        }

        int resto = 11 - (suma % 11);
        char calculadoDV;

        if (resto == 11) {
            calculadoDV = '0';
        } else if (resto == 10) {
            calculadoDV = 'K';
        } else {
            calculadoDV = (char) (resto + '0');
        }

        return calculadoDV == dv;
    }
}

