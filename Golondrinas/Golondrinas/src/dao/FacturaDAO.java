/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 */
import excepciones.AsientosInvalidosException;
import modelo.Asiento;
import modelo.Cliente;
import modelo.Factura;
import modelo.Ruta;
import modelo.Usuario;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FacturaDAO {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final RutaDAO rutaDAO = new RutaDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Guarda la factura y le asigna el id generado por la base de datos.
    public void crear(Factura factura) throws SQLException {
        String sql = "INSERT INTO facturas (id_cliente, id_ruta, id_usuario, tipo_asiento, "
                + "cantidad_asientos, cantidad_equipaje, iva, fecha, hora, total) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, factura.getCliente().getId());
            ps.setInt(2, factura.getRuta().getId());
            ps.setInt(3, factura.getUsuario().getId());
            ps.setString(4, factura.getAsiento().getTipo());
            ps.setInt(5, factura.getCantidadAsientos());
            ps.setInt(6, factura.getCantidadEquipaje());
            ps.setDouble(7, factura.getIva());
            ps.setString(8, factura.getFechaLocal().toString());
            ps.setString(9, factura.getHoraLocal().withNano(0).toString());
            ps.setDouble(10, factura.calcularTotal());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    factura.setId(keys.getInt(1));
                }
            }
        }
    }

    public List<Factura> listar() throws SQLException {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas ORDER BY id";
        try (Connection conn = ConexionBD.obtenerConexion();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                facturas.add(mapear(rs));
            }
        }
        return facturas;
    }

    public Factura buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM facturas WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Factura> listarPorCliente(int idCliente) throws SQLException {
        List<Factura> facturas = new ArrayList<>();
        String sql = "SELECT * FROM facturas WHERE id_cliente = ? ORDER BY id";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idCliente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    facturas.add(mapear(rs));
                }
            }
        }
        return facturas;
    }

    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM facturas WHERE id = ?";
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // Reconstruye una Factura desde una fila, cargando cliente, ruta y usuario.
    private Factura mapear(ResultSet rs) throws SQLException {
        Cliente cliente = clienteDAO.buscarPorId(rs.getInt("id_cliente"));
        Ruta ruta = rutaDAO.buscarPorId(rs.getInt("id_ruta"));
        Usuario usuario = usuarioDAO.buscarPorId(rs.getInt("id_usuario"));
        try {
            return new Factura(
                    rs.getInt("id"),
                    cliente,
                    ruta,
                    usuario,
                    Asiento.desdeTipo(rs.getString("tipo_asiento")),
                    rs.getInt("cantidad_asientos"),
                    rs.getInt("cantidad_equipaje"),
                    rs.getDouble("iva"),
                    LocalDate.parse(rs.getString("fecha")),
                    LocalTime.parse(rs.getString("hora"))
            );
        } catch (AsientosInvalidosException e) {
            throw new SQLException("Factura con datos inválidos en la base de datos: " + e.getMessage(), e);
        }
    }
}
