package ZonasMaster;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un evento en el sistema
 * (concierto, evento deportivo, cultural, religioso)
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

    /**
     * Constructor
     */
    public Evento(String id, String nombre, String tipoEvento, LocalDateTime fechaHora, Venue venue) {
        this.id = id;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
        this.fechaHora = fechaHora;
        this.venue = venue;
        this.estado = "PROGRAMADO";
        this.localidades = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.maximoTiquetesPorTransaccion = 10; // Valor por defecto
    }

    /**
     * Establece el máximo de tiquetes por transacción
     */
    public void setMaximoTiquetesPorTransaccion(int max) {
        if (max > 0) {
            this.maximoTiquetesPorTransaccion = max;
        }
    }

    /**
     * Obtiene el número de asientos disponibles en una localidad
     * @param localidad la localidad a consultar
     * @return cantidad de asientos disponibles
     */
    public int asientosDisponibles(Localidad localidad) {
        // TODO: Implementar lógica real
        // Contar tiquetes vendidos en esa localidad y restar de capacidad
        return localidad.getCapacidad();
    }

    /**
     * Verifica si se puede vender una cantidad de tiquetes
     * @param localidad la localidad
     * @param cantidad cantidad solicitada
     * @return true si hay suficientes asientos disponibles
     */
    public boolean puedeVender(Localidad localidad, int cantidad) {
        if (!localidades.contains(localidad)) {
            return false;
        }
        
        // Verificar límite por transacción
        if (cantidad > maximoTiquetesPorTransaccion) {
            System.out.println("Excede máximo por transacción: " + maximoTiquetesPorTransaccion);
            return false;
        }
        
        // Verificar disponibilidad
        return asientosDisponibles(localidad) >= cantidad;
    }

    /**
     * Obtiene el precio vigente de una localidad (con descuento si aplica)
     * @param localidad la localidad
     * @param fecha fecha/hora de la compra
     * @return precio vigente
     */
    public double precioVigente(Localidad localidad, LocalDateTime fecha) {
        // Buscar si hay una oferta activa
        for (Oferta oferta : ofertas) {
            if (oferta.getLocalidad().equals(localidad) && 
                oferta.getVentana().incluye(fecha)) {
                return localidad.precioConDescuento();
            }
        }
        
        // Sin oferta, precio base
        return localidad.getPrecioBase();
    }

    /**
     * Cancela el evento
     */
    public void cancelar() {
        this.estado = "CANCELADO";
        System.out.println("Evento cancelado: " + nombre);
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstado() {
        return estado;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public List<Localidad> getLocalidades() {
        return localidades;
    }

    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public int getMaximoTiquetesPorTransaccion() {
        return maximoTiquetesPorTransaccion;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", tipo='" + tipoEvento + '\'' +
                ", fecha=" + fechaHora +
                ", venue=" + venue.getNombre() +
                ", estado='" + estado + '\'' +
                ", numLocalidades=" + localidades.size() +
                '}';
    }
}

