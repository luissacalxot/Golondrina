/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package golondrinas;

/**
 *
 */
import dao.ClienteDAO;
import dao.ConexionBD;
import dao.FacturaDAO;
import dao.RutaDAO;
import dao.UsuarioDAO;
import excepciones.AsientosInvalidosException;
import modelo.*;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Golondrinas {

    private static final Scanner scanner = new Scanner(System.in);
    private static final ClienteDAO clienteDAO = new ClienteDAO();
    private static final RutaDAO rutaDAO = new RutaDAO();
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final FacturaDAO facturaDAO = new FacturaDAO();

    public static void main(String[] args) {
        try {
            ConexionBD.inicializar();
        } catch (Exception e) {
            System.out.println("No se pudo preparar la base de datos: " + e.getMessage());
            return;
        }

        try {
            System.out.println("========== GOLONDRINAS S.A. ==========");
            Usuario usuario = login();
            if (usuario == null) {
                return;
            }
            System.out.println("\nBienvenido, " + usuario.getNombre() + " (" + usuario.getRol() + ")");
            menuPrincipal(usuario);
        } catch (NoSuchElementException e) {
            // Se cerró la entrada de datos (por ejemplo Ctrl+D)
            System.out.println("\nEntrada cerrada. Saliendo del programa.");
        }
    }

    // ---------------------------------------------------------------- Login

    private static Usuario login() {
        while (true) {
            String nombreUsuario = leerTexto("Usuario (o 'salir'): ");
            if (nombreUsuario.equalsIgnoreCase("salir")) {
                System.out.println("Hasta luego.");
                return null;
            }
            String clave = leerTexto("Clave: ");
            try {
                Usuario usuario = usuarioDAO.autenticar(nombreUsuario, clave);
                if (usuario != null) {
                    return usuario;
                }
                System.out.println("Usuario o clave incorrectos. Intente de nuevo.\n");
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage() + "\n");
            }
        }
    }

    // ----------------------------------------------------------------- Menú

    private static void menuPrincipal(Usuario usuario) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n---------------- MENU ----------------");
            System.out.println("1. Registrar cliente");
            System.out.println("2. Crear factura");
            System.out.println("3. Listar facturas");
            System.out.println("4. Buscar factura por id");
            System.out.println("5. Salir");
            int opcion = leerEntero("Elija una opcion: ", 1, 5);
            try {
                switch (opcion) {
                    case 1:
                        registrarCliente();
                        break;
                    case 2:
                        crearFactura(usuario);
                        break;
                    case 3:
                        listarFacturas();
                        break;
                    case 4:
                        buscarFactura();
                        break;
                    case 5:
                        salir = true;
                        System.out.println("Hasta luego.");
                        break;
                }
            } catch (AsientosInvalidosException e) {
                System.out.println("Error de validacion: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }
    }

    // ------------------------------------------------------------- Opciones

    private static Cliente registrarCliente() throws SQLException {
        System.out.println("\n--- Registrar cliente ---");
        String nombre = leerTextoNoVacio("Nombre: ");
        String telefono = leerTexto("Telefono: ");
        String direccion = leerTexto("Direccion: ");
        clienteDAO.crear(new Cliente(0, nombre, telefono, direccion));

        // El id lo asigna la base de datos; el cliente recien creado es el ultimo.
        List<Cliente> clientes = clienteDAO.listar();
        Cliente cliente = clientes.get(clientes.size() - 1);
        System.out.println("Cliente registrado con id " + cliente.getId() + ".");
        return cliente;
    }

    private static void crearFactura(Usuario usuario) throws SQLException, AsientosInvalidosException {
        System.out.println("\n--- Crear factura ---");

        Cliente cliente = elegirCliente();
        if (cliente == null) {
            return;
        }
        Ruta ruta = elegirRuta();
        if (ruta == null) {
            return;
        }

        System.out.println("\nTipo de asiento:");
        System.out.println("1. Normal");
        System.out.println("2. Ventanilla (+Q40.00)");
        System.out.println("3. VIP (+Q100.00)");
        int tipo = leerEntero("Elija el tipo: ", 1, 3);
        Asiento asiento;
        switch (tipo) {
            case 2:
                asiento = new AsientoVentanilla();
                break;
            case 3:
                asiento = new AsientoVIP();
                break;
            default:
                asiento = new AsientoNormal();
        }

        int cantidadAsientos = leerEntero("Cantidad de asientos (1 a 3): ", 1, 3);
        int cantidadEquipaje = leerEntero("Cantidad de piezas de equipaje (0 o mas): ", 0, 100);

        Factura factura = new Factura(cliente, ruta, usuario, asiento, cantidadAsientos, cantidadEquipaje);
        facturaDAO.crear(factura);
        System.out.println("\nFactura guardada con id " + factura.getId() + ".\n");
        System.out.println(factura.generarTexto());
    }

    private static void listarFacturas() throws SQLException {
        System.out.println("\n--- Facturas ---");
        List<Factura> facturas = facturaDAO.listar();
        if (facturas.isEmpty()) {
            System.out.println("Todavia no hay facturas registradas.");
            return;
        }
        for (Factura f : facturas) {
            System.out.printf("#%d | %s %s | %s | %s -> %s | %s x%d | Total: Q%.2f%n",
                    f.getId(), f.getFecha(), f.getHora(), f.getCliente().getNombre(),
                    f.getRuta().getEstacionSalida(), f.getRuta().getDestino(),
                    f.getAsiento().getTipo(), f.getCantidadAsientos(), f.calcularTotal());
        }
        System.out.println("\nTotal de facturas: " + facturas.size());
    }

    private static void buscarFactura() throws SQLException {
        System.out.println("\n--- Buscar factura ---");
        int id = leerEntero("Id de la factura: ", 1, Integer.MAX_VALUE);
        Factura factura = facturaDAO.buscarPorId(id);
        if (factura == null) {
            System.out.println("No existe una factura con id " + id + ".");
        } else {
            System.out.println("\n" + factura.generarTexto());
        }
    }

    // ------------------------------------------------- Seleccion de datos

    private static Cliente elegirCliente() throws SQLException {
        System.out.println("1. Cliente existente");
        System.out.println("2. Cliente nuevo");
        System.out.println("0. Cancelar");
        int opcion = leerEntero("Elija una opcion: ", 0, 2);
        if (opcion == 0) {
            return null;
        }
        if (opcion == 2) {
            return registrarCliente();
        }

        List<Cliente> clientes = clienteDAO.listar();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados. Se registrara uno nuevo.");
            return registrarCliente();
        }
        System.out.println("\nClientes:");
        for (Cliente c : clientes) {
            System.out.println(c.getId() + ". " + c.getNombre() + " - " + c.getTelefono());
        }
        while (true) {
            int id = leerEntero("Id del cliente (0 para cancelar): ", 0, Integer.MAX_VALUE);
            if (id == 0) {
                return null;
            }
            Cliente cliente = clienteDAO.buscarPorId(id);
            if (cliente != null) {
                return cliente;
            }
            System.out.println("No existe un cliente con id " + id + ".");
        }
    }

    private static Ruta elegirRuta() throws SQLException {
        List<Ruta> rutas = rutaDAO.listar();
        if (rutas.isEmpty()) {
            System.out.println("No hay rutas registradas.");
            return null;
        }
        System.out.println("\nRutas:");
        for (Ruta r : rutas) {
            System.out.println(r.getId() + ". " + r);
        }
        while (true) {
            int id = leerEntero("Id de la ruta (0 para cancelar): ", 0, Integer.MAX_VALUE);
            if (id == 0) {
                return null;
            }
            for (Ruta r : rutas) {
                if (r.getId() == id) {
                    return r;
                }
            }
            System.out.println("No existe una ruta con id " + id + ".");
        }
    }

    // ------------------------------------------------------ Lectura segura

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }

    private static String leerTextoNoVacio(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje);
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este dato es obligatorio.");
        }
    }

    private static int leerEntero(String mensaje, int minimo, int maximo) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= minimo && valor <= maximo) {
                    return valor;
                }
                System.out.println("El numero debe estar entre " + minimo + " y "
                        + (maximo == Integer.MAX_VALUE ? "el maximo permitido" : String.valueOf(maximo)) + ".");
            } catch (NumberFormatException e) {
                System.out.println("Entrada invalida. Escriba un numero entero.");
            }
        }
    }
}
