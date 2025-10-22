package PaquetesTiquetes;

import java.util.Collections;
import java.util.List;
import java.util.Collections;
import java.util.List;

public abstract class Paquete implements ProductoVenta {
    public Boolean transferible;
    public Double precioPaq;

    public Paquete() {
        this.transferible = true;
        this.precioPaq = 0.0;
    }

    @Override
    public boolean esTransferible() {
        return Boolean.TRUE.equals(transferible);
    }

    @Override
    public double getPrecioBaseTotal() {
        return precioPaq != null ? precioPaq : 0.0;
    }

    @Override
    public int getCantidadTiquetesTotales() {
        return 0; // por defecto, los paquetes concretos definen su cantidad
    }

    @Override
    public List<Tiquete> generarTiquetes() {
        return Collections.emptyList();
    }
}
