package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class IdFileCatalog {
    private static final String catalogFileName = "_catalog_";
    private static final Map<Path, IdFileCatalog> cache = new HashMap<>();
    private final Path catalogFilePath;

    private IdFileCatalog(Path directory) throws IOException {
        this.catalogFilePath = directory.resolve(catalogFileName);
        createFileIfAbsent();
    }

    private void createFileIfAbsent() throws IOException {
        if (!Files.exists(catalogFilePath)) {
            try (var oos = new ObjectOutputStream(Files.newOutputStream(catalogFilePath))) {
                oos.writeObject(new HashMap<Integer, String>());
            }
        }
    }

    public static IdFileCatalog of(Path directory) throws IOException {
        IdFileCatalog catalog = cache.get(directory);
        if (catalog == null) {
            IdFileCatalog newCatalog = new IdFileCatalog(directory);
            cache.put(directory, newCatalog);
            return newCatalog;
        } else {
            return catalog;
        }
    }

    public synchronized AddFileResult addFile(String fileName) throws IOException {
        Map<Integer, String> catalog = readCatalog();
        int id = getNextId(catalog);
        catalog.put(id, fileName);
        writeCatalog(catalog);
        return new AddFileResult(id);
    }

    private Map<Integer, String> readCatalog() throws IOException {
        try (var ois = new ObjectInputStream(Files.newInputStream(catalogFilePath))) {
            //noinspection unchecked
            return (HashMap<Integer, String>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNextId(Map<Integer, String> catalog) {
        if (catalog.isEmpty()) return 1;
        return Collections.max(catalog.keySet()) + 1;
    }

    private void writeCatalog(Map<Integer, String> catalog) throws IOException {
        try (var oos = new ObjectOutputStream(Files.newOutputStream(catalogFilePath))) {
            oos.writeObject(catalog);
        }
    }

    public synchronized Optional<String> getFileName(int id) throws IOException {
        Map<Integer, String> catalog = readCatalog();
        return Optional.ofNullable(catalog.get(id));
    }

    public synchronized void deleteFileName(String fileName) throws IOException {
        Map<Integer, String> catalog = readCatalog();
        int id = findIDByFileName(fileName, catalog);
        catalog.remove(id);
        writeCatalog(catalog);
    }

    private int findIDByFileName(String fileName, Map<Integer, String> catalog) {
        for (Map.Entry<Integer, String> entry : catalog.entrySet()) {
            if (entry.getValue().equals(fileName)) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException();
    }

    public static class AddFileResult {
        private final int id;

        public AddFileResult(int id) {
            this.id = id;
        }

        public int takeId() {
            return id;
        }
    }
}
