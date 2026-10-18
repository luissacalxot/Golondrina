/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author allan
 */
import modelo.Ruta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RutaDAO {

public void crear(Ruta ruta) throws SQLException {
String sql = "INSERT INTO rutas (estacion_salida, destino, precio_base) VALUES (?, ?, ?)";
try (Connection conn = ConexionBD.obtenerConexion();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, ruta.getEstacionSalida());
ps.setString(2, ruta.getDestino());
ps.setDouble(3, ruta.getPrecioBase());
ps.executeUpdate();
}
}

public List<Ruta> listar() throws SQLException {
List<Ruta> rutas = new ArrayList<>();
String sql = "SELECT * FROM rutas";
try (Connection conn = ConexionBD.obtenerConexion();
Statement st = conn.createStatement();
ResultSet rs = st.executeQuery(sql)) {
while (rs.next()) {
rutas.add(new Ruta(
rs.getInt("id"),
rs.getString("estacion_salida"),
rs.getString("destino"),
rs.getDouble("precio_base")
));
}
}
return rutas;
}

public Ruta buscarPorId(int id) throws SQLException {
String sql = "SELECT * FROM rutas WHERE id = ?";
try (Connection conn = ConexionBD.obtenerConexion();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setInt(1, id);
try (ResultSet rs = ps.executeQuery()) {
if (rs.next()) {
return new Ruta(
rs.getInt("id"),
rs.getString("estacion_salida"),
rs.getString("destino"),
rs.getDouble("precio_base")
);
}
}
}
return null;
}

public void actualizar(Ruta ruta) throws SQLException {
String sql = "UPDATE rutas SET estacion_salida = ?, destino = ?, precio_base = ? WHERE id = ?";
try (Connection conn = ConexionBD.obtenerConexion();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setString(1, ruta.getEstacionSalida());
ps.setString(2, ruta.getDestino());
ps.setDouble(3, ruta.getPrecioBase());
ps.setInt(4, ruta.getId());
ps.executeUpdate();
}
}

public void eliminar(int id) throws SQLException {
String sql = "DELETE FROM rutas WHERE id = ?";
try (Connection conn = ConexionBD.obtenerConexion();
PreparedStatement ps = conn.prepareStatement(sql)) {
ps.setInt(1, id);
ps.executeUpdate();
}
}
}