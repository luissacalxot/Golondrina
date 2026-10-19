/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 */
import excepciones.AsientosInvalidosException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Factura {

    public static final double PRECIO_POR_EQUIPAJE = 20.00;
    public static final double IVA_GUATEMALA = 12.0;
    public static final String LEYENDA = "Gracias por su preferencia. La leyenda le solicita que conserve su factura hasta el final del viaje.";
    private static final int MAX_ASIENTOS = 3;

    private int id;
    private Cliente cliente;
    private Ruta ruta;
    private Usuario usuario;
    private Asiento asiento;
    private int cantidadAsientos;
    private int cantidadEquipaje;
    private double iva;
    private LocalDate fecha;
    private LocalTime hora;

    public Factura(Cliente cliente, Ruta ruta, Usuario usuario, Asiento asiento,
                   int cantidadAsientos, int cantidadEquipaje) throws AsientosInvalidosException {
        this(0, cliente, ruta, usuario, asiento, cantidadAsientos, cantidadEquipaje,
                IVA_GUATEMALA, LocalDate.now(), LocalTime.now());
    }

    public Factura(int id, Cliente cliente, Ruta ruta, Usuario usuario, Asiento asiento,
                   int cantidadAsientos, int cantidadEquipaje, double iva,
                   LocalDate fecha, LocalTime hora) throws AsientosInvalidosException {
        this.id = id;
        this.cliente = cliente;
        this.ruta = ruta;
        this.usuario = usuario;
        this.asiento = asiento;
        setCantidadAsientos(cantidadAsientos);
        setCantidadEquipaje(cantidadEquipaje);
        this.iva = iva;
        this.fecha = fecha;
        this.hora = hora;
    }

    public int getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Ruta getRuta() { return ruta; }
    public Usuario getUsuario() { return usuario; }
    public Asiento getAsiento() { return asiento; }
    public int getCantidadAsientos() { return cantidadAsientos; }
    public int getCantidadEquipaje() { return cantidadEquipaje; }
    public double getIva() { return iva; }
    public LocalDate getFechaLocal() { return fecha; }
    public LocalTime getHoraLocal() { return hora; }

    public String getFecha() { return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")); }
    public String getHora() { return hora.format(DateTimeFormatter.ofPattern("HH:mm")); }

    public void setId(int id) { this.id = id; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public void setRuta(Ruta ruta) { this.ruta = ruta; }
    public void setAsiento(Asiento asiento) { this.asiento = asiento; }

    public void setCantidadAsientos(int cantidadAsientos) throws AsientosInvalidosException {
        if (cantidadAsientos < 1 || cantidadAsientos > MAX_ASIENTOS) {
            throw new AsientosInvalidosException(
                    "La cantidad de asientos debe estar entre 1 y " + MAX_ASIENTOS + ".");
        }
        this.cantidadAsientos = cantidadAsientos;
    }

    public void setCantidadEquipaje(int cantidadEquipaje) {
        if (cantidadEquipaje >= 0) {
            this.cantidadEquipaje = cantidadEquipaje;
        }
    }

    public double getPrecioPorAsiento() {
        return asiento.calcularPrecio(ruta.getPrecioBase());
    }

    public double getCostoAsientos() {
        return getPrecioPorAsiento() * cantidadAsientos;
    }

    public double getCostoEquipaje() {
        return PRECIO_POR_EQUIPAJE * cantidadEquipaje;
    }

    public double calcularSubtotal() {
        return getCostoAsientos() + getCostoEquipaje();
    }

    public double calcularMontoIva() {
        return calcularSubtotal() * (iva / 100);
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularMontoIva();
    }

    public String generarTexto() {
        StringBuilder sb = new StringBuilder();
        sb.append("============ GOLONDRINAS S.A. ============\n");
        sb.append(String.format("Fecha:      %s%n", getFecha()));
        sb.append(String.format("Hora:       %s%n", getHora()));
        sb.append(String.format("Nombre:     %s%n", cliente.getNombre()));
        sb.append(String.format("Telefono:   %s%n", cliente.getTelefono()));
        sb.append(String.format("Direccion:  %s%n", cliente.getDireccion()));
        sb.append(String.format("Salida:     %s%n", ruta.getEstacionSalida()));
        sb.append(String.format("Destino:    %s%n", ruta.getDestino()));
        sb.append(String.format("Asiento:    %s x%d%n", asiento.getTipo(), cantidadAsientos));
        sb.append(String.format("Equipaje:   %d pieza(s)%n", cantidadEquipaje));
        sb.append("------------------------------------------\n");
        sb.append(String.format("Precio p/asiento: Q%.2f%n", getPrecioPorAsiento()));
        sb.append(String.format("Asientos:         Q%.2f%n", getCostoAsientos()));
        sb.append(String.format("Equipaje:         Q%.2f%n", getCostoEquipaje()));
        sb.append(String.format("Subtotal:         Q%.2f%n", calcularSubtotal()));
        sb.append(String.format("IVA (%.0f%%):        Q%.2f%n", iva, calcularMontoIva()));
        sb.append(String.format("TOTAL:            Q%.2f%n", calcularTotal()));
        sb.append("------------------------------------------\n");
        sb.append(LEYENDA).append("\n");
        sb.append("==========================================\n");
        return sb.toString();
    }
}
