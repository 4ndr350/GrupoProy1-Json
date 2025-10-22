package PaquetesTiquetes;

import java.util.List;

/**
 * Abstracción de un producto vendible en la plataforma.
 * Puede ser un tiquete simple o un paquete de varios tiquetes.
 */
public interface ProductoVenta {
    int getCantidadTiquetesTotales();
    double getPrecioBaseTotal();
    boolean esTransferible();

    /**
     * Genera los tiquetes que componen este producto.
     * Nota: La generación se hace sin validar inventario/aforo.
     */
    List<Tiquete> generarTiquetes();
}

