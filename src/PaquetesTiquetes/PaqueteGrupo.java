package PaquetesTiquetes;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Paquete de entradas múltiples para un mismo evento/localidad (ej: palco para N personas).
 */
public class PaqueteGrupo extends Paquete {
    private final Evento evento;
    private final Localidad localidad;
    private final int cantidadEntradas;

    public PaqueteGrupo(Evento evento, Localidad localidad, int cantidadEntradas, double precioPaq) {
        super();
        this.evento = evento;
        this.localidad = localidad;
        this.cantidadEntradas = Math.max(1, cantidadEntradas);
        this.precioPaq = precioPaq;
        this.transferible = true; // transferable individualmente o como paquete (reglas de negocio se aplican fuera)
    }

    @Override
    public int getCantidadTiquetesTotales() {
        return cantidadEntradas;
    }

    @Override
    public List<Tiquete> generarTiquetes() {
        List<Tiquete> out = new ArrayList<>();
        Date fecha = Date.from(evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
        String hora = evento.getFechaHora().toLocalTime().toString();
        long base = System.currentTimeMillis();
        for (int i = 0; i < cantidadEntradas; i++) {
            String id = evento.getId() + "-GRP-" + localidad.getId() + "-" + (base++) + "-" + i;
            Tiquete t = new Tiquete(id, localidad.getNombre(), fecha, hora);
            out.add(t);
        }
        return out;
    }
}

