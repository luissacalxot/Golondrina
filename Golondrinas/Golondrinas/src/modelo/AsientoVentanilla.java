/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author allan
 */
public class AsientoVentanilla extends Asiento {
private static final double RECARGO = 40.00;

public AsientoVentanilla() {
super("VENTANILLA");
}

@Override
public double calcularPrecio(double precioBase) {
return precioBase + RECARGO;
}
}