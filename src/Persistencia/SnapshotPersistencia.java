package Persistencia;

import Actores.Usuario;
import Reportes.RegistroVentas;
import ZonasMaster.Evento;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Persists a snapshot of current state to JSON files under a target folder.
 * Files: usuarios.json, eventos.json, ventas.json
 */
public final class SnapshotPersistencia {
    private SnapshotPersistencia() {}

    public static void guardarEstado(String carpetaDestino, List<Usuario> usuarios, List<Evento> eventos) {
        if (carpetaDestino == null || carpetaDestino.trim().isEmpty()) carpetaDestino = "data";

        List<Usuario> us = usuarios != null ? usuarios : new ArrayList<>();
        List<Evento> evs = eventos != null ? eventos : new ArrayList<>();

        String usuariosJson = Serializadores.jsonArrayUsuarios(us);
        String eventosJson = Serializadores.jsonArrayEventos(evs);
        String ventasJson = Serializadores.jsonArrayVentas(RegistroVentas.listarVentas());

        String usuariosPath = Paths.get(carpetaDestino, "usuarios.json").toString();
        String eventosPath = Paths.get(carpetaDestino, "eventos.json").toString();
        String ventasPath = Paths.get(carpetaDestino, "ventas.json").toString();

        Writer.writeToFile(usuariosPath, usuariosJson);
        Writer.writeToFile(eventosPath, eventosJson);
        Writer.writeToFile(ventasPath, ventasJson);

        System.out.println("[Persistencia] Estado guardado en carpeta: " + carpetaDestino);
    }
}
