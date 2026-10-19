/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 */
public abstract class Asiento {
    private final String tipo;

    protected Asiento(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public abstract double calcularPrecio(double precioBase);

    public static Asiento desdeTipo(String tipo) {
        switch (tipo) {
            case "VENTANILLA":
                return new AsientoVentanilla();
            case "VIP":
                return new AsientoVIP();
            default:
                return new AsientoNormal();
        }
    }

    @Override
    public String toString() {
        return tipo;
    }
}
