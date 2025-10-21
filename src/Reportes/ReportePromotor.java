package Reportes;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import java.util.HashMap;
import java.util.Map;

/**
 * Reporte de ventas para un Organizador
 * Muestra estadísticas de sus eventos (sin incluir cargos de servicio)
 */
public class ReportePromotor {
    private Evento evento;                              // Evento específico (null = todos)
    private Map<Localidad, Integer> totalVendidos;      // Vendidos por localidad
    private Map<Localidad, Integer> capacidadTotal;     // Capacidad por localidad
    private double totalIngresosBrutos;                 // Solo precio base (sin cargos)

    /**
     * Constructor para reporte de un evento específico
     */
    public ReportePromotor(Evento evento) {
        this.evento = evento;
        this.totalVendidos = new HashMap<>();
        this.capacidadTotal = new HashMap<>();
        this.totalIngresosBrutos = 0.0;
        inicializarLocalidades();
    }

    /**
     * Constructor para reporte global (todos los eventos)
     */
    public ReportePromotor() {
        this.evento = null;
        this.totalVendidos = new HashMap<>();
        this.capacidadTotal = new HashMap<>();
        this.totalIngresosBrutos = 0.0;
    }

    /**
     * Inicializa las localidades del evento
     */
    private void inicializarLocalidades() {
        if (evento != null) {
            for (Localidad loc : evento.getLocalidades()) {
                totalVendidos.put(loc, 0);
                capacidadTotal.put(loc, loc.getCapacidad());
            }
        }
    }

    /**
     * Registra una venta
     */
    public void registrarVenta(Localidad localidad, int cantidad, double precioBase) {
        totalVendidos.put(localidad, totalVendidos.getOrDefault(localidad, 0) + cantidad);
        totalIngresosBrutos += (precioBase * cantidad);
    }

    /**
     * Calcula el porcentaje de ocupación de una localidad
     */
    public double porcentajeVendido(Localidad localidad) {
        int vendidos = totalVendidos.getOrDefault(localidad, 0);
        int capacidad = capacidadTotal.getOrDefault(localidad, 0);
        
        if (capacidad == 0) return 0.0;
        return (vendidos * 100.0) / capacidad;
    }

    /**
     * Calcula el porcentaje de ocupación total
     */
    public double porcentajeVendidoTotal() {
        int totalVend = totalVendidos.values().stream().mapToInt(Integer::intValue).sum();
        int totalCap = capacidadTotal.values().stream().mapToInt(Integer::intValue).sum();
        
        if (totalCap == 0) return 0.0;
        return (totalVend * 100.0) / totalCap;
    }

    /**
     * Genera un resumen del reporte
     */
    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== REPORTE PROMOTOR ==========\n");
        
        if (evento != null) {
            sb.append("Evento: ").append(evento.getNombre()).append("\n");
            sb.append("Fecha: ").append(evento.getFechaHora()).append("\n\n");
        } else {
            sb.append("REPORTE GLOBAL - Todos los eventos\n\n");
        }
        
        sb.append("--- Ventas por Localidad ---\n");
        for (Map.Entry<Localidad, Integer> entry : totalVendidos.entrySet()) {
            Localidad loc = entry.getKey();
            int vendidos = entry.getValue();
            int capacidad = capacidadTotal.get(loc);
            double porcentaje = porcentajeVendido(loc);
            
            sb.append(String.format("  %s: %d/%d (%.1f%%)\n", 
                loc.getNombre(), vendidos, capacidad, porcentaje));
        }
        
        sb.append("\n--- Totales ---\n");
        sb.append(String.format("Ocupación total: %.1f%%\n", porcentajeVendidoTotal()));
        sb.append(String.format("Ingresos brutos: $%.2f\n", totalIngresosBrutos));
        sb.append("(No incluye cargos de servicio - esos son de la tiquetera)\n");
        sb.append("======================================\n");
        
        return sb.toString();
    }

    // Getters
    public Evento getEvento() {
        return evento;
    }

    public Map<Localidad, Integer> getTotalVendidos() {
        return totalVendidos;
    }

    public Map<Localidad, Integer> getCapacidadTotal() {
        return capacidadTotal;
    }

    public double getTotalIngresosBrutos() {
        return totalIngresosBrutos;
    }

    @Override
    public String toString() {
        return generarResumen();
    }
}