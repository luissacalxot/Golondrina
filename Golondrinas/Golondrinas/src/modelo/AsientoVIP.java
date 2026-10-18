/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author allan
 */
public class AsientoVIP extends Asiento {
private static final double RECARGO = 100.00;

public AsientoVIP() {
super("VIP");
}

@Override
public double calcularPrecio(double precioBase) {
return precioBase + RECARGO;
}
}