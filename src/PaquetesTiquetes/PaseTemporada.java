package PaquetesTiquetes;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Paquete que incluye entradas a varios eventos (pase de temporada).
 */
public class PaseTemporada extends Paquete {
    public static class Entrada {
        public final Evento evento;
        public final Localidad localidad;
        public Entrada(Evento evento, Localidad localidad) {
            this.evento = evento; this.localidad = localidad;
        }
    }

    private final List<Entrada> entradas;

    public PaseTemporada(double precioPaq) {
        super();
        this.precioPaq = precioPaq;
        this.entradas = new ArrayList<>();
    }

    public void agregarEntrada(Evento evento, Localidad localidad) {
        if (evento != null && localidad != null) entradas.add(new Entrada(evento, localidad));
    }

    public List<Entrada> getEntradas() { return new ArrayList<>(entradas); }

    @Override
    public int getCantidadTiquetesTotales() {
        return entradas.size();
    }

    @Override
    public List<Tiquete> generarTiquetes() {
        List<Tiquete> out = new ArrayList<>();
        long base = System.currentTimeMillis();
        for (Entrada e : entradas) {
            Date fecha = Date.from(e.evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            String hora = e.evento.getFechaHora().toLocalTime().toString();
            String id = e.evento.getId() + "-TEMP-" + e.localidad.getId() + "-" + (base++);
            Tiquete t = new Tiquete(id, e.localidad.getNombre(), fecha, hora);
            t.tipo = "TEMPORADA";
            out.add(t);
        }
        return out;
    }
}
