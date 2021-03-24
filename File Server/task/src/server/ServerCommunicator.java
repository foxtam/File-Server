package server;

import core.Codes;
import core.IdentificationFileType;
import core.RequestType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class ServerCommunicator {
    public static Path storagePath =
            Path.of(System.getProperty("user.dir"), "src", "server", "data");

    static {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DataInputStream input;
    private final DataOutputStream output;
    private final IdFileCatalog idFileCatalog = IdFileCatalog.of(storagePath);
    private final Runnable onExit;

    public ServerCommunicator(Socket socket, Runnable onExit) throws IOException {
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
        this.onExit = onExit;
    }

    public void run() throws IOException {
        String requestType = input.readUTF();
        switch (requestType) {
            case RequestType.EXIT:
                onExit.run();
                break;
            case RequestType.PUT:
                saveFile();
                break;
            case RequestType.GET:
                sendFile();
                break;
            case RequestType.DEL:
                deleteFile();
                break;
            default:
                throw new IllegalStateException(requestType);
        }
    }

    private void saveFile() throws IOException {
        String fileName = input.readUTF();
        Path filePath = storagePath.resolve(fileName);
        if (Files.exists(filePath)) {
            output.writeInt(Codes.FILE_ALREADY_EXISTS_CODE);
        } else {
            writeInputTo(filePath);
            int fileId = idFileCatalog.addFile(fileName).takeId();
            output.writeInt(Codes.OK_CODE);
            output.writeInt(fileId);
        }
    }

    private void sendFile() throws IOException {
        Optional<String> fileNameOptional = getFileName();
        if (fileNameOptional.isPresent()) {
            byte[] content = Files.readAllBytes(storagePath.resolve(fileNameOptional.get()));
            output.writeInt(Codes.OK_CODE);
            output.writeInt(content.length);
            output.write(content);
        } else {
            output.writeInt(Codes.NO_FILE_CODE);
        }
    }

    private void deleteFile() throws IOException {
        Optional<String> fileNameOptional = getFileName();
        if (fileNameOptional.isPresent()) {
            Files.delete(storagePath.resolve(fileNameOptional.get()));
            idFileCatalog.deleteFileName(fileNameOptional.get());
            output.writeInt(Codes.OK_CODE);
        } else {
            output.writeInt(Codes.NO_FILE_CODE);
        }
    }

    private void writeInputTo(Path filePath) throws IOException {
        int fileLength = input.readInt();
        byte[] content = new byte[fileLength];
        input.readFully(content);
        Files.createFile(filePath);
        Files.write(filePath, content);
    }

    private Optional<String> getFileName() throws IOException {
        String idFileType = input.readUTF();
        switch (idFileType) {
            case IdentificationFileType.BY_ID:
                int id = input.readInt();
                return idFileCatalog.getFileName(id);
            case IdentificationFileType.BY_NAME:
                String fileName = input.readUTF();
                if (Files.exists(storagePath.resolve(fileName))) {
                    return Optional.of(fileName);
                } else {
                    return Optional.empty();
                }
            default:
                throw new IllegalStateException(idFileType);
        }
    }
}
