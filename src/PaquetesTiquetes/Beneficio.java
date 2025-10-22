package PaquetesTiquetes;

/**
 * Representa un beneficio o merch incluido en un paquete Deluxe.
 */
public class Beneficio {
    public final String descripcion;

    public Beneficio(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Beneficio{" +
                "descripcion='" + descripcion + '\'' +
                '}';
    }
}
