/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 */
public class Ruta {
private int id;
private String estacionSalida;
private String destino;
private double precioBase;

public Ruta(int id, String estacionSalida, String destino, double precioBase) {
this.id = id;
this.estacionSalida = estacionSalida;
this.destino = destino;
setPrecioBase(precioBase);
}

public int getId() {
return id;
}

public void setId(int id) {
this.id = id;
}

public String getEstacionSalida() {
return estacionSalida;
}

public void setEstacionSalida(String estacionSalida) {
this.estacionSalida = estacionSalida;
}

public String getDestino() {
return destino;
}

public void setDestino(String destino) {
this.destino = destino;
}

public double getPrecioBase() {
return precioBase;
}

public void setPrecioBase(double precioBase) {
if (precioBase >= 0) {
this.precioBase = precioBase;
}
}

@Override
public String toString() {
return estacionSalida + " -> " + destino + " (Q" + String.format("%.2f", precioBase) + ")";
}
}
