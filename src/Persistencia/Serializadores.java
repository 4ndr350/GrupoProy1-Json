package Persistencia;

import Actores.Comprador;
import Actores.Usuario;
import Reportes.RegistroVentas;
import ZonasMaster.*;
import PaquetesTiquetes.Tiquete;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Static JSON serializers for domain objects. Output is compact JSON strings.
 */
public final class Serializadores {
    private Serializadores() {}

    public static String usuario(Usuario u) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"id\":").append(JsonUtil.q(u.getId())).append(',');
        sb.append("\"rol\":").append(JsonUtil.q(u.getClass().getSimpleName())).append(',');
        sb.append("\"login\":").append(JsonUtil.q(u.getLogin())).append(',');
        sb.append("\"nombre\":").append(JsonUtil.q(u.getNombre())).append(',');
        sb.append("\"correo\":").append(JsonUtil.q(u.getCorreo())).append(',');
        sb.append("\"saldoVirtual\":").append(String.format(java.util.Locale.US, "%.2f", u.getSaldoVirtual()));
        if (u instanceof Comprador) {
            Comprador c = (Comprador) u;
            List<Tiquete> ts = c.listarMisTiquetes();
            sb.append(',').append("\"misTiquetes\":");
            sb.append('[');
            for (int i = 0; i < ts.size(); i++) {
                if (i > 0) sb.append(',');
                sb.append(tiquete(ts.get(i)));
            }
            sb.append(']');
        }
        sb.append('}');
        return sb.toString();
    }

    public static String tiquete(Tiquete t) {
        StringBuilder sb = new StringBuilder();
        sb.append('{')
          .append("\"idTiquete\":").append(JsonUtil.q(t.idTiquete)).append(',')
          .append("\"asiento\":").append(t.asiento == null ? "null" : JsonUtil.q(t.asiento)).append(',')
          .append("\"tipo\":").append(t.tipo == null ? "null" : JsonUtil.q(t.tipo)).append(',')
          .append("\"fecha\":").append(JsonUtil.fmt(t.fecha)).append(',')
          .append("\"hora\":").append(JsonUtil.q(t.hora))
          .append('}');
        return sb.toString();
    }

    public static String venue(Venue v) {
        StringBuilder sb = new StringBuilder();
        sb.append('{')
          .append("\"id\":").append(JsonUtil.q(v.getId())).append(',')
          .append("\"nombre\":").append(JsonUtil.q(v.getNombre())).append(',')
          .append("\"ubicacion\":").append(JsonUtil.q(v.getUbicacion())).append(',')
          .append("\"capacidadMaxima\":").append(v.getCapacidadMaxima()).append(',')
          .append("\"restriccionesUso\":").append(JsonUtil.q(v.getRestriccionesUso())).append(',')
          .append("\"aprobado\":").append(v.isAprobado())
          .append('}');
        return sb.toString();
    }

    public static String localidad(Localidad l) {
        StringBuilder sb = new StringBuilder();
        sb.append('{')
          .append("\"id\":").append(JsonUtil.q(l.getId())).append(',')
          .append("\"nombre\":").append(JsonUtil.q(l.getNombre())).append(',')
          .append("\"numerada\":").append(l.isNumerada()).append(',')
          .append("\"precioBase\":").append(String.format(java.util.Locale.US, "%.2f", l.getPrecioBase())).append(',')
          .append("\"capacidad\":").append(l.getCapacidad()).append(',')
          .append("\"descuentoPct\":").append(String.format(java.util.Locale.US, "%.2f", l.getDescuentoPct())).append(',')
          .append("\"descuentoActivo\":").append(l.isDescuentoActivo())
          .append('}');
        return sb.toString();
    }

    public static String ventana(VentanaTiempo v) {
        StringBuilder sb = new StringBuilder();
        sb.append('{')
          .append("\"inicio\":").append(JsonUtil.fmt(v.getInicio())).append(',')
          .append("\"fin\":").append(JsonUtil.fmt(v.getFin()))
          .append('}');
        return sb.toString();
    }

    public static String oferta(Oferta o) {
        StringBuilder sb = new StringBuilder();
        sb.append('{')
          .append("\"id\":").append(JsonUtil.q(o.getId())).append(',')
          .append("\"descuentoPct\":").append(String.format(java.util.Locale.US, "%.2f", o.getDescuentoPct())).append(',')
          .append("\"ventana\":").append(ventana(o.getVentana())).append(',')
          .append("\"localidadId\":").append(JsonUtil.q(o.getLocalidad().getId()))
          .append('}');
        return sb.toString();
    }

    public static String evento(Evento e) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"id\":").append(JsonUtil.q(e.getId())).append(',');
        sb.append("\"nombre\":").append(JsonUtil.q(e.getNombre())).append(',');
        sb.append("\"tipoEvento\":").append(JsonUtil.q(e.getTipoEvento())).append(',');
        sb.append("\"fechaHora\":").append(JsonUtil.fmt(e.getFechaHora())).append(',');
        sb.append("\"estado\":").append(JsonUtil.q(e.getEstado())).append(',');
        if (e.getVenue() != null) {
            sb.append("\"venue\":").append(venue(e.getVenue())).append(',');
        } else {
            sb.append("\"venue\":null,");
        }

        // Localidades
        List<Localidad> locs = e.getLocalidades();
        sb.append("\"localidades\":");
        sb.append('[');
        for (int i = 0; i < locs.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(localidad(locs.get(i)));
        }
        sb.append(']').append(',');

        // Ofertas
        sb.append("\"ofertas\":");
        sb.append('[');
        for (int i = 0; i < e.getOfertas().size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(oferta(e.getOfertas().get(i)));
        }
        sb.append(']').append(',');

        sb.append("\"maximoTiquetesPorTransaccion\":").append(e.getMaximoTiquetesPorTransaccion()).append(',');
        sb.append("\"organizadorId\":").append(e.getOrganizador() != null ? JsonUtil.q(e.getOrganizador().getId()) : "null");
        sb.append('}');
        return sb.toString();
    }

    public static String venta(RegistroVentas.Venta v) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"eventoId\":").append(v.evento != null ? JsonUtil.q(v.evento.getId()) : "null").append(',');
        sb.append("\"organizadorId\":").append(v.organizador != null ? JsonUtil.q(v.organizador.getId()) : "null").append(',');
        sb.append("\"localidadId\":").append(v.localidad != null ? JsonUtil.q(v.localidad.getId()) : "null").append(',');
        sb.append("\"cantidad\":").append(v.cantidad).append(',');
        sb.append("\"precioUnitarioBase\":").append(String.format(java.util.Locale.US, "%.2f", v.precioUnitarioBase)).append(',');
        sb.append("\"cargoServicioUnitario\":").append(String.format(java.util.Locale.US, "%.2f", v.cargoServicioUnitario)).append(',');
        sb.append("\"costoEmisionUnitario\":").append(String.format(java.util.Locale.US, "%.2f", v.costoEmisionUnitario)).append(',');
        sb.append("\"fechaVenta\":").append(JsonUtil.fmt(v.fechaVenta)).append(',');
        sb.append("\"esCortesia\":").append(v.esCortesia);
        sb.append('}');
        return sb.toString();
    }

    // Helpers to join arrays
    public static String jsonArrayUsuarios(List<Usuario> us) {
        return '[' + join(us.stream().map(Serializadores::usuario).collect(Collectors.toList())) + ']';
    }

    public static String jsonArrayEventos(List<Evento> evs) {
        return '[' + join(evs.stream().map(Serializadores::evento).collect(Collectors.toList())) + ']';
    }

    public static String jsonArrayVentas(List<RegistroVentas.Venta> vs) {
        return '[' + join(vs.stream().map(Serializadores::venta).collect(Collectors.toList())) + ']';
    }

    private static String join(List<String> parts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.size(); i++) {
            if (i > 0) sb.append(',');
            sb.append(parts.get(i));
        }
        return sb.toString();
    }
}
