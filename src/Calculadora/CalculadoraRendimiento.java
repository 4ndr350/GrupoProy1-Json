package Calculadora;

import Reportes.RegistroVentas;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Cálculos agregados de rendimiento sin estado.
 * Opera sobre colecciones provistas por los consumidores.
 */
public final class CalculadoraRendimiento {
    private CalculadoraRendimiento() {}

    /** Ingresos del promotor (precio base) para un evento. */
    public static double ingresosPromotorEvento(Evento evento, List<RegistroVentas.Venta> ventas) {
        if (evento == null || ventas == null) return 0.0;
        double total = 0.0;
        for (RegistroVentas.Venta v : ventas) {
            if (evento.equals(v.evento)) {
                total += v.precioUnitarioBase * v.cantidad;
            }
        }
        return total;
    }

    /** Ingresos de la tiquetera (cargos + emisión) para un evento. */
    public static double ingresosTiqueteraEvento(Evento evento, List<RegistroVentas.Venta> ventas) {
        if (evento == null || ventas == null) return 0.0;
        double total = 0.0;
        for (RegistroVentas.Venta v : ventas) {
            if (evento.equals(v.evento)) {
                total += (v.cargoServicioUnitario + v.costoEmisionUnitario) * v.cantidad;
            }
        }
        return total;
    }

    /** Cantidades vendidas por localidad para un evento. */
    public static Map<Localidad, Integer> vendidosPorLocalidad(Evento evento, List<RegistroVentas.Venta> ventas) {
        Map<Localidad, Integer> res = new HashMap<>();
        if (evento == null || ventas == null) return res;
        for (RegistroVentas.Venta v : ventas) {
            if (evento.equals(v.evento)) {
                res.put(v.localidad, res.getOrDefault(v.localidad, 0) + v.cantidad);
            }
        }
        return res;
    }
}