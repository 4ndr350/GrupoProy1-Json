package Reportes;

import Actores.Organizador;

/**
 * Totales de la tiquetera para un organizador especifico
 * (suma de ingresos brutos de todos sus eventos en el periodo).
 * Totales de la tiquetera para un organizador especifico
 * (suma de ingresos brutos de todos sus eventos en el periodo).
 */
public class TotalPorOrganizador {
    private final Organizador organizador;
    private final Organizador organizador;
    private int boletosVendidos;
    private double ingresosBrutos;
    private double ingresosBrutos;

    public TotalPorOrganizador(Organizador organizador) {
        this.organizador = organizador;
        this.boletosVendidos = 0;
        this.ingresosBrutos = 0.0;
    }

    public TotalPorOrganizador(Organizador organizador, int boletosVendidos, double ingresosBrutos) {
        this.organizador = organizador;
        this.boletosVendidos = boletosVendidos;
        this.ingresosBrutos = ingresosBrutos;
    }

    public void agregarVenta(int cantidadBoletos, double ingreso) {
        this.boletosVendidos += cantidadBoletos;
        this.ingresosBrutos += ingreso;
    }

    public Organizador getOrganizador() { return organizador; }
    public int getBoletosVendidos() { return boletosVendidos; }
    public void setBoletosVendidos(int boletosVendidos) { this.boletosVendidos = boletosVendidos; }
    public double getIngresosBrutos() { return ingresosBrutos; }
    public void setIngresosBrutos(double ingresosBrutos) { this.ingresosBrutos = ingresosBrutos; }
    public Organizador getOrganizador() { return organizador; }
    public int getBoletosVendidos() { return boletosVendidos; }
    public void setBoletosVendidos(int boletosVendidos) { this.boletosVendidos = boletosVendidos; }
    public double getIngresosBrutos() { return ingresosBrutos; }
    public void setIngresosBrutos(double ingresosBrutos) { this.ingresosBrutos = ingresosBrutos; }

    @Override
    public String toString() {
        return "TotalPorOrganizador{" +
                "organizador=" + (organizador != null ? organizador.getNombre() : "-") +
                "organizador=" + (organizador != null ? organizador.getNombre() : "-") +
                ", boletosVendidos=" + boletosVendidos +
                ", ingresosBrutos=$" + String.format("%.2f", ingresosBrutos) +
                '}';
    }
}
