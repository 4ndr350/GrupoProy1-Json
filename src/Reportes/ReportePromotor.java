package Reportes;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;


import java.util.HashMap;
import java.util.List;
import java.util.List;
import java.util.Map;

/**
 * Reporte de ventas para un Organizador sobre un evento especifico.
 * Muestra ventas por localidad y los ingresos brutos del promotor (precio base).
 * Reporte de ventas para un Organizador sobre un evento especifico.
 * Muestra ventas por localidad y los ingresos brutos del promotor (precio base).
 */
public class ReportePromotor {
    private final Evento evento;
    private final Map<Localidad, Integer> totalVendidos;   // Vendidos por localidad
    private final Map<Localidad, Integer> capacidadTotal;  // Capacidad por localidad
    private double totalIngresosBrutos;                    // Solo precio base (sin cargos)
    private final Evento evento;
    private final Map<Localidad, Integer> totalVendidos;   // Vendidos por localidad
    private final Map<Localidad, Integer> capacidadTotal;  // Capacidad por localidad
    private double totalIngresosBrutos;                    // Solo precio base (sin cargos)

    public ReportePromotor(Evento evento) {
        this.evento = evento;
        this.totalVendidos = new HashMap<>();
        this.capacidadTotal = new HashMap<>();
        this.totalIngresosBrutos = 0.0;
        inicializarLocalidades();
        cargarVentas();
    }

        cargarVentas();
    }

    private void inicializarLocalidades() {
        if (evento != null) {
            for (Localidad loc : evento.getLocalidades()) {
                totalVendidos.put(loc, 0);
                capacidadTotal.put(loc, loc.getCapacidad());
            }
        }
    }

    private void cargarVentas() {
        if (evento == null) return;
        List<RegistroVentas.Venta> ventas = RegistroVentas.listarVentas(evento);
        for (RegistroVentas.Venta v : ventas) {
            // Suma vendidos
            totalVendidos.put(v.localidad, totalVendidos.getOrDefault(v.localidad, 0) + v.cantidad);
            // Suma ingresos brutos del promotor (precio base). Cortesias registradas con base=0.
            totalIngresosBrutos += (v.precioUnitarioBase * v.cantidad);
        }
    }

    private void cargarVentas() {
        if (evento == null) return;
        List<RegistroVentas.Venta> ventas = RegistroVentas.listarVentas(evento);
        for (RegistroVentas.Venta v : ventas) {
            // Suma vendidos
            totalVendidos.put(v.localidad, totalVendidos.getOrDefault(v.localidad, 0) + v.cantidad);
            // Suma ingresos brutos del promotor (precio base). Cortesias registradas con base=0.
            totalIngresosBrutos += (v.precioUnitarioBase * v.cantidad);
        }
    }

    public double porcentajeVendido(Localidad localidad) {
        int vendidos = totalVendidos.getOrDefault(localidad, 0);
        int capacidad = capacidadTotal.getOrDefault(localidad, 0);
        if (capacidad == 0) return 0.0;
        return (vendidos * 100.0) / capacidad;
    }

    public double porcentajeVendidoTotal() {
        int totalVend = totalVendidos.values().stream().mapToInt(Integer::intValue).sum();
        int totalCap = capacidadTotal.values().stream().mapToInt(Integer::intValue).sum();
        if (totalCap == 0) return 0.0;
        return (totalVend * 100.0) / totalCap;
    }

    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== REPORTE PROMOTOR ==========" + System.lineSeparator());
        sb.append("Evento: ").append(evento.getNombre()).append(System.lineSeparator());
        sb.append("Fecha: ").append(evento.getFechaHora()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("--- Ventas por Localidad ---").append(System.lineSeparator());
        sb.append("========== REPORTE PROMOTOR ==========" + System.lineSeparator());
        sb.append("Evento: ").append(evento.getNombre()).append(System.lineSeparator());
        sb.append("Fecha: ").append(evento.getFechaHora()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("--- Ventas por Localidad ---").append(System.lineSeparator());
        for (Map.Entry<Localidad, Integer> entry : totalVendidos.entrySet()) {
            Localidad loc = entry.getKey();
            int vendidos = entry.getValue();
            int capacidad = capacidadTotal.get(loc);
            double porcentaje = porcentajeVendido(loc);
            sb.append(String.format("  %s: %d/%d (%.1f%%)%n", loc.getNombre(), vendidos, capacidad, porcentaje));
            sb.append(String.format("  %s: %d/%d (%.1f%%)%n", loc.getNombre(), vendidos, capacidad, porcentaje));
        }
        sb.append(System.lineSeparator());
        sb.append("--- Totales ---").append(System.lineSeparator());
        sb.append(String.format("Ocupacion total: %.1f%%%n", porcentajeVendidoTotal()));
        sb.append(String.format("Ingresos brutos (promotor): $%.2f%n", totalIngresosBrutos));
        sb.append("(Sin cargos de servicio)" + System.lineSeparator());
        sb.append("======================================" + System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("--- Totales ---").append(System.lineSeparator());
        sb.append(String.format("Ocupacion total: %.1f%%%n", porcentajeVendidoTotal()));
        sb.append(String.format("Ingresos brutos (promotor): $%.2f%n", totalIngresosBrutos));
        sb.append("(Sin cargos de servicio)" + System.lineSeparator());
        sb.append("======================================" + System.lineSeparator());
        return sb.toString();
    }

    // Getters
    public Evento getEvento() { return evento; }
    public Map<Localidad, Integer> getTotalVendidos() { return totalVendidos; }
    public Map<Localidad, Integer> getCapacidadTotal() { return capacidadTotal; }
    public double getTotalIngresosBrutos() { return totalIngresosBrutos; }
    public Evento getEvento() { return evento; }
    public Map<Localidad, Integer> getTotalVendidos() { return totalVendidos; }
    public Map<Localidad, Integer> getCapacidadTotal() { return capacidadTotal; }
    public double getTotalIngresosBrutos() { return totalIngresosBrutos; }

    @Override
    public String toString() { return generarResumen(); }
    public String toString() { return generarResumen(); }
}
