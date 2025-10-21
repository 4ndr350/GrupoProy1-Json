package Actores;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import ZonasMaster.VentanaTiempoOferta;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa el ROL de Organizador de eventos
 * Puede crear eventos, configurar localidades y ver reportes de ventas
 */
public class Organizador extends Usuario {
    // Atributos adicionales
    private List<Evento> misEventos;

    /**
     * Constructor de Organizador
     */
    public Organizador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.misEventos = new ArrayList<>();
    }

    /**
     * Crea un nuevo evento
     * @param evento el Evento a crear
     */
    public void crearEvento(Evento evento) {
        if (evento == null) {
            System.out.println("El evento no puede ser null");
            return;
        }

        // TODO: Validar que el Venue esté aprobado y disponible
        
        misEventos.add(evento);
        System.out.println("Evento creado: " + evento.getNombre());
    }

    /**
     * Configura las localidades para un evento
     * @param evento el evento a configurar
     * @param localidades lista de localidades
     */
    public void configurarLocalidades(Evento evento, List<Localidad> localidades) {
        if (!misEventos.contains(evento)) {
            System.out.println("Este evento no te pertenece");
            return;
        }

        // TODO: Validar capacidad total vs Venue
        // TODO: Generar tiquetes según configuración

        System.out.println("Configuradas " + localidades.size() + " localidades");
    }

    /**
     * Crea una oferta de descuento
     * @param evento el evento
     * @param localidad la localidad donde aplicar el descuento
     * @param descuentoPct porcentaje de descuento
     * @param ventana ventana de tiempo de la oferta
     */
    public void crearOferta(Evento evento, Localidad localidad, double descuentoPct, 
                           VentanaTiempoOferta ventana) {
        if (!misEventos.contains(evento)) {
            System.out.println("Este evento no te pertenece");
            return;
        }

        // TODO: Crear objeto Oferta y asociarlo al evento

        System.out.println("Oferta creada: " + descuentoPct + "% de descuento");
    }

    /**
     * Consulta el estado de ventas
     * @param evento evento a consultar (null para ver todos)
     * @return ReportePromotor con estadísticas
     */
    public Object verEstadoVentas(Evento evento) {
        // TODO: Generar ReportePromotor
        // - Calcular ventas por localidad
        // - Calcular porcentaje de ocupación
        // - Mostrar ingresos brutos (sin cargos de servicio)

        System.out.println("Generando reporte de ventas...");
        return null; // Placeholder
    }

    /**
     * Obtiene los eventos del organizador
     */
    public List<Evento> getMisEventos() {
        return new ArrayList<>(misEventos);
    }

    @Override
    public String toString() {
        return "Organizador{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", numEventos=" + misEventos.size() +
                '}';
    }
}