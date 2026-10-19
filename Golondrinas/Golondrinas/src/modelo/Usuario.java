/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 */
public class Usuario extends Persona {
private String usuario;
private String clave;
private Rol rol;

public Usuario(int id, String nombre, String telefono, String usuario, String clave, Rol rol) {
super(id, nombre, telefono);
this.usuario = usuario;
this.clave = clave;
this.rol = rol;
}

public String getUsuario() {
return usuario;
}

public void setUsuario(String usuario) {
this.usuario = usuario;
}

public String getClave() {
return clave;
}

public void setClave(String clave) {
this.clave = clave;
}

public Rol getRol() {
return rol;
}

public void setRol(Rol rol) {
this.rol = rol;
}

public boolean esAdmin() {
return rol == Rol.ADMIN;
}

@Override
public String getDescripcion() {
return "Usuario: " + getNombre() + " [" + rol + "]";
}
}
