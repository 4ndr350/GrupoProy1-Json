package PaquetesTiquetes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaqueteDeluxe extends Paquete {
    public String beneficio; // descripcion general
    private final List<ProductoVenta> incluidos;
    private final List<Beneficio> beneficios;

    public PaqueteDeluxe(String beneficio) {
        super();
        this.precioPaq = 10000.0; // precio base del paquete (puede ajustarse)
        this.transferible = false; // los Deluxe no se pueden transferir
        this.beneficio = beneficio;
        this.incluidos = new ArrayList<>();
        this.beneficios = new ArrayList<>();
    }

    public void agregarProducto(ProductoVenta p) {
        if (p != null) incluidos.add(p);
    }

    public void agregarBeneficio(Beneficio b) {
        if (b != null) beneficios.add(b);
    }

    public List<ProductoVenta> getIncluidos() {
        return Collections.unmodifiableList(incluidos);
    }

    public List<Beneficio> getBeneficios() {
        return Collections.unmodifiableList(beneficios);
    }

    @Override
    public int getCantidadTiquetesTotales() {
        return incluidos.stream().mapToInt(ProductoVenta::getCantidadTiquetesTotales).sum();
    }

    @Override
    public List<Tiquete> generarTiquetes() {
        // Genera los tiquetes incluidos, marc�ndolos como tipo DELUXE para bloquear transferencia en Comprador
        List<Tiquete> out = new ArrayList<>();
        for (ProductoVenta p : incluidos) {
            List<Tiquete> ts = p.generarTiquetes();
            for (Tiquete t : ts) {
                if (t != null) t.tipo = "DELUXE";
            }
            out.addAll(ts);
        }
        return out;
    }
}
