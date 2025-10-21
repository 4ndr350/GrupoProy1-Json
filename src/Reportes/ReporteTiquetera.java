package Reportes;

import ZonasMaster.VentanaTiempo;
import java.util.ArrayList;
import java.util.List;

/**
 * Reporte financiero de la tiquetera (para el Administrador)
 * Muestra las ganancias generadas por cargos de servicio y costos de emisión
 */
public class ReporteTiquetera {
    private VentanaTiempo rango;                        // Rango de fechas (null = todo)
    private List<TotalPorEvento> totalesPorEvento;
    private List<TotalPorOrganizador> totalesPorOrganizador;
    private int totalBoletosVendidos;
    private double totalGananciasBrutas;                // Cargos + emisión

    /**
     * Constructor con rango de fechas
     */
    public ReporteTiquetera(VentanaTiempo rango) {
        this.rango = rango;
        this.totalesPorEvento = new ArrayList<>();
        this.totalesPorOrganizador = new ArrayList<>();
        this.totalBoletosVendidos = 0;
        this.totalGananciasBrutas = 0.0;
    }

    /**
     * Constructor sin rango (todos los datos)
     */
    public ReporteTiquetera() {
        this(null);
    }

    /**
     * Agrega un total por evento
     */
    public void agregarTotalEvento(TotalPorEvento total) {
        totalesPorEvento.add(total);
        totalBoletosVendidos += total.getBoletosVendidos();
        totalGananciasBrutas += total.getIngresosBrutos();
    }

    /**
     * Agrega un total por organizador
     */
    public void agregarTotalOrganizador(TotalPorOrganizador total) {
        totalesPorOrganizador.add(total);
    }

    /**
     * Calcula el total de ganancias brutas
     * (suma de todos los ingresos de la tiquetera)
     */
    public double calcularTotalGananciasBrutas() {
        return totalesPorEvento.stream()
                .mapToDouble(TotalPorEvento::getIngresosBrutos)
                .sum();
    }

    /**
     * Genera un resumen del reporte
     */
    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== REPORTE TIQUETERA ==========\n");
        
        if (rango != null) {
            sb.append("Período: ").append(rango.getInicio()).append(" - ").append(rango.getFin()).append("\n");
        } else {
            sb.append("REPORTE GLOBAL - Todos los períodos\n");
        }
        sb.append("\n");
        
        sb.append("--- Resumen General ---\n");
        sb.append(String.format("Total boletos vendidos: %d\n", totalBoletosVendidos));
        sb.append(String.format("Ganancias totales: $%.2f\n", totalGananciasBrutas));
        sb.append("(Incluye cargos de servicio + costos de emisión)\n\n");
        
        if (!totalesPorEvento.isEmpty()) {
            sb.append("--- Totales por Evento ---\n");
            for (TotalPorEvento total : totalesPorEvento) {
                sb.append(String.format("  %s: %d boletos, $%.2f\n",
                    total.getEvento().getNombre(),
                    total.getBoletosVendidos(),
                    total.getIngresosBrutos()));
            }
            sb.append("\n");
        }
        
        if (!totalesPorOrganizador.isEmpty()) {
            sb.append("--- Totales por Organizador ---\n");
            for (TotalPorOrganizador total : totalesPorOrganizador) {
                sb.append(String.format("  %s: %d boletos, $%.2f\n",
                    total.getOrganizador().getNombre(),
                    total.getBoletosVendidos(),
                    total.getIngresosBrutos()));
            }
            sb.append("\n");
        }
        
        sb.append("========================================\n");
        return sb.toString();
    }

    // Getters
    public VentanaTiempo getRango() {
        return rango;
    }

    public List<TotalPorEvento> getTotalesPorEvento() {
        return totalesPorEvento;
    }

    public List<TotalPorOrganizador> getTotalesPorOrganizador() {
        return totalesPorOrganizador;
    }

    public int getTotalBoletosVendidos() {
        return totalBoletosVendidos;
    }

    public double getTotalGananciasBrutas() {
        return totalGananciasBrutas;
    }

    @Override
    public String toString() {
        return generarResumen();
    }
}
