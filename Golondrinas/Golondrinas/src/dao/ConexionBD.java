/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {

private static final String URL = "jdbc:sqlite:golondrinas.db";

public static Connection obtenerConexion() throws SQLException {
Connection conn = DriverManager.getConnection(URL);
try (Statement st = conn.createStatement()) {
st.execute("PRAGMA foreign_keys = ON");
}
return conn;
}

public static void inicializar() throws SQLException, IOException {
StringBuilder sql = new StringBuilder();
try (InputStream in = ConexionBD.class.getResourceAsStream("/recursos/schema.sql")) {
if (in == null) {
throw new IOException("No se encontró recursos/schema.sql en el proyecto.");
}
try (BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
String linea;
while ((linea = br.readLine()) != null) {
if (!linea.trim().startsWith("--")) {
sql.append(linea).append("\n");
}
}
}
}
try (Connection conn = obtenerConexion(); Statement st = conn.createStatement()) {
for (String sentencia : sql.toString().split(";")) {
if (!sentencia.trim().isEmpty()) {
st.execute(sentencia);
}
}
}
}
}
