package ZonasMaster;

import java.time.LocalDateTime;

/**
 * Representa un período de tiempo con fecha/hora de inicio y fin
 * Usado para ofertas y reportes con rango de fechas
 */
public class VentanaTiempo {
    private LocalDateTime inicio;
    private LocalDateTime fin;

    /**
     * Constructor
     */
    public VentanaTiempo(LocalDateTime inicio, LocalDateTime fin) {
        this.inicio = inicio;
        this.fin = fin;
    }

    /**
     * Verifica si una fecha está dentro de la ventana
     * @param fecha fecha a verificar
     * @return true si la fecha está dentro del rango
     */
    public boolean incluye(LocalDateTime fecha) {
        return !fecha.isBefore(inicio) && !fecha.isAfter(fin);
    }

    /**
     * Verifica si la ventana está activa en este momento
     * @return true si la fecha/hora actual está dentro de la ventana
     */
    public boolean estaActiva() {
        LocalDateTime ahora = LocalDateTime.now();
        return incluye(ahora);
    }

    // Getters y Setters
    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFin() {
        return fin;
    }

    public void setFin(LocalDateTime fin) {
        this.fin = fin;
    }

    @Override
    public String toString() {
        return "VentanaTiempo{" +
                "inicio=" + inicio +
                ", fin=" + fin +
                '}';
    }
}


