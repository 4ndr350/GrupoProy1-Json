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
    // Atributos específicos
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
     * Define o actualiza el cargo por servicio para un tipo de evento
     * @param tipo tipo de evento (MUSICAL, CULTURAL, DEPORTIVO, RELIGIOSO)
     * @param porcentaje porcentaje de cargo (0-100)
     */
    public void fijarCargoServicio(String tipo, double porcentaje) {
        // Buscar si ya existe una regla para este tipo
        for (CargoServicio regla : reglasCargo) {
            if (regla.getTipoEvento().equalsIgnoreCase(tipo)) {
                regla.setPorcentaje(porcentaje);
                System.out.println("Cargo actualizado: " + tipo + " = " + porcentaje + "%");
                return;
            }
        }
        
        // Si no existe, crear nueva regla
        CargoServicio nueva = new CargoServicio(tipo, porcentaje);
        reglasCargo.add(nueva);
        System.out.println("Nuevo cargo creado: " + tipo + " = " + porcentaje + "%");
    }

    /**
     * Define el costo fijo de emisión/impresión
     * @param monto monto fijo por tiquete
     */
    public void fijarCostoFijoEmision(double monto) {
        if (monto >= 0) {
            this.costoFijoEmision = monto;
            System.out.println("Costo de emisión actualizado: $" + monto);
        }
    }

    /**
     * Aprueba un venue sugerido por un organizador
     * @param venue el Venue a aprobar
     */
    public void aprobarVenue(Venue venue) {
        if (venue == null) {
            System.out.println("Venue inválido");
            return;
        }

        // TODO: Validar requisitos del venue
        // venue.aprobar();
        
        System.out.println("Venue aprobado: " + venue.getNombre());
    }

    /**
     * Cancela un evento por fraude
     * Reembolsa: precio total - costo de emisión
     * @param evento el evento a cancelar
     */
    public void cancelarEventoPorFraude(Evento evento) {
        if (evento == null) {
            System.out.println("Evento inválido");
            return;
        }

        // TODO: Implementar cancelación
        // 1. Marcar evento como CANCELADO
        // 2. Obtener todos los tiquetes vendidos
        // 3. Para cada comprador: acreditar (precioTotal - costoEmision)

        System.out.println("Evento cancelado por fraude: " + evento.getNombre());
        System.out.println("Reembolsando: precio - emisión");
    }

    /**
     * Autoriza cancelación solicitada por organizador
     * Reembolsa: solo precio base (sin cargos ni emisión)
     * @param evento el evento a cancelar
     */
    public void autorizarCancelacionSolicitada(Evento evento) {
        if (evento == null) {
            System.out.println("Evento inválido");
            return;
        }

        // TODO: Implementar autorización
        // 1. Validar solicitud del organizador
        // 2. Marcar evento como CANCELADO
        // 3. Para cada comprador: acreditar solo precio base
        // 4. La tiquetera retiene (cargos + emisión)

        System.out.println("Cancelación autorizada: " + evento.getNombre());
        System.out.println("Reembolsando: solo precio base");
    }

    /**
     * Obtiene el porcentaje de cargo para un tipo de evento
     * @param tipoEvento tipo de evento
     * @return porcentaje de cargo
     */
    public double obtenerCargoServicio(String tipoEvento) {
        for (CargoServicio regla : reglasCargo) {
            if (regla.getTipoEvento().equalsIgnoreCase(tipoEvento)) {
                return regla.getPorcentaje();
            }
        }
        return 0.0;
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
