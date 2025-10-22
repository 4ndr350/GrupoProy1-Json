package Actores;

import Reportes.ReportePromotor;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import ZonasMaster.Oferta;
import ZonasMaster.Venue;
import ZonasMaster.VentanaTiempo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Rol Organizador de eventos
 * - Crea eventos
 * - Configura localidades
 * - Crea ofertas y consulta ventas
 */
public class Organizador extends Usuario {
    private List<Evento> misEventos;

    public Organizador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.misEventos = new ArrayList<>();
    }

    /**
     * Crea un nuevo evento asociado al organizador.
     * Valida que el Venue este aprobado y que no haya otro evento del mismo dia en el mismo Venue
     * entre los eventos de este organizador.
     */
    public void crearEvento(Evento evento) {
        if (evento == null) {
            System.out.println("El evento no puede ser null");
            return;
        }

        Venue venue = evento.getVenue();
        if (venue == null || !venue.isAprobado()) {
            System.out.println("El Venue no esta aprobado o es invalido");
            return;
        }

        if (evento.getFechaHora() == null) {
            System.out.println("La fecha/hora del evento es obligatoria");
            return;
        }
        LocalDate dia = evento.getFechaHora().toLocalDate();

        for (Evento e : misEventos) {
            if (e.getVenue() != null && e.getVenue().getId().equals(venue.getId())) {
                if (e.getFechaHora() != null && e.getFechaHora().toLocalDate().equals(dia)) {
                    System.out.println("Ya existe un evento tuyo en ese Venue en la misma fecha");
                    return;
                }
            }
        }

        // Asociar organizador al evento
        evento.setOrganizador(this);
        misEventos.add(evento);
        System.out.println("Evento creado: " + evento.getNombre());
    }

    /**
     * Configura las localidades del evento. Verifica que:
     * - El evento pertenece al organizador
     * - La suma de capacidades no excede la capacidad del Venue
     */
    public void configurarLocalidades(Evento evento, List<Localidad> localidades) {
        if (evento == null || !misEventos.contains(evento)) {
            System.out.println("Este evento no te pertenece");
            return;
        }
        if (localidades == null || localidades.isEmpty()) {
            System.out.println("Debe proporcionar al menos una localidad");
            return;
        }

        Venue venue = evento.getVenue();
        if (venue == null) {
            System.out.println("El evento no tiene Venue asociado");
            return;
        }

        int sumaCapacidades = localidades.stream().mapToInt(Localidad::getCapacidad).sum();
        if (sumaCapacidades > venue.getCapacidadMaxima()) {
            System.out.println("La capacidad total de localidades excede la capacidad del Venue");
            return;
        }

        // Reemplaza configuracion de localidades
        evento.getLocalidades().clear();
        evento.getLocalidades().addAll(localidades);

        // Nota: La generacion de tiquetes numerados/individuales se modelara en otra capa.
        System.out.println("Configuradas " + localidades.size() + " localidades");
    }

    /**
     * Crea una oferta de descuento para una localidad de un evento de este organizador.
     * Asocia la oferta al evento y activa el descuento si la ventana esta vigente.
     */
    public void crearOferta(Evento evento, Localidad localidad, double descuentoPct, VentanaTiempo ventana) {
        if (evento == null || !misEventos.contains(evento)) {
            System.out.println("Este evento no te pertenece");
            return;
        }
        if (localidad == null || !evento.getLocalidades().contains(localidad)) {
            System.out.println("La localidad no pertenece al evento");
            return;
        }
        if (descuentoPct < 0 || descuentoPct > 100) {
            System.out.println("El descuento debe estar entre 0% y 100%");
            return;
        }
        if (ventana == null) {
            System.out.println("Debe especificar una ventana de tiempo valida");
            return;
        }

        String idOferta = "OF-" + System.currentTimeMillis();
        Oferta oferta = new Oferta(idOferta, descuentoPct, ventana, localidad);
        evento.getOfertas().add(oferta);
        oferta.activarSiVigente();

        System.out.println("Oferta creada: " + descuentoPct + "% de descuento");
    }

    /**
     * Construye un reporte de ventas por evento (estadisticas de localidades e ingresos brutos base).
     */
    public ReportePromotor verEstadoVentas(Evento evento) {
        if (evento == null || !misEventos.contains(evento)) {
            System.out.println("Debe indicar un evento valido del organizador");
            return null;
        }
        ReportePromotor reporte = new ReportePromotor(evento);
        System.out.println("Generando reporte de ventas para: " + evento.getNombre());
        return reporte;
    }

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
