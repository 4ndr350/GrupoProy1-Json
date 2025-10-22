package Reportes;

import ZonasMaster.Evento;

/**
 * Totales de la tiquetera para un evento especifico.
 * Ingresos brutos = cargos de servicio + costos de emision.
 */
public class TotalPorEvento {
    private final Evento evento;
    private int boletosVendidos;
    private double ingresosBrutos;

    public TotalPorEvento(Evento evento) {
        this.evento = evento;
        this.boletosVendidos = 0;
        this.ingresosBrutos = 0.0;
    }

    public TotalPorEvento(Evento evento, int boletosVendidos, double ingresosBrutos) {
        this.evento = evento;
        this.boletosVendidos = boletosVendidos;
        this.ingresosBrutos = ingresosBrutos;
    }

    public void agregarVenta(int cantidadBoletos, double ingreso) {
        this.boletosVendidos += cantidadBoletos;
        this.ingresosBrutos += ingreso;
    }

    public Evento getEvento() { return evento; }
    public int getBoletosVendidos() { return boletosVendidos; }
    public void setBoletosVendidos(int boletosVendidos) { this.boletosVendidos = boletosVendidos; }
    public double getIngresosBrutos() { return ingresosBrutos; }
    public void setIngresosBrutos(double ingresosBrutos) { this.ingresosBrutos = ingresosBrutos; }

    @Override
    public String toString() {
        return "TotalPorEvento{" +
                "evento=" + (evento != null ? evento.getNombre() : "-") +
                ", boletosVendidos=" + boletosVendidos +
                ", ingresosBrutos=$" + String.format("%.2f", ingresosBrutos) +
                '}';
    }
}
