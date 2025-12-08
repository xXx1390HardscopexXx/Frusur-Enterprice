package Modelo;

import java.io.Serializable;

public class MateriaPrima implements Serializable {
    private String tipoFruta;         // Atributo del diagrama
    private Productor productorOrigen; // Relación con Productor
    private double kilos;             // Necesario por lógica de negocio (aunque el diagrama a veces recorta atributos básicos)

    public MateriaPrima(String tipoFruta, double kilos, Productor productorOrigen) {
        this.tipoFruta = tipoFruta;
        this.kilos = kilos;
        this.productorOrigen = productorOrigen;
    }

    public String getTipoFruta() {
        return tipoFruta;
    }

    public void setTipoFruta(String tipoFruta) {
        this.tipoFruta = tipoFruta;
    }

    public Productor getProductorOrigen() {
        return productorOrigen;
    }

    public void setProductorOrigen(Productor productorOrigen) {
        this.productorOrigen = productorOrigen;
    }

    public double getKilos() {
        return kilos;
    }

    public void setKilos(double kilos) {
        this.kilos = kilos;
    }

    @Override
    public String toString() {
        return "MateriaPrima: " + tipoFruta + " (" + kilos + "kg) - Prod: " + productorOrigen.getNombre();
    }
}