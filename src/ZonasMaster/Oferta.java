package ZonasMaster;

/**
 * Representa una oferta de descuento para una localidad específica
 * La oferta es válida durante una ventana de tiempo
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
     * Verifica si la oferta está activa ahora
     * @return true si la ventana de tiempo incluye el momento actual
     */
    public boolean estaActiva() {
        return ventana.estaActiva();
    }

    /**
     * Activa la oferta en la localidad si está en la ventana de tiempo
     * @return true si se activó exitosamente
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