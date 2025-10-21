package Reportes;

import Actores.Organizador;

/**
 * Representa los totales de ganancias de la tiquetera generadas
 * por eventos de un organizador específico
 * Usado en ReporteTiquetera
 */
public class TotalPorOrganizador {
    private Organizador organizador;
    private int boletosVendidos;
    private double ingresosBrutos;      // Ganancias de la tiquetera de eventos de este organizador

    /**
     * Constructor
     */
    public TotalPorOrganizador(Organizador organizador) {
        this.organizador = organizador;
        this.boletosVendidos = 0;
        this.ingresosBrutos = 0.0;
    }

    /**
     * Constructor con valores iniciales
     */
    public TotalPorOrganizador(Organizador organizador, int boletosVendidos, double ingresosBrutos) {
        this.organizador = organizador;
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
    public Organizador getOrganizador() {
        return organizador;
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
        return "TotalPorOrganizador{" +
                "organizador=" + organizador.getNombre() +
                ", boletosVendidos=" + boletosVendidos +
                ", ingresosBrutos=$" + String.format("%.2f", ingresosBrutos) +
                '}';
    }
}