package Consolas;

import Actores.Administrador;
import Actores.Comprador;
import Actores.Organizador;
import PaquetesTiquetes.*;
import Reportes.ReportePromotor;
import Reportes.ReporteTiquetera;
import ZonasMaster.Evento;
import ZonasMaster.Localidad;
import ZonasMaster.VentanaTiempo;
import ZonasMaster.Venue;
import Persistencia.SnapshotPersistencia;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Consola principal de demostración.
 * Uso rápido:
 *   - Sin argumentos: muestra menú interactivo minimal.
 *   - Con argumento: compras | paquetes | cancelacion | reporte-tiq | reporte-prom | pruebas | prueba-limite | prueba-localidad | prueba-compra
 */
public class Main {
    private static final boolean PERSISTIR_JSON = true; // Guardar snapshots JSON en carpeta data

    public static void main(String[] args) {
        if (args != null && args.length > 0) {
            ejecutar(args[0].trim().toLowerCase(Locale.ROOT));
            return;
        }

        Scanner sc = new Scanner(System.in);
        System.out.println("==== Consola Tiquetera ====");
        System.out.println("Seleccione una opcion:");
        System.out.println("  1) compras");
        System.out.println("  2) paquetes");
        System.out.println("  3) cancelacion");
        System.out.println("  4) reporte-tiq (global)");
        System.out.println("  5) reporte-prom (por evento)");
        System.out.println("  6) pruebas (funcionalidad basica)");
        System.out.print("Opcion [1-6] (ENTER=1): ");
        String opt = sc.nextLine().trim();
        if (opt.isEmpty()) opt = "1";
        switch (opt) {
            case "2": ejecutar("paquetes"); break;
            case "3": ejecutar("cancelacion"); break;
            case "4": ejecutar("reporte-tiq"); break;
            case "5": ejecutar("reporte-prom"); break;
            case "6": ejecutar("pruebas"); break;
            case "1":
            default: ejecutar("compras");
        }
    }

    private static void ejecutar(String comando) {
        switch (comando) {
            case "paquetes": menuPaquetes(); break;
            case "cancelacion": escenarioCancelacion(); break;
            case "reporte-tiq": escenarioReporteTiquetera(); break;
            case "reporte-prom": escenarioReportePromotor(); break;
            case "pruebas": ejecutarPruebas(null); break;
            case "prueba-limite": ejecutarPruebas("limite"); break;
            case "prueba-localidad": ejecutarPruebas("localidad"); break;
            case "prueba-compra": ejecutarPruebas("compra"); break;
            case "compras":
            default: menuCompras();
        }
    }

    // --------- ESCENARIOS ---------
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

    private static void escenarioCompras() {
        System.out.println("-- Escenario: Compras --");
        Ctx ctx = setupBasico();

        // Oferta VIP
        VentanaTiempo vt = new VentanaTiempo(LocalDateTime.now().minusHours(1), LocalDateTime.now().plusHours(3));
        ctx.org.crearOferta(ctx.evento, ctx.vip, 20.0, vt);

        List<Comprador.ItemCompra> items = new ArrayList<>();
        items.add(new Comprador.ItemCompra(ctx.evento, ctx.vip, 2));
        items.add(new Comprador.ItemCompra(ctx.evento, ctx.general, 3));
        ctx.c1.comprarTiquetes(items, "SALDO_VIRTUAL", ctx.admin);

        ReportePromotor rp = new ReportePromotor(ctx.evento);
        System.out.println(rp.generarResumen());
        System.out.println(new ReporteTiquetera().generarResumen());

        guardarJson(ctx, Collections.emptyList());
    }

