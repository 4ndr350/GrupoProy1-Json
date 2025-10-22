package ZonasMaster;

/**
 * Representa un lugar físico donde se realizan eventos
 * (estadio, teatro, bar, discoteca, lote, etc.)
 */
public class Venue {
    private String id;
    private String nombre;
    private String ubicacion;
    private int capacidadMaxima;
    private String restriccionesUso;
    private boolean aprobado;

    /**
     * Constructor
     */
    public Venue(String id, String nombre, String ubicacion, int capacidadMaxima, 
                 String restriccionesUso) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.restriccionesUso = restriccionesUso;
        this.aprobado = false; // Por defecto no está aprobado
    }

    /**
     * Aprueba el venue (llamado por el Administrador)
     */
    public void aprobar() {
        this.aprobado = true;
        System.out.println("Venue aprobado: " + nombre);
    }

    /**
     * Rechaza el venue
     */
    public void rechazar() {
        this.aprobado = false;
        System.out.println("Venue rechazado: " + nombre);
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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public void setCapacidadMaxima(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getRestriccionesUso() {
        return restriccionesUso;
    }

    public void setRestriccionesUso(String restriccionesUso) {
        this.restriccionesUso = restriccionesUso;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", capacidadMaxima=" + capacidadMaxima +
                ", aprobado=" + aprobado +
                '}';
    }
}


