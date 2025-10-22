package Calculadora;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;

/**
 * Validaciones de cupos y límites de venta.
 * No guarda estado, solo lógica condicional reutilizable.
 */
public final class CalculadoraValidaciones {
    private CalculadoraValidaciones() {}

    /** Verifica si la localidad pertenece al evento. */
    public static boolean localidadPertenece(Evento evento, Localidad localidad) {
        return evento != null && localidad != null && evento.getLocalidades().contains(localidad);
    }

    /** Verifica límite máximo por transacción a nivel de evento. */
    public static boolean cumpleMaximoPorTransaccion(Evento evento, int totalSolicitado) {
        if (evento == null || totalSolicitado <= 0) return false;
        return totalSolicitado <= evento.getMaximoTiquetesPorTransaccion();
    }

    /** Verifica disponibilidad (usa la capacidad modelo actual). */
    public static boolean hayDisponibilidad(Evento evento, Localidad localidad, int cantidad) {
        if (evento == null || localidad == null || cantidad <= 0) return false;
        return evento.asientosDisponibles(localidad) >= cantidad;
    }
}
