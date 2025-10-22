package Persistencia;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Simple file writer ensuring parent directories exist. */
public final class Writer {
    private Writer() {}

    public static void writeToFile(String pathStr, String content) {
        try {
            Path path = Paths.get(pathStr);
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("[Persistencia] Error escribiendo archivo: " + pathStr + " - " + e.getMessage());
        }
    }
}

