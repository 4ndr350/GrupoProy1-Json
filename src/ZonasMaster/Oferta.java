package ZonasMaster;

/**
 * Representa una oferta de descuento para una localidad especÃ­fica
 * La oferta es vÃ¡lida durante una ventana de tiempo
 */
public class Oferta {
    private String id;
    private double descuentoPct;
    private VentanaTiempo ventana;
    private Localidad localidad;

    /**
     * Constructor
     */
    public Oferta(String id, double descuentoPct, VentanaTiempo ventana, Localidad localidad) {
        this.id = id;
        this.descuentoPct = descuentoPct;
        this.ventana = ventana;
        this.localidad = localidad;
    }

    /**
     * Verifica si la oferta estÃ¡ activa ahora
     * @return true si la ventana de tiempo incluye el momento actual
     */
    public boolean estaActiva() {
        return ventana.estaActiva();
    }

    /**
     * Activa la oferta en la localidad si estÃ¡ en la ventana de tiempo
     * @return true si se activÃ³ exitosamente
     */
    public boolean activarSiVigente() {
        if (estaActiva()) {
            localidad.activarDescuento(descuentoPct);
            return true;
        }
        return false;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public double getDescuentoPct() {
        return descuentoPct;
    }

    public void setDescuentoPct(double descuentoPct) {
        this.descuentoPct = descuentoPct;
    }

    public VentanaTiempo getVentana() {
        return ventana;
    }

    public void setVentana(VentanaTiempo ventana) {
        this.ventana = ventana;
    }

    public Localidad getLocalidad() {
        return localidad;
    }

    @Override
    public String toString() {
        return "Oferta{" +
                "id='" + id + '\'' +
                ", descuento=" + descuentoPct + "%" +
                ", localidad=" + localidad.getNombre() +
                ", activa=" + estaActiva() +
                '}';
    }
}
