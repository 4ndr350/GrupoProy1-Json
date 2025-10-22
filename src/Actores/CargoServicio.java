package Actores;

/**
 * Regla de cargo por servicio según el tipo de evento.
 * El administrador define estas reglas para cada tipo de evento.
 */
public class CargoServicio {
    private String tipoEvento;   // MUSICAL, CULTURAL, DEPORTIVO, RELIGIOSO
    private double porcentaje;   // Porcentaje de cargo (ej: 10.0 para 10%)

    public CargoServicio(String tipoEvento, double porcentaje) {
        this.tipoEvento = (tipoEvento == null) ? "" : tipoEvento.trim().toUpperCase();
        setPorcentaje(porcentaje);
    }

    /**
     * Calcula el cargo de servicio para un monto base.
     */
    public double calcularCargo(double montoBase) {
        return montoBase * (porcentaje / 100.0);
    }

    public String getTipoEvento() {
        return tipoEvento;
    }

    public void setTipoEvento(String tipoEvento) {
        this.tipoEvento = (tipoEvento == null) ? "" : tipoEvento.trim().toUpperCase();
    }

    public double getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(double porcentaje) {
        if (porcentaje < 0 || porcentaje > 100) {
            System.out.println("El porcentaje debe estar entre 0 y 100");
            return;
        }
        this.porcentaje = porcentaje;
    }

    @Override
    public String toString() {
        return "CargoServicio{" +
                "tipoEvento='" + tipoEvento + '\'' +
                ", porcentaje=" + porcentaje + "%" +
                '}';
    }
}
