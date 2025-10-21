package Actores;

/**
 * Representa una regla de cargo por servicio según el tipo de evento
 * El administrador define estas reglas para cada tipo de evento
 */
public class CargoServicio {
    // Atributos
    private String tipoEvento;   // MUSICAL, CULTURAL, DEPORTIVO, RELIGIOSO
    private double porcentaje;   // Porcentaje de cargo (ej: 10.0 para 10%)

    /**
     * Constructor de CargoServicio
     */
    public CargoServicio(String tipoEvento, double porcentaje) {
        this.tipoEvento = tipoEvento;
        this.porcentaje = porcentaje;
    }

    /**
     * Calcula el cargo de servicio para un monto base
     * @param montoBase precio base del tiquete
     * @return monto del cargo de servicio
     */
    public double calcularCargo(double montoBase) {
        return montoBase * (porcentaje / 100.0);
    }

    // Getters y Setters
    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        if (porcentaje >= 0 && porcentaje <= 100) {
            this.porcentaje = porcentaje;
        } else {
            System.out.println("El porcentaje debe estar entre 0 y 100");
        }
    }

    @Override
    public String toString() {
        return "CargoServicio{" +
                "tipoEvento='" + tipoEvento + '\'' +
                ", porcentaje=" + porcentaje + "%" +
                '}';
    }
}
