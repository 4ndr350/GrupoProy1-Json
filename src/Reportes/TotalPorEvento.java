package Reportes;

import ZonasMaster.Evento;

/**
 * Representa los totales de ventas y ganancias para un evento específico
 * Usado en ReporteTiquetera
 */
public class TotalPorEvento {
    private Evento evento;
    private int boletosVendidos;
    private double ingresosBrutos;      // Ganancias de la tiquetera (cargos + emisión)

    /**
     * Constructor
     */
    public TotalPorEvento(Evento evento) {
        this.evento = evento;
        this.boletosVendidos = 0;
        this.ingresosBrutos = 0.0;
    }

    /**
     * Constructor con valores iniciales
     */
    public TotalPorEvento(Evento evento, int boletosVendidos, double ingresosBrutos) {
        this.evento = evento;
        this.boletosVendidos = boletosVendidos;
        this.ingresosBrutos = ingresosBrutos;
    }

    /**
     * Incrementa los totales
     */
    public void agregarVenta(int cantidadBoletos, double ingreso) {
        this.boletosVendidos += cantidadBoletos;
        this.ingresosBrutos += ingreso;
    }

    // Getters y Setters
    public Evento getEvento() {
        return evento;
    }

    public int getBoletosVendidos() {
        return boletosVendidos;
    }

    public void setBoletosVendidos(int boletosVendidos) {
        this.boletosVendidos = boletosVendidos;
    }

    public double getIngresosBrutos() {
        return ingresosBrutos;
    }

    public void setIngresosBrutos(double ingresosBrutos) {
        this.ingresosBrutos = ingresosBrutos;
    }

    @Override
    public String toString() {
        return "TotalPorEvento{" +
                "evento=" + evento.getNombre() +
                ", boletosVendidos=" + boletosVendidos +
                ", ingresosBrutos=$" + String.format("%.2f", ingresosBrutos) +
                '}';
    }
}