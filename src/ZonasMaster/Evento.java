package ZonasMaster;

import Actores.Organizador;

import Actores.Organizador;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un evento en el sistema (concierto, deportivo, cultural, religioso)
 * Representa un evento en el sistema (concierto, deportivo, cultural, religioso)
 */
public class Evento {
    private String id;
    private String nombre;
    private String tipoEvento;              // MUSICAL, CULTURAL, DEPORTIVO, RELIGIOSO
    private LocalDateTime fechaHora;
    private String estado;                  // PROGRAMADO, CANCELADO
    private Venue venue;
    private List<Localidad> localidades;
    private List<Oferta> ofertas;
    private int maximoTiquetesPorTransaccion;
    private Organizador organizador;        // due�o del evento

    public Evento(String id, String nombre, String tipoEvento, LocalDateTime fechaHora, Venue venue) {
        this.id = id;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
        this.fechaHora = fechaHora;
        this.venue = venue;
        this.estado = "PROGRAMADO";
        this.localidades = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.maximoTiquetesPorTransaccion = 10; // por defecto
        this.organizador = null;
        this.maximoTiquetesPorTransaccion = 10; // por defecto
        this.organizador = null;
    }

    public void setMaximoTiquetesPorTransaccion(int max) {
        if (max > 0) this.maximoTiquetesPorTransaccion = max;
        if (max > 0) this.maximoTiquetesPorTransaccion = max;
    }

    public int asientosDisponibles(Localidad localidad) {
        // En esta version no se lleva inventario; se usa capacidad de la localidad
        // En esta version no se lleva inventario; se usa capacidad de la localidad
        return localidad.getCapacidad();
    }

    public boolean puedeVender(Localidad localidad, int cantidad) {
        if (!localidades.contains(localidad)) return false;
        if (!localidades.contains(localidad)) return false;
        if (cantidad > maximoTiquetesPorTransaccion) {
            System.out.println("Excede maximo por transaccion: " + maximoTiquetesPorTransaccion);
            System.out.println("Excede maximo por transaccion: " + maximoTiquetesPorTransaccion);
            return false;
        }
        return asientosDisponibles(localidad) >= cantidad;
    }

    public double precioVigente(Localidad localidad, LocalDateTime fecha) {
        for (Oferta oferta : ofertas) {
            if (oferta.getLocalidad().equals(localidad) && oferta.getVentana().incluye(fecha)) {
            if (oferta.getLocalidad().equals(localidad) && oferta.getVentana().incluye(fecha)) {
                return localidad.precioConDescuento();
            }
        }
        return localidad.getPrecioBase();
    }

    public void cancelar() {
        this.estado = "CANCELADO";
        System.out.println("Evento cancelado: " + nombre);
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public List<Localidad> getLocalidades() { return localidades; }
    public List<Oferta> getOfertas() { return ofertas; }
    public int getMaximoTiquetesPorTransaccion() { return maximoTiquetesPorTransaccion; }
    public Organizador getOrganizador() { return organizador; }
    public void setOrganizador(Organizador organizador) { this.organizador = organizador; }
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
    public String getEstado() { return estado; }
    public Venue getVenue() { return venue; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public List<Localidad> getLocalidades() { return localidades; }
    public List<Oferta> getOfertas() { return ofertas; }
    public int getMaximoTiquetesPorTransaccion() { return maximoTiquetesPorTransaccion; }
    public Organizador getOrganizador() { return organizador; }
    public void setOrganizador(Organizador organizador) { this.organizador = organizador; }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipoEvento + '\'' +
                ", fecha=" + fechaHora +
                ", venue=" + (venue != null ? venue.getNombre() : "-") +
                ", venue=" + (venue != null ? venue.getNombre() : "-") +
                ", estado='" + estado + '\'' +
                ", numLocalidades=" + localidades.size() +
                '}';
    }
}



