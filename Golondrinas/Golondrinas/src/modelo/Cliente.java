/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author allan
 */
public class Cliente extends Persona {
    private String direccion;

    public Cliente(int id, String nombre, String telefono, String direccion) {
        super(id, nombre, telefono);
        this.direccion = direccion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String getDescripcion() {
        return "Cliente: " + getNombre() + " (Tel. " + getTelefono() + ")";
    }
}