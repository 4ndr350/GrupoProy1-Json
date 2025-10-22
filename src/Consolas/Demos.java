package Consolas;

import Actores.Administrador;
import Actores.Comprador;
import Actores.Organizador;
import PaquetesTiquetes.Tiquete;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import ZonasMaster.VentanaTiempo;
import ZonasMaster.Venue;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Demostraciones visibles en consola de funcionalidades basicas.
 * - Limite por transaccion
 * - Seleccion de localidad (valida/ invalida)
 * - Compra que agrega un tiquete y descuenta saldo
 */
public final class Demos {
    private Demos() {}

    public static void runAll() {
        demoLimitePorTransaccion();
        linea();
        demoSeleccionLocalidad();
        linea();
        demoCompraAgregaTiquete();
    }

    public static void runOne(String cual) {
        switch (String.valueOf(cual).toLowerCase(Locale.ROOT)) {
            case "limite": demoLimitePorTransaccion(); break;
            case "localidad": demoSeleccionLocalidad(); break;
            case "compra": demoCompraAgregaTiquete(); break;
            default:
                System.out.println("Nombre no reconocido. Use: limite | localidad | compra");
        }
    }

    // ===== Escenario base reutilizable =====
    private static class Ctx {
        Administrador admin; Organizador org; Venue venue; Evento evento;
        Localidad vip; Localidad general; Comprador c1;
    }

    private static Ctx setupBasico() {
        Ctx ctx = new Ctx();
        ctx.admin = new Administrador("adm-1", "admin", "admin123", "Admin", "admin@tiq.com");
        ctx.admin.fijarCargoServicio("MUSICAL", 10.0);
        ctx.admin.fijarCostoFijoEmision(5.0);

        ctx.venue = new Venue("v-1", "Estadio ABC", "Ciudad", 5000, "");
        ctx.admin.aprobarVenue(ctx.venue);

        ctx.org = new Organizador("org-1", "org", "org123", "Organizador Uno", "org@tiq.com");
        LocalDateTime fecha = LocalDateTime.now().plusDays(7).withHour(20).withMinute(0);
        ctx.evento = new Evento("e-1", "Concierto Rock", "MUSICAL", fecha, ctx.venue);
        ctx.org.crearEvento(ctx.evento);

        ctx.vip = new Localidad("loc-1", "VIP", true, 200.0, 1000);
        ctx.general = new Localidad("loc-2", "General", false, 100.0, 3000);
        ctx.org.configurarLocalidades(ctx.evento, Arrays.asList(ctx.vip, ctx.general));

        ctx.c1 = new Comprador("c-1", "c1", "pass1", "Cliente Uno", "c1@mail.com");
        ctx.c1.acreditarSaldo(2000.0);
        return ctx;
    }

    // ===== Demos =====
    private static void demoLimitePorTransaccion() {
        System.out.println("=== DEMO: Limite por transaccion ===");
        Ctx ctx = setupBasico();
        ctx.evento.setMaximoTiquetesPorTransaccion(5);
        System.out.println("Evento: " + ctx.evento.getNombre() + ", max por transaccion = " + ctx.evento.getMaximoTiquetesPorTransaccion());
        System.out.println("Localidad: " + ctx.general.getNombre());

        List<Comprador.ItemCompra> intentoOK = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, ctx.general, 5));
        System.out.println("Intento comprar 5 boletos (deberia PERMITIR):");
        Object r1 = ctx.c1.comprarTiquetes(intentoOK, "TARJETA", ctx.admin);
        System.out.println(r1 != null ? "Resultado: OK" : "Resultado: RECHAZADO");

        List<Comprador.ItemCompra> intentoExcede = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, ctx.general, 6));
        System.out.println("Intento comprar 6 boletos (deberia RECHAZAR):");
        Object r2 = ctx.c1.comprarTiquetes(intentoExcede, "TARJETA", ctx.admin);
        System.out.println(r2 != null ? "Resultado: OK (NO esperado)" : "Resultado: RECHAZADO (Correcto)");
    }

    private static void demoSeleccionLocalidad() {
        System.out.println("=== DEMO: Seleccion de localidad ===");
        Ctx ctx = setupBasico();
        System.out.println("Localidades disponibles: ");
        for (Localidad l : ctx.evento.getLocalidades()) {
            System.out.println(" - " + l.getId() + " -> " + l.getNombre());
        }

        Localidad falsa = new Localidad("X-999", "Falsa", false, 50.0, 10);
        System.out.println("Intento comprar en localidad NO perteneciente al evento: " + falsa.getNombre());
        List<Comprador.ItemCompra> mal = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, falsa, 1));
        Object r1 = ctx.c1.comprarTiquetes(mal, "TARJETA", ctx.admin);
        System.out.println(r1 != null ? "Resultado: OK (NO esperado)" : "Resultado: RECHAZADO (Correcto)");

        System.out.println("Intento comprar en localidad valida: " + ctx.vip.getNombre());
        List<Comprador.ItemCompra> bien = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, ctx.vip, 1));
        Object r2 = ctx.c1.comprarTiquetes(bien, "TARJETA", ctx.admin);
        System.out.println(r2 != null ? "Resultado: OK (Correcto)" : "Resultado: RECHAZADO (NO esperado)");
    }

    private static void demoCompraAgregaTiquete() {
        System.out.println("=== DEMO: Compra agrega tiquete y descuenta saldo ===");
        Ctx ctx = setupBasico();
        System.out.println("Saldo inicial de " + ctx.c1.getNombre() + ": $" + String.format(Locale.US, "%.2f", ctx.c1.getSaldoVirtual()));
        System.out.println("Tiquetes iniciales: " + ctx.c1.listarMisTiquetes().size());

        VentanaTiempo vt = new VentanaTiempo(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(2));
        ctx.org.crearOferta(ctx.evento, ctx.general, 10.0, vt);
        List<Comprador.ItemCompra> items = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, ctx.general, 1));
        Comprador.Recibo recibo = ctx.c1.comprarTiquetes(items, "SALDO_VIRTUAL", ctx.admin);

        if (recibo != null) {
            System.out.println("Compra OK. Total pagado: $" + String.format(Locale.US, "%.2f", recibo.totalPagar));
        } else {
            System.out.println("Compra RECHAZADA");
        }

        System.out.println("Tiquetes despues: " + ctx.c1.listarMisTiquetes().size());
        if (!ctx.c1.listarMisTiquetes().isEmpty()) {
            Tiquete t = ctx.c1.listarMisTiquetes().get(0);
            System.out.println("Primer tiquete -> id=" + t.idTiquete + ", tipo=" + t.tipo + ", fecha=" + t.fecha + ", hora=" + t.hora);
        }
        System.out.println("Saldo final: $" + String.format(Locale.US, "%.2f", ctx.c1.getSaldoVirtual()));
    }

    private static void linea() {
        System.out.println("----------------------------------------");
    }
}


