package PaquetesTiquetes;

import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa un producto de un solo tiquete (evento/localidad/asiento opcional).
 */
public class TiqueteSimpleProducto implements ProductoVenta {
    private final Evento evento;
    private final Localidad localidad;
    private final String asiento; // opcional
    private final double precioBase;
    private boolean transferible = true;

    public TiqueteSimpleProducto(Evento evento, Localidad localidad, String asiento, double precioBase) {
        this.evento = evento;
        this.localidad = localidad;
        this.asiento = asiento;
        this.precioBase = precioBase;
    }

    @Override
    public int getCantidadTiquetesTotales() { return 1; }

    @Override
    public double getPrecioBaseTotal() { return precioBase; }

    @Override
    public boolean esTransferible() { return transferible; }

    public void setTransferible(boolean transferible) { this.transferible = transferible; }

    @Override
    public List<Tiquete> generarTiquetes() {
        List<Tiquete> out = new ArrayList<>();
        Date fecha = Date.from(evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
        String hora = evento.getFechaHora().toLocalTime().toString();
        String id = evento.getId() + "-SIMPLE-" + localidad.getId() + "-" + System.currentTimeMillis();
        Tiquete t = new Tiquete(id, asiento, localidad.getNombre(), fecha, hora);
        out.add(t);
        return out;
    }
}

