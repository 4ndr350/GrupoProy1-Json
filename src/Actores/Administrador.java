package Actores;

import ZonasMaster.Evento;
import ZonasMaster.Venue;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el ROL de Administrador del sistema
 * Gestiona cargos, aprueba venues, cancela eventos y consulta finanzas
 * El administrador NO puede comprar tiquetes
 */
public class Administrador extends Usuario {
    private List<CargoServicio> reglasCargo;
    private double costoFijoEmision;

    /**
     * Constructor de Administrador
     */
    public Administrador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.reglasCargo = new ArrayList<>();
        this.costoFijoEmision = 0.0;
    }

    /**
     * Define o actualiza el cargo por servicio para un tipo de evento.
     * Normaliza el tipo a MAYUSCULAS para consistencia.
     */
    public void fijarCargoServicio(String tipo, double porcentaje) {
        if (tipo == null || tipo.trim().isEmpty()) {
            System.out.println("Tipo de evento inválido");
            return;
        }
        String tipoNorm = tipo.trim().toUpperCase();

        for (CargoServicio regla : reglasCargo) {
            if (regla.getTipoEvento().equalsIgnoreCase(tipoNorm)) {
                regla.setPorcentaje(porcentaje);
                System.out.println("Cargo actualizado: " + tipoNorm + " = " + porcentaje + "%");
                return;
            }
        }

        CargoServicio nueva = new CargoServicio(tipoNorm, porcentaje);
        reglasCargo.add(nueva);
        System.out.println("Nuevo cargo creado: " + tipoNorm + " = " + porcentaje + "%");
    }

    /**
     * Define el costo fijo de emision/impresion por tiquete.
     */
    public void fijarCostoFijoEmision(double monto) {
        if (monto < 0) {
            System.out.println("El costo fijo de emisión no puede ser negativo");
            return;
        }
        this.costoFijoEmision = monto;
        System.out.println("Costo de emisión actualizado: $" + monto);
    }

    /**
     * Aprueba un venue sugerido por un organizador si cumple validaciones básicas.
     */
    public void aprobarVenue(Venue venue) {
        if (venue == null) {
            System.out.println("Venue inválido");
            return;
        }
        if (venue.getNombre() == null || venue.getNombre().trim().isEmpty()) {
            System.out.println("El venue debe tener nombre");
            return;
        }
        if (venue.getCapacidadMaxima() <= 0) {
            System.out.println("Capacidad máxima inválida para el venue");
            return;
        }
        venue.aprobar();
    }

    /**
     * Cancela un evento por fraude.
     * Política: reembolsa precio pagado menos costo de emision.
     * Nota: sin inventario/ventas en el modelo actual, solo registramos la cancelación.
     */
    public void cancelarEventoPorFraude(Evento evento) {
        if (evento == null) {
            System.out.println("Evento inválido");
            return;
        }
        evento.cancelar();
        System.out.println("Reembolso a compradores: precio - costo de emisión ($" + costoFijoEmision + ")");
    }

    /**
     * Autoriza cancelación solicitada por organizador.
     * Política: reembolsa solo precio base (tiquetera retiene cargos + emision).
     */
    public void autorizarCancelacionSolicitada(Evento evento) {
        if (evento == null) {
            System.out.println("Evento inválido");
            return;
        }
        evento.cancelar();
        System.out.println("Reembolso a compradores: solo precio base (tiquetera retiene cargos+emisión)");
    }

    /**
     * Obtiene el porcentaje de cargo para un tipo de evento.
     */
    public double obtenerCargoServicio(String tipoEvento) {
        if (tipoEvento == null) return 0.0;
        for (CargoServicio regla : reglasCargo) {
            if (regla.getTipoEvento().equalsIgnoreCase(tipoEvento)) {
                return regla.getPorcentaje();
            }
        }
        return 0.0;
    }

    /**
     * Calcula el cargo de servicio para un monto base dado un tipo de evento.
     */
    public double calcularCargoServicio(String tipoEvento, double montoBase) {
        double pct = obtenerCargoServicio(tipoEvento);
        return montoBase * (pct / 100.0);
    }

    // Getters
    public List<CargoServicio> getReglasCargo() {
        return new ArrayList<>(reglasCargo);
    }

    public double getCostoFijoEmision() {
        return costoFijoEmision;
    }

    @Override
    public String toString() {
        return "Administrador{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", costoEmision=$" + costoFijoEmision +
                ", numReglas=" + reglasCargo.size() +
                '}';
    }
}
