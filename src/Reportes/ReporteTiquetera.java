package Reportes;

import Actores.Organizador;
import ZonasMaster.Evento;
import ZonasMaster.VentanaTiempo;

import java.util.*;

/**
 * Reporte financiero de la tiquetera (global).
 * Resume ventas y ganancias brutas (cargos + emision) por evento y por organizador.
 */
public class ReporteTiquetera {
    private final VentanaTiempo rango;                        // Rango de fechas (null = todo)
    private final List<TotalPorEvento> totalesPorEvento;
    private final List<TotalPorOrganizador> totalesPorOrganizador;
    private int totalBoletosVendidos;
    private double totalGananciasBrutas;                      // Cargos + emision

    public ReporteTiquetera(VentanaTiempo rango) {
        this.rango = rango;
        this.totalesPorEvento = new ArrayList<>();
        this.totalesPorOrganizador = new ArrayList<>();
        recalcular();
    }

    public ReporteTiquetera() { this(null); }

    private void recalcular() {
        Map<Evento, TotalPorEvento> mapEvento = new LinkedHashMap<>();
        Map<Organizador, TotalPorOrganizador> mapOrg = new LinkedHashMap<>();
        totalBoletosVendidos = 0;
        totalGananciasBrutas = 0.0;

        for (RegistroVentas.Venta v : RegistroVentas.listarVentas(rango)) {
            double ingresoTiqueteraUnit = v.cargoServicioUnitario + v.costoEmisionUnitario;
            double ingresoTiquetera = ingresoTiqueteraUnit * v.cantidad;

            // Por evento
            mapEvento.computeIfAbsent(v.evento, TotalPorEvento::new)
                     .agregarVenta(v.cantidad, ingresoTiquetera);

            // Por organizador
            Organizador org = (v.organizador != null) ? v.organizador : (v.evento != null ? v.evento.getOrganizador() : null);
            if (org != null) {
                mapOrg.computeIfAbsent(org, TotalPorOrganizador::new)
                     .agregarVenta(v.cantidad, ingresoTiquetera);
            }

            totalBoletosVendidos += v.cantidad;
            totalGananciasBrutas += ingresoTiquetera;
        }

        totalesPorEvento.clear();
        totalesPorEvento.addAll(mapEvento.values());
        totalesPorOrganizador.clear();
        totalesPorOrganizador.addAll(mapOrg.values());
    }

    public double calcularTotalGananciasBrutas() {
        return totalGananciasBrutas;
    }

    public String generarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("========== REPORTE TIQUETERA ==========" + System.lineSeparator());
        if (rango != null) {
            sb.append("Periodo: ").append(rango.getInicio()).append(" - ").append(rango.getFin()).append(System.lineSeparator());
        } else {
            sb.append("REPORTE GLOBAL - Todos los periodos").append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        sb.append("--- Resumen General ---").append(System.lineSeparator());
        sb.append(String.format("Total boletos vendidos: %d%n", totalBoletosVendidos));
        sb.append(String.format("Ganancias totales (tiquetera): $%.2f%n", totalGananciasBrutas));
        sb.append("(Cargos de servicio + costos de emision)" + System.lineSeparator());
        sb.append(System.lineSeparator());

        if (!totalesPorEvento.isEmpty()) {
            sb.append("--- Totales por Evento ---").append(System.lineSeparator());
            for (TotalPorEvento total : totalesPorEvento) {
                sb.append(String.format("  %s: %d boletos, $%.2f%n",
                        total.getEvento().getNombre(), total.getBoletosVendidos(), total.getIngresosBrutos()));
            }
            sb.append(System.lineSeparator());
        }

        if (!totalesPorOrganizador.isEmpty()) {
            sb.append("--- Totales por Organizador ---").append(System.lineSeparator());
            for (TotalPorOrganizador total : totalesPorOrganizador) {
                sb.append(String.format("  %s: %d boletos, $%.2f%n",
                        total.getOrganizador().getNombre(), total.getBoletosVendidos(), total.getIngresosBrutos()));
            }
            sb.append(System.lineSeparator());
        }

        sb.append("========================================" + System.lineSeparator());
        return sb.toString();
    }

    // Getters
    public VentanaTiempo getRango() { return rango; }
    public List<TotalPorEvento> getTotalesPorEvento() { return Collections.unmodifiableList(totalesPorEvento); }
    public List<TotalPorOrganizador> getTotalesPorOrganizador() { return Collections.unmodifiableList(totalesPorOrganizador); }
    public int getTotalBoletosVendidos() { return totalBoletosVendidos; }
    public double getTotalGananciasBrutasAcumuladas() { return totalGananciasBrutas; }

    @Override
    public String toString() { return generarResumen(); }
}

