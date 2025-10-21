package ZonasMaster;

/**
 * Representa una sección o zona dentro de un evento
 * (Ej: VIP, Platea, General, Gramilla, etc.)
 */
public class Localidad {
    private String id;
    private String nombre;
    private boolean numerada;           // Si tiene asientos numerados
    private double precioBase;
    private int capacidad;
    private double descuentoPct;        // Descuento activo (%)
    private boolean descuentoActivo;

    /**
     * Constructor
     */
    public Localidad(String id, String nombre, boolean numerada, double precioBase, int capacidad) {
        this.id = id;
        this.nombre = nombre;
        this.numerada = numerada;
        this.precioBase = precioBase;
        this.capacidad = capacidad;
        this.descuentoPct = 0.0;
        this.descuentoActivo = false;
    }

    /**
     * Activa un descuento en esta localidad
     * @param porcentaje porcentaje de descuento (0-100)
     */
    public void activarDescuento(double porcentaje) {
        if (porcentaje >= 0 && porcentaje <= 100) {
            this.descuentoPct = porcentaje;
            this.descuentoActivo = true;
            System.out.println("Descuento activado en " + nombre + ": " + porcentaje + "%");
        }
    }

    /**
     * Desactiva el descuento
     */
    public void desactivarDescuento() {
        this.descuentoPct = 0.0;
        this.descuentoActivo = false;
        System.out.println("Descuento desactivado en " + nombre);
    }

    /**
     * Calcula el precio con descuento si está activo
     * @return precio final considerando descuento
     */
    public double precioConDescuento() {
        if (descuentoActivo) {
            return precioBase * (1 - descuentoPct / 100.0);
        }
        return precioBase;
    }

    // Getters y Setters
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isNumerada() {
        return numerada;
    }

    public void setNumerada(boolean numerada) {
        this.numerada = numerada;
    }

    public double getPrecioBase() {
        return precioBase;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getDescuentoPct() {
        return descuentoPct;
    }

    public boolean isDescuentoActivo() {
        return descuentoActivo;
    }

    @Override
    public String toString() {
        return "Localidad{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", numerada=" + numerada +
                ", precioBase=$" + precioBase +
                ", capacidad=" + capacidad +
                ", descuento=" + (descuentoActivo ? descuentoPct + "%" : "Sin descuento") +
                '}';
    }
}