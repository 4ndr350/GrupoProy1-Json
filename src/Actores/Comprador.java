<<<<<<< HEAD
package Actores;

import PaquetesTiquetes.Tiquete;
import Reportes.RegistroVentas;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Representa el ROL de Comprador en el sistema
 * Cualquier usuario puede actuar como Comprador para adquirir tiquetes
 */
public class Comprador extends Usuario {
    // Atributos adicionales
    private List<Tiquete> misTiquetes;

    /**
     * Modelo de item de compra para consola/API simple.
     */
    public static class ItemCompra {
        public final Evento evento;
        public final Localidad localidad;
        public final int cantidad;

        public ItemCompra(Evento evento, Localidad localidad, int cantidad) {
            this.evento = evento;
            this.localidad = localidad;
            this.cantidad = cantidad;
        }
    }

    /**
     * L韓ea de recibo con desglose por item.
     */
    public static class LineaRecibo {
        public final Evento evento;
        public final Localidad localidad;
        public final int cantidad;
        public final double precioUnitarioBase;
        public final double cargoServicioUnitario;
        public final double costoEmisionUnitario;
        public final double totalLinea;

        public LineaRecibo(Evento evento, Localidad localidad, int cantidad,
                           double precioUnitarioBase, double cargoServicioUnitario,
                           double costoEmisionUnitario, double totalLinea) {
            this.evento = evento;
            this.localidad = localidad;
            this.cantidad = cantidad;
            this.precioUnitarioBase = precioUnitarioBase;
            this.cargoServicioUnitario = cargoServicioUnitario;
            this.costoEmisionUnitario = costoEmisionUnitario;
            this.totalLinea = totalLinea;
        }
    }

    /**
     * Recibo de compra con totales.
     */
    public static class Recibo {
        public final List<LineaRecibo> lineas;
        public final int totalBoletos;
        public final double totalBase;
        public final double totalCargosServicio;
        public final double totalCostosEmision;
        public final double totalPagar;

        public Recibo(List<LineaRecibo> lineas) {
            this.lineas = Collections.unmodifiableList(new ArrayList<>(lineas));
            int sumCant = 0;
            double base = 0, cargos = 0, emision = 0, total = 0;
            for (LineaRecibo l : lineas) {
                sumCant += l.cantidad;
                base += l.precioUnitarioBase * l.cantidad;
                cargos += l.cargoServicioUnitario * l.cantidad;
                emision += l.costoEmisionUnitario * l.cantidad;
                total += l.totalLinea;
            }
            this.totalBoletos = sumCant;
            this.totalBase = base;
            this.totalCargosServicio = cargos;
            this.totalCostosEmision = emision;
            this.totalPagar = total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("========== RECIBO ==========\n");
            for (LineaRecibo l : lineas) {
                sb.append(String.format(Locale.US,
                        "%s - %s x%d: base=%.2f cargo=%.2f emision=%.2f total=%.2f\n",
                        l.evento.getNombre(), l.localidad.getNombre(), l.cantidad,
                        l.precioUnitarioBase, l.cargoServicioUnitario,
                        l.costoEmisionUnitario, l.totalLinea));
            }
            sb.append(String.format(Locale.US, "Boletos: %d\n", totalBoletos));
            sb.append(String.format(Locale.US, "Base: $%.2f\n", totalBase));
            sb.append(String.format(Locale.US, "Cargos: $%.2f\n", totalCargosServicio));
            sb.append(String.format(Locale.US, "Emision: $%.2f\n", totalCostosEmision));
            sb.append(String.format(Locale.US, "TOTAL: $%.2f\n", totalPagar));
            sb.append("===========================\n");
            return sb.toString();
        }
    }

