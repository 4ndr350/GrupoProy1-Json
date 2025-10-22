package Reportes;

import Actores.Organizador;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import ZonasMaster.VentanaTiempo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Registro global en memoria de ventas de tiquetes.
 * Permite que los reportes consulten datos agregados del sistema.
 */
public final class RegistroVentas {
    private static final List<Venta> VENTAS = new ArrayList<>();

    private RegistroVentas() {}

    /**
     * Entrada de venta (linea) con desglose por item.
     */
    public static final class Venta {
        public final Evento evento;
        public final Organizador organizador;
        public final Localidad localidad;
        public final int cantidad;
        public final double precioUnitarioBase;
        public final double cargoServicioUnitario;
        public final double costoEmisionUnitario;
        public final LocalDateTime fechaVenta;
        public final boolean esCortesia;

        public Venta(Evento evento, Organizador organizador, Localidad localidad, int cantidad,
                     double precioUnitarioBase, double cargoServicioUnitario, double costoEmisionUnitario,
                     LocalDateTime fechaVenta, boolean esCortesia) {
            this.evento = evento;
            this.organizador = organizador;
            this.localidad = localidad;
            this.cantidad = cantidad;
            this.precioUnitarioBase = precioUnitarioBase;
            this.cargoServicioUnitario = cargoServicioUnitario;
            this.costoEmisionUnitario = costoEmisionUnitario;
            this.fechaVenta = fechaVenta;
            this.esCortesia = esCortesia;
        }
    }

    /**
     * Registra una venta en el sistema.
     */
    public static void registrarVenta(Evento evento, Organizador organizador, Localidad localidad, int cantidad,
                                      double precioUnitarioBase, double cargoServicioUnitario,
                                      double costoEmisionUnitario, LocalDateTime fechaVenta) {
        registrarVenta(evento, organizador, localidad, cantidad, precioUnitarioBase, cargoServicioUnitario,
                costoEmisionUnitario, fechaVenta, false);
    }

    public static void registrarVenta(Evento evento, Organizador organizador, Localidad localidad, int cantidad,
                                      double precioUnitarioBase, double cargoServicioUnitario,
                                      double costoEmisionUnitario, LocalDateTime fechaVenta, boolean esCortesia) {
        VENTAS.add(new Venta(evento, organizador, localidad, cantidad,
                precioUnitarioBase, cargoServicioUnitario, costoEmisionUnitario, fechaVenta, esCortesia));
    }

    public static List<Venta> listarVentas() {
        return Collections.unmodifiableList(VENTAS);
    }

    public static List<Venta> listarVentas(Evento evento) {
        return VENTAS.stream().filter(v -> v.evento.equals(evento)).collect(Collectors.toList());
    }

    public static List<Venta> listarVentas(Organizador organizador) {
        return VENTAS.stream().filter(v -> v.organizador != null && v.organizador.equals(organizador))
                .collect(Collectors.toList());
    }

    public static List<Venta> listarVentas(VentanaTiempo rango) {
        if (rango == null) return listarVentas();
        return VENTAS.stream().filter(v -> rango.incluye(v.fechaVenta)).collect(Collectors.toList());
    }
}