    // --------- MENUS INTERACTIVOS ---------
    private static void menuCompras() {
        System.out.println("-- Compra interactiva --");
        Ctx ctx = setupBasico();

        // Mostrar evento y localidades
        System.out.println("Evento: " + ctx.evento.getNombre() + " (" + ctx.evento.getTipoEvento() + ")");
        System.out.println("Localidades disponibles:");
        List<Localidad> locs = ctx.evento.getLocalidades();
        for (int i = 0; i < locs.size(); i++) {
            Localidad l = locs.get(i);
            System.out.println("  " + (i+1) + ") " + l.getNombre() + " | Precio base: $" + String.format(java.util.Locale.US, "%.2f", l.getPrecioBase()) + " | Capacidad: " + l.getCapacidad());
        }

        int idx = leerEntero("Elige localidad [1-" + locs.size() + "]: ", 1);
        if (idx < 1 || idx > locs.size()) idx = 1;
        Localidad elegida = locs.get(idx - 1);

        int cantidad = leerEntero("¿Cuántos tiquetes quieres comprar? ", 1);
        String medio = leerLinea("Medio de pago (SALDO_VIRTUAL/TARJETA) [SALDO_VIRTUAL]: ");
        if (medio == null || medio.trim().isEmpty()) medio = "SALDO_VIRTUAL";

        List<Comprador.ItemCompra> items = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, elegida, Math.max(1, cantidad)));
        Comprador.Recibo recibo = ctx.c1.comprarTiquetes(items, medio, ctx.admin);

        if (recibo != null) {
            System.out.println("Compra realizada. Tiquetes ahora: " + ctx.c1.listarMisTiquetes().size());
        } else {
            System.out.println("La compra no se completó.");
        }

        System.out.println(new ReporteTiquetera().generarResumen());
        guardarJson(ctx, Collections.emptyList());
    }

    private static void menuPaquetes() {
        System.out.println("-- Paquetes interactivo --");
        Ctx ctx = setupBasico();

        System.out.println("Selecciona tipo de paquete:");
        System.out.println("  1) Deluxe");
        System.out.println("  2) Grupo");
        System.out.println("  3) Pase de temporada");
        int opt = leerEntero("Opción [1-3]: ", 1);

        switch (opt) {
            case 2: // Grupo
            {
                int n = leerEntero("¿Cuántas entradas quieres en el paquete de grupo? ", 4);
                // Por defecto usar VIP y precio como suma de base simples
                double precio = n * ctx.vip.getPrecioBase();
                PaqueteGrupo grupo = new PaqueteGrupo(ctx.evento, ctx.vip, Math.max(1, n), precio);
                List<PaquetesTiquetes.Tiquete> ts = grupo.generarTiquetes();
                System.out.println("Paquete Grupo generado con tiquetes: " + ts.size());
                for (PaquetesTiquetes.Tiquete t : ts) ctx.c1.agregarTiquete(t);
                break;
            }
            case 3: // Pase de temporada
            {
                PaseTemporada pase = new PaseTemporada(300.0);
                int k = leerEntero("¿Cuántas entradas agregar al pase (al mismo evento)? ", 2);
                for (int i = 0; i < k; i++) pase.agregarEntrada(ctx.evento, ctx.general);
                List<PaquetesTiquetes.Tiquete> ts = pase.generarTiquetes();
                System.out.println("Pase de temporada generado con tiquetes: " + ts.size());
                for (PaquetesTiquetes.Tiquete t : ts) ctx.c1.agregarTiquete(t);
                break;
            }
            case 1: // Deluxe
            default:
            {
                PaqueteGrupo palcoPara4 = new PaqueteGrupo(ctx.evento, ctx.vip, 4, 4 * ctx.vip.getPrecioBase());
                PaseTemporada pase = new PaseTemporada(300.0);
                pase.agregarEntrada(ctx.evento, ctx.general);
                pase.agregarEntrada(ctx.evento, ctx.general);
                PaqueteDeluxe deluxe = new PaqueteDeluxe("Meet&Greet + Merch");
                deluxe.agregarProducto(palcoPara4);
                deluxe.agregarProducto(pase);
                deluxe.agregarBeneficio(new Beneficio("Camiseta Oficial"));
                List<PaquetesTiquetes.Tiquete> ts = deluxe.generarTiquetes();
                System.out.println("Paquete Deluxe generado con tiquetes: " + ts.size());
                for (PaquetesTiquetes.Tiquete t : ts) ctx.c1.agregarTiquete(t);
                break;
            }
        }

        System.out.println("Tiquetes en la cuenta del comprador: " + ctx.c1.listarMisTiquetes().size());
        guardarJson(ctx, Collections.emptyList());
    }

    // --------- Helpers de entrada ---------
    private static String leerLinea(String prompt) {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    private static int leerEntero(String prompt, int def) {
        System.out.print(prompt);
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return def;
        }
    }

    private static void escenarioPaquetes() {
        System.out.println("-- Escenario: Paquetes --");
        Ctx ctx = setupBasico();

        PaqueteGrupo palcoPara4 = new PaqueteGrupo(ctx.evento, ctx.vip, 4, 600.0);
        PaseTemporada pase = new PaseTemporada(300.0);
        pase.agregarEntrada(ctx.evento, ctx.general);
        pase.agregarEntrada(ctx.evento, ctx.general);

        PaqueteDeluxe deluxe = new PaqueteDeluxe("Meet&Greet + Merch");
        deluxe.agregarProducto(palcoPara4);
        deluxe.agregarProducto(pase);
        deluxe.agregarBeneficio(new Beneficio("Camiseta Oficial"));

        List<Tiquete> ts = deluxe.generarTiquetes();
        System.out.println("Paquete Deluxe generado con tiquetes: " + ts.size());
        if (!ts.isEmpty()) {
            ctx.c1.agregarTiquete(ts.get(0));
            Comprador c2 = new Comprador("c-2", "c2", "pass2", "Cliente Dos", "c2@mail.com");
            boolean ok = ctx.c1.transferirTiquete(ts.get(0), c2, "pass1");
            System.out.println("Transferencia tiquete DELUXE: " + (ok ? "OK" : "BLOQUEADA"));
            guardarJson(ctx, Collections.singletonList(c2));
        } else {
            guardarJson(ctx, Collections.emptyList());
        }
    }

    private static void escenarioCancelacion() {
        System.out.println("-- Escenario: Cancelación --");
        Ctx ctx = setupBasico();
        ctx.admin.cancelarEventoPorFraude(ctx.evento);
        System.out.println(new ReporteTiquetera().generarResumen());

        guardarJson(ctx, Collections.emptyList());
    }

    private static void escenarioReporteTiquetera() {
        System.out.println("-- Escenario: Reporte Global --");
        Ctx ctx = setupBasico();
        // Realiza una compra mínima para que el reporte tenga datos
        List<Comprador.ItemCompra> items = Collections.singletonList(new Comprador.ItemCompra(ctx.evento, ctx.general, 1));
        ctx.c1.comprarTiquetes(items, "TARJETA", ctx.admin);
        System.out.println(new ReporteTiquetera().generarResumen());

        guardarJson(ctx, Collections.emptyList());
    }

    private static void escenarioReportePromotor() {
        System.out.println("-- Escenario: Reporte Promotor --");
        Ctx ctx = setupBasico();
        ReportePromotor rp = new ReportePromotor(ctx.evento);
        System.out.println(rp.generarResumen());

        guardarJson(ctx, Collections.emptyList());
    }

    private static void guardarJson(Ctx ctx, List<Comprador> extras) {
        if (!PERSISTIR_JSON) return; // no escribir JSON en carpeta data
        List<Actores.Usuario> usuarios = new ArrayList<>();
        usuarios.add(ctx.admin);
        usuarios.add(ctx.org);
        usuarios.add(ctx.c1);
        if (extras != null) usuarios.addAll(extras);

        List<Evento> eventos = ctx.org.getMisEventos();
        SnapshotPersistencia.guardarEstado("data", usuarios, eventos);
    }

    private static void ejecutarPruebas(String cual) {
        System.out.println("-- Demostracion de funcionalidades basicas --");
        if (cual == null) {
            Demos.runAll();
        } else {
            Demos.runOne(cual);
        }
    }
}