    /**
     * Constructor de Comprador
     */
    public Comprador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.misTiquetes = new ArrayList<>();
    }

    /**
     * Versi髇 compatible: espera elementos de tipo ItemCompra en la lista.
     * Para cargos y costos usa 0 por no contar con Administrador en la firma.
     */
    public Object comprarTiquetes(List<Object> items, String medioPago) {
        List<ItemCompra> cast = new ArrayList<>();
        for (Object o : items) {
            if (o instanceof ItemCompra) cast.add((ItemCompra) o);
        }
        return comprarTiquetes(cast, medioPago, null);
    }

    /**
     * Compra tiquetes con soporte de cargos (si se provee Administrador).
     * - Valida disponibilidad y l韒ite por transacci髇 por evento
     * - Calcula precio: base + cargo% + emision fija (por tiquete)
     * - Procesa pago (saldo virtual o externo)
     * - Genera tiquetes y los agrega al inventario del comprador
     */
    public Recibo comprarTiquetes(List<ItemCompra> items, String medioPago, Administrador admin) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay items para comprar");
            return null;
        }

        // Acumular cantidades por evento para validar maximo por transaccion
        Map<Evento, Integer> conteoPorEvento = new HashMap<>();
        for (ItemCompra it : items) {
            if (it == null || it.evento == null || it.localidad == null || it.cantidad <= 0) {
                System.out.println("Item de compra invalido");
                return null;
            }
            conteoPorEvento.put(it.evento, conteoPorEvento.getOrDefault(it.evento, 0) + it.cantidad);
        }

        // Validaciones por item
        for (ItemCompra it : items) {
            if (!"PROGRAMADO".equalsIgnoreCase(it.evento.getEstado())) {
                System.out.println("El evento no esta programado: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.getLocalidades().contains(it.localidad)) {
                System.out.println("La localidad no pertenece al evento");
                return null;
            }
            int totalEvento = conteoPorEvento.get(it.evento);
            if (totalEvento > it.evento.getMaximoTiquetesPorTransaccion()) {
                System.out.println("Excede maximo por transaccion para el evento: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.puedeVender(it.localidad, it.cantidad)) {
                System.out.println("No hay disponibilidad suficiente en " + it.localidad.getNombre());
                return null;
            }
        }

        // Calculo de totales y generaci髇 de l韓eas
        List<LineaRecibo> lineas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        double totalPagar = 0;

        for (ItemCompra it : items) {
            boolean esCortesia = false; // Cortesias requieren flujo de Organizador; no aplican aqui directamente

            double precioBaseUnit = it.evento.precioVigente(it.localidad, ahora);
            double cargoPct = (admin != null) ? admin.obtenerCargoServicio(it.evento.getTipoEvento()) : 0.0;
            double cargoUnit = esCortesia ? 0.0 : (precioBaseUnit * (cargoPct / 100.0));
            double emisionUnit = (admin != null && !esCortesia) ? admin.getCostoFijoEmision() : 0.0;
            double totalLinea = (esCortesia ? 0.0 : (precioBaseUnit + cargoUnit + emisionUnit)) * it.cantidad;

            lineas.add(new LineaRecibo(it.evento, it.localidad, it.cantidad,
                    precioBaseUnit, cargoUnit, emisionUnit, totalLinea));
            totalPagar += totalLinea;

            // Registrar la venta en el registro global (cortesias con base/cargos/emision en 0 ya estan reflejadas)
            RegistroVentas.registrarVenta(it.evento, it.evento.getOrganizador(), it.localidad, it.cantidad,
                    esCortesia ? 0.0 : precioBaseUnit,
                    esCortesia ? 0.0 : cargoUnit,
                    esCortesia ? 0.0 : emisionUnit,
                    ahora,
                    esCortesia);
        }

        // Pago
        boolean pagoOk = true;
        if (totalPagar > 0) {
            if ("SALDO_VIRTUAL".equalsIgnoreCase(medioPago)) {
                pagoOk = debitarSaldo(totalPagar);
            } else {
                // Simula pasarela externa aprobada
                pagoOk = true;
            }
        }
        if (!pagoOk) {
            System.out.println("Pago rechazado");
            return null;
        }

        // Generar tiquetes
        long baseId = System.currentTimeMillis();
        for (ItemCompra it : items) {
            Date fecha = Date.from(it.evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            for (int i = 0; i < it.cantidad; i++) {
                String id = it.evento.getId() + "-" + it.localidad.getId() + "-" + (baseId++) + "-" + i;
                // Asiento: si es numerada se podria asignar un numero; sin inventario real dejamos null
                Tiquete t = new Tiquete(id, it.localidad.getNombre(), fecha, it.evento.getFechaHora().toLocalTime().toString());
                agregarTiquete(t);
            }
        }

        Recibo recibo = new Recibo(lineas);
        System.out.println(recibo);
        return recibo;
    }

    /**
     * Compra tiquetes como organizador: las compras sobre eventos cuyo organizador coincide
     * con el parametro 'organizador' se consideran cortesias (sin cobros ni emision) y se
     * registran como tales en el registro de ventas.
     */
    public Recibo comprarTiquetesComoOrganizador(Organizador organizador, List<ItemCompra> items, String medioPago, Administrador admin) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay items para comprar");
            return null;
        }

        Map<Evento, Integer> conteoPorEvento = new HashMap<>();
        for (ItemCompra it : items) {
            if (it == null || it.evento == null || it.localidad == null || it.cantidad <= 0) {
                System.out.println("Item de compra invalido");
                return null;
            }
            conteoPorEvento.put(it.evento, conteoPorEvento.getOrDefault(it.evento, 0) + it.cantidad);
        }

        for (ItemCompra it : items) {
            if (!"PROGRAMADO".equalsIgnoreCase(it.evento.getEstado())) {
                System.out.println("El evento no esta programado: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.getLocalidades().contains(it.localidad)) {
                System.out.println("La localidad no pertenece al evento");
                return null;
            }
            int totalEvento = conteoPorEvento.get(it.evento);
            if (totalEvento > it.evento.getMaximoTiquetesPorTransaccion()) {
                System.out.println("Excede maximo por transaccion para el evento: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.puedeVender(it.localidad, it.cantidad)) {
                System.out.println("No hay disponibilidad suficiente en " + it.localidad.getNombre());
                return null;
            }
        }

        List<LineaRecibo> lineas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        double totalPagar = 0;

        for (ItemCompra it : items) {
            boolean esCortesia = it.evento.getOrganizador() != null && it.evento.getOrganizador().equals(organizador);

            double precioBaseUnit = it.evento.precioVigente(it.localidad, ahora);
            double cargoPct = (admin != null) ? admin.obtenerCargoServicio(it.evento.getTipoEvento()) : 0.0;
            double cargoUnit = esCortesia ? 0.0 : (precioBaseUnit * (cargoPct / 100.0));
            double emisionUnit = (admin != null && !esCortesia) ? admin.getCostoFijoEmision() : 0.0;
            double totalLinea = (esCortesia ? 0.0 : (precioBaseUnit + cargoUnit + emisionUnit)) * it.cantidad;

            lineas.add(new LineaRecibo(it.evento, it.localidad, it.cantidad,
                    precioBaseUnit, cargoUnit, emisionUnit, totalLinea));
            totalPagar += totalLinea;

            RegistroVentas.registrarVenta(it.evento, it.evento.getOrganizador(), it.localidad, it.cantidad,
                    esCortesia ? 0.0 : precioBaseUnit,
                    esCortesia ? 0.0 : cargoUnit,
                    esCortesia ? 0.0 : emisionUnit,
                    ahora,
                    esCortesia);
        }

        boolean pagoOk = true;
        if (totalPagar > 0) {
            if ("SALDO_VIRTUAL".equalsIgnoreCase(medioPago)) {
                pagoOk = debitarSaldo(totalPagar);
            } else {
                pagoOk = true;
            }
        }
        if (!pagoOk) {
            System.out.println("Pago rechazado");
            return null;
        }

        long baseId = System.currentTimeMillis();
        for (ItemCompra it : items) {
            Date fecha = Date.from(it.evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            for (int i = 0; i < it.cantidad; i++) {
                String id = it.evento.getId() + "-" + it.localidad.getId() + "-" + (baseId++) + "-" + i;
                Tiquete t = new Tiquete(id, it.localidad.getNombre(), fecha, it.evento.getFechaHora().toLocalTime().toString());
                agregarTiquete(t);
            }
        }

        Recibo recibo = new Recibo(lineas);
        System.out.println(recibo);
        return recibo;
    }

    /**
     * Transfiere un tiquete a otro usuario con validaciones basicas.
     */
    public boolean transferirTiquete(Tiquete tiquete, Usuario destinatario, String password) {
        // Verificar autenticaci髇
        if (!autenticar(password)) {
            System.out.println("Contrase馻 incorrecta");
            return false;
        }

        // Verificar propiedad del tiquete
        if (!misTiquetes.contains(tiquete)) {
            System.out.println("Este tiquete no te pertenece");
            return false;
        }

        // Restriccion: no transferir tiquetes tipo DELUXE (segun dominio)
        if (tiquete.tipo != null && tiquete.tipo.equalsIgnoreCase("DELUXE")) {
            System.out.println("Los tiquetes Deluxe no se pueden transferir");
            return false;
        }

        // Restriccion: no transferir tiquete vencido (fecha pasada)
        if (tiquete.fecha != null && tiquete.fecha.before(new Date())) {
            System.out.println("El tiquete esta vencido y no se puede transferir");
            return false;
        }

        if (!(destinatario instanceof Comprador)) {
            System.out.println("El destinatario no puede recibir tiquetes");
            return false;
        }

        // Ejecutar transferencia
        misTiquetes.remove(tiquete);
        ((Comprador) destinatario).agregarTiquete(tiquete);

        System.out.println("Tiquete transferido a: " + destinatario.getNombre());
        return true;
    }

    /**
     * Lista todos los tiquetes del comprador
     */
    public List<Tiquete> listarMisTiquetes() {
        return new ArrayList<>(misTiquetes);
    }

    /**
     * Agrega un tiquete a la colecci髇
     */
    public void agregarTiquete(Tiquete tiquete) {
        if (tiquete != null && !misTiquetes.contains(tiquete)) {
            misTiquetes.add(tiquete);
        }
    }

    /**
     * Remueve un tiquete de la colecci髇
     */
    public void removerTiquete(Tiquete tiquete) {
        misTiquetes.remove(tiquete);
    }

    @Override
    public String toString() {
        return "Comprador{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", numTiquetes=" + misTiquetes.size() +
                ", saldoVirtual=$" + getSaldoVirtual() +
                '}';
    }
}
=======
package Actores;

import PaquetesTiquetes.Tiquete;
import Reportes.RegistroVentas;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Representa el ROL de Comprador en el sistema
 * Cualquier usuario puede actuar como Comprador para adquirir tiquetes
 */
public class Comprador extends Usuario {
    // Atributos adicionales
    private List<Tiquete> misTiquetes;

    /**
     * Modelo de item de compra para consola/API simple.
     */
    public static class ItemCompra {
        public final Evento evento;
        public final Localidad localidad;
        public final int cantidad;

        public ItemCompra(Evento evento, Localidad localidad, int cantidad) {
            this.evento = evento;
            this.localidad = localidad;
            this.cantidad = cantidad;
        }
    }

    /**
     * L铆nea de recibo con desglose por item.
     */
    public static class LineaRecibo {
        public final Evento evento;
        public final Localidad localidad;
        public final int cantidad;
        public final double precioUnitarioBase;
        public final double cargoServicioUnitario;
        public final double costoEmisionUnitario;
        public final double totalLinea;

        public LineaRecibo(Evento evento, Localidad localidad, int cantidad,
                           double precioUnitarioBase, double cargoServicioUnitario,
                           double costoEmisionUnitario, double totalLinea) {
            this.evento = evento;
            this.localidad = localidad;
            this.cantidad = cantidad;
            this.precioUnitarioBase = precioUnitarioBase;
            this.cargoServicioUnitario = cargoServicioUnitario;
            this.costoEmisionUnitario = costoEmisionUnitario;
            this.totalLinea = totalLinea;
        }
    }

    /**
     * Recibo de compra con totales.
     */
    public static class Recibo {
        public final List<LineaRecibo> lineas;
        public final int totalBoletos;
        public final double totalBase;
        public final double totalCargosServicio;
        public final double totalCostosEmision;
        public final double totalPagar;

        public Recibo(List<LineaRecibo> lineas) {
            this.lineas = Collections.unmodifiableList(new ArrayList<>(lineas));
            int sumCant = 0;
            double base = 0, cargos = 0, emision = 0, total = 0;
            for (LineaRecibo l : lineas) {
                sumCant += l.cantidad;
                base += l.precioUnitarioBase * l.cantidad;
                cargos += l.cargoServicioUnitario * l.cantidad;
                emision += l.costoEmisionUnitario * l.cantidad;
                total += l.totalLinea;
            }
            this.totalBoletos = sumCant;
            this.totalBase = base;
            this.totalCargosServicio = cargos;
            this.totalCostosEmision = emision;
            this.totalPagar = total;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("========== RECIBO ==========\n");
            for (LineaRecibo l : lineas) {
                sb.append(String.format(Locale.US,
                        "%s - %s x%d: base=%.2f cargo=%.2f emision=%.2f total=%.2f\n",
                        l.evento.getNombre(), l.localidad.getNombre(), l.cantidad,
                        l.precioUnitarioBase, l.cargoServicioUnitario,
                        l.costoEmisionUnitario, l.totalLinea));
            }
            sb.append(String.format(Locale.US, "Boletos: %d\n", totalBoletos));
            sb.append(String.format(Locale.US, "Base: $%.2f\n", totalBase));
            sb.append(String.format(Locale.US, "Cargos: $%.2f\n", totalCargosServicio));
            sb.append(String.format(Locale.US, "Emision: $%.2f\n", totalCostosEmision));
            sb.append(String.format(Locale.US, "TOTAL: $%.2f\n", totalPagar));
            sb.append("===========================\n");
            return sb.toString();
        }
    }

    /**
     * Constructor de Comprador
     */
    public Comprador(String id, String login, String password, String nombre, String correo) {
        super(id, login, password, nombre, correo);
        this.misTiquetes = new ArrayList<>();
    }

    /**
     * Versi贸n compatible: espera elementos de tipo ItemCompra en la lista.
     * Para cargos y costos usa 0 por no contar con Administrador en la firma.
     */
    public Object comprarTiquetes(List<Object> items, String medioPago) {
        List<ItemCompra> cast = new ArrayList<>();
        for (Object o : items) {
            if (o instanceof ItemCompra) cast.add((ItemCompra) o);
        }
        return comprarTiquetes(cast, medioPago, null);
    }

    /**
     * Compra tiquetes con soporte de cargos (si se provee Administrador).
     * - Valida disponibilidad y l铆mite por transacci贸n por evento
     * - Calcula precio: base + cargo% + emision fija (por tiquete)
     * - Procesa pago (saldo virtual o externo)
     * - Genera tiquetes y los agrega al inventario del comprador
     */
    public Recibo comprarTiquetes(List<ItemCompra> items, String medioPago, Administrador admin) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay items para comprar");
            return null;
        }

        // Acumular cantidades por evento para validar maximo por transaccion
        Map<Evento, Integer> conteoPorEvento = new HashMap<>();
        for (ItemCompra it : items) {
            if (it == null || it.evento == null || it.localidad == null || it.cantidad <= 0) {
                System.out.println("Item de compra invalido");
                return null;
            }
            conteoPorEvento.put(it.evento, conteoPorEvento.getOrDefault(it.evento, 0) + it.cantidad);
        }

        // Validaciones por item
        for (ItemCompra it : items) {
            if (!"PROGRAMADO".equalsIgnoreCase(it.evento.getEstado())) {
                System.out.println("El evento no esta programado: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.getLocalidades().contains(it.localidad)) {
                System.out.println("La localidad no pertenece al evento");
                return null;
            }
            int totalEvento = conteoPorEvento.get(it.evento);
            if (totalEvento > it.evento.getMaximoTiquetesPorTransaccion()) {
                System.out.println("Excede maximo por transaccion para el evento: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.puedeVender(it.localidad, it.cantidad)) {
                System.out.println("No hay disponibilidad suficiente en " + it.localidad.getNombre());
                return null;
            }
        }

        // Calculo de totales y generaci贸n de l铆neas
        List<LineaRecibo> lineas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        double totalPagar = 0;

        for (ItemCompra it : items) {
            boolean esCortesia = false; // Cortesias requieren flujo de Organizador; no aplican aqui directamente

            double precioBaseUnit = it.evento.precioVigente(it.localidad, ahora);
            double cargoPct = (admin != null) ? admin.obtenerCargoServicio(it.evento.getTipoEvento()) : 0.0;
            double cargoUnit = esCortesia ? 0.0 : (precioBaseUnit * (cargoPct / 100.0));
            double emisionUnit = (admin != null && !esCortesia) ? admin.getCostoFijoEmision() : 0.0;
            double totalLinea = (esCortesia ? 0.0 : (precioBaseUnit + cargoUnit + emisionUnit)) * it.cantidad;

            lineas.add(new LineaRecibo(it.evento, it.localidad, it.cantidad,
                    precioBaseUnit, cargoUnit, emisionUnit, totalLinea));
            totalPagar += totalLinea;

            // Registrar la venta en el registro global (cortesias con base/cargos/emision en 0 ya estan reflejadas)
            RegistroVentas.registrarVenta(it.evento, it.evento.getOrganizador(), it.localidad, it.cantidad,
                    esCortesia ? 0.0 : precioBaseUnit,
                    esCortesia ? 0.0 : cargoUnit,
                    esCortesia ? 0.0 : emisionUnit,
                    ahora,
                    esCortesia);
        }

        // Pago
        boolean pagoOk = true;
        if (totalPagar > 0) {
            if ("SALDO_VIRTUAL".equalsIgnoreCase(medioPago)) {
                pagoOk = debitarSaldo(totalPagar);
            } else {
                // Simula pasarela externa aprobada
                pagoOk = true;
            }
        }
        if (!pagoOk) {
            System.out.println("Pago rechazado");
            return null;
        }

        // Generar tiquetes
        long baseId = System.currentTimeMillis();
        for (ItemCompra it : items) {
            Date fecha = Date.from(it.evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            for (int i = 0; i < it.cantidad; i++) {
                String id = it.evento.getId() + "-" + it.localidad.getId() + "-" + (baseId++) + "-" + i;
                // Asiento: si es numerada se podria asignar un numero; sin inventario real dejamos null
                Tiquete t = new Tiquete(id, it.localidad.getNombre(), fecha, it.evento.getFechaHora().toLocalTime().toString());
                agregarTiquete(t);
            }
        }

        Recibo recibo = new Recibo(lineas);
        System.out.println(recibo);
        return recibo;
    }

    /**
     * Compra tiquetes como organizador: las compras sobre eventos cuyo organizador coincide
     * con el parametro 'organizador' se consideran cortesias (sin cobros ni emision) y se
     * registran como tales en el registro de ventas.
     */
    public Recibo comprarTiquetesComoOrganizador(Organizador organizador, List<ItemCompra> items, String medioPago, Administrador admin) {
        if (items == null || items.isEmpty()) {
            System.out.println("No hay items para comprar");
            return null;
        }

        Map<Evento, Integer> conteoPorEvento = new HashMap<>();
        for (ItemCompra it : items) {
            if (it == null || it.evento == null || it.localidad == null || it.cantidad <= 0) {
                System.out.println("Item de compra invalido");
                return null;
            }
            conteoPorEvento.put(it.evento, conteoPorEvento.getOrDefault(it.evento, 0) + it.cantidad);
        }

        for (ItemCompra it : items) {
            if (!"PROGRAMADO".equalsIgnoreCase(it.evento.getEstado())) {
                System.out.println("El evento no esta programado: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.getLocalidades().contains(it.localidad)) {
                System.out.println("La localidad no pertenece al evento");
                return null;
            }
            int totalEvento = conteoPorEvento.get(it.evento);
            if (totalEvento > it.evento.getMaximoTiquetesPorTransaccion()) {
                System.out.println("Excede maximo por transaccion para el evento: " + it.evento.getNombre());
                return null;
            }
            if (!it.evento.puedeVender(it.localidad, it.cantidad)) {
                System.out.println("No hay disponibilidad suficiente en " + it.localidad.getNombre());
                return null;
            }
        }

        List<LineaRecibo> lineas = new ArrayList<>();
        LocalDateTime ahora = LocalDateTime.now();
        double totalPagar = 0;

        for (ItemCompra it : items) {
            boolean esCortesia = it.evento.getOrganizador() != null && it.evento.getOrganizador().equals(organizador);

            double precioBaseUnit = it.evento.precioVigente(it.localidad, ahora);
            double cargoPct = (admin != null) ? admin.obtenerCargoServicio(it.evento.getTipoEvento()) : 0.0;
            double cargoUnit = esCortesia ? 0.0 : (precioBaseUnit * (cargoPct / 100.0));
            double emisionUnit = (admin != null && !esCortesia) ? admin.getCostoFijoEmision() : 0.0;
            double totalLinea = (esCortesia ? 0.0 : (precioBaseUnit + cargoUnit + emisionUnit)) * it.cantidad;

            lineas.add(new LineaRecibo(it.evento, it.localidad, it.cantidad,
                    precioBaseUnit, cargoUnit, emisionUnit, totalLinea));
            totalPagar += totalLinea;

            RegistroVentas.registrarVenta(it.evento, it.evento.getOrganizador(), it.localidad, it.cantidad,
                    esCortesia ? 0.0 : precioBaseUnit,
                    esCortesia ? 0.0 : cargoUnit,
                    esCortesia ? 0.0 : emisionUnit,
                    ahora,
                    esCortesia);
        }

        boolean pagoOk = true;
        if (totalPagar > 0) {
            if ("SALDO_VIRTUAL".equalsIgnoreCase(medioPago)) {
                pagoOk = debitarSaldo(totalPagar);
            } else {
                pagoOk = true;
            }
        }
        if (!pagoOk) {
            System.out.println("Pago rechazado");
            return null;
        }

        long baseId = System.currentTimeMillis();
        for (ItemCompra it : items) {
            Date fecha = Date.from(it.evento.getFechaHora().atZone(ZoneId.systemDefault()).toInstant());
            for (int i = 0; i < it.cantidad; i++) {
                String id = it.evento.getId() + "-" + it.localidad.getId() + "-" + (baseId++) + "-" + i;
                Tiquete t = new Tiquete(id, it.localidad.getNombre(), fecha, it.evento.getFechaHora().toLocalTime().toString());
                agregarTiquete(t);
            }
        }

        Recibo recibo = new Recibo(lineas);
        System.out.println(recibo);
        return recibo;
    }

    /**
     * Transfiere un tiquete a otro usuario con validaciones basicas.
     */
    public boolean transferirTiquete(Tiquete tiquete, Usuario destinatario, String password) {
        // Verificar autenticaci贸n
        if (!autenticar(password)) {
            System.out.println("Contrase帽a incorrecta");
            return false;
        }

        // Verificar propiedad del tiquete
        if (!misTiquetes.contains(tiquete)) {
            System.out.println("Este tiquete no te pertenece");
            return false;
        }

        // Restriccion: no transferir tiquetes tipo DELUXE (segun dominio)
        if (tiquete.tipo != null && tiquete.tipo.equalsIgnoreCase("DELUXE")) {
            System.out.println("Los tiquetes Deluxe no se pueden transferir");
            return false;
        }

        // Restriccion: no transferir tiquete vencido (fecha pasada)
        if (tiquete.fecha != null && tiquete.fecha.before(new Date())) {
            System.out.println("El tiquete esta vencido y no se puede transferir");
            return false;
        }

        if (!(destinatario instanceof Comprador)) {
            System.out.println("El destinatario no puede recibir tiquetes");
            return false;
        }

        // Ejecutar transferencia
        misTiquetes.remove(tiquete);
        ((Comprador) destinatario).agregarTiquete(tiquete);

        System.out.println("Tiquete transferido a: " + destinatario.getNombre());
        return true;
    }

    /**
     * Lista todos los tiquetes del comprador
     */
    public List<Tiquete> listarMisTiquetes() {
        return new ArrayList<>(misTiquetes);
    }

    /**
     * Agrega un tiquete a la colecci贸n
     */
    public void agregarTiquete(Tiquete tiquete) {
        if (tiquete != null && !misTiquetes.contains(tiquete)) {
            misTiquetes.add(tiquete);
        }
    }

    /**
     * Remueve un tiquete de la colecci贸n
     */
    public void removerTiquete(Tiquete tiquete) {
        misTiquetes.remove(tiquete);
    }

    @Override
    public String toString() {
        return "Comprador{" +
                "id='" + getId() + '\'' +
                ", nombre='" + getNombre() + '\'' +
                ", numTiquetes=" + misTiquetes.size() +
                ", saldoVirtual=$" + getSaldoVirtual() +
                '}';
    }
}
>>>>>>> 25a6273abf714110a79f8ae8be174e675ff6c793
