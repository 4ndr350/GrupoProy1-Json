package Calculadora;

/**
 * Núcleo de cálculos de precios/cargos para ventas.
 * - No guarda estado
 * - No realiza I/O ni emite recibos
 */
public final class CalculadoraTiquetes {
    private CalculadoraTiquetes() {}

    /**
     * Calcula el cargo de servicio unitario dado el precio base y el porcentaje.
     * @param precioBaseUnit precio base unitario
     * @param porcentajeCargo porcentaje [0-100]
     * @return cargo unitario (precioBaseUnit * porcentaje/100)
     */
    public static double cargoServicioUnitario(double precioBaseUnit, double porcentajeCargo) {
        if (precioBaseUnit <= 0 || porcentajeCargo <= 0) return 0.0;
        return precioBaseUnit * (porcentajeCargo / 100.0);
    }

    /**
     * Devuelve el costo de emisión unitario (conveniencia: identidad del costo fijo).
     */
    public static double costoEmisionUnitario(double costoFijo) {
        return Math.max(0.0, costoFijo);
    }

    /**
     * Calcula el total de una línea considerando cortesías.
     * Si es cortesía, el total es 0.
     */
    public static double totalLinea(boolean esCortesia,
                                    double precioBaseUnit,
                                    double cargoServicioUnit,
                                    double costoEmisionUnit,
                                    int cantidad) {
        if (esCortesia || cantidad <= 0) return 0.0;
        return (precioBaseUnit + cargoServicioUnit + costoEmisionUnit) * cantidad;
    }
}
