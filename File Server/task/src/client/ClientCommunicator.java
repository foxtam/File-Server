package client;

import core.Codes;
import core.IdentificationFileType;
import core.RequestType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class ClientCommunicator {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Path storagePath =
            Path.of(System.getProperty("user.dir"), "src", "client", "data");

    static {
        try {
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final DataInputStream input;
    private final DataOutputStream output;

    public ClientCommunicator(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void run() throws IOException {
        System.out.print("Enter action (1 - get a file, 2 - save a file, 3 - delete a file): ");
        String input = scanner.nextLine();
        if (input.equals(RequestType.EXIT.toLowerCase())) {
            exit();
            return;
        }

        int answer = Integer.parseInt(input);
        if (answer == 1) {
            getFile();
        } else if (answer == 2) {
            saveFile();
        } else if (answer == 3) {
            deleteFile();
        }
    }

    private void exit() throws IOException {
        output.writeUTF(RequestType.EXIT);
        System.out.println("The request was sent.");
    }

    private void getFile() throws IOException {
        System.out.print("Do you want to get the file by name or by id (1 - name, 2 - id): ");
        int identificationType = Integer.parseInt(scanner.nextLine());
        output.writeUTF(RequestType.GET);
        sendFileIdentification(identificationType);
        System.out.println("The request was sent.");

        int response = input.readInt();
        if (response == Codes.OK_CODE) {
            System.out.print("The file was downloaded! Specify a name for it: ");
            String fileName = scanner.nextLine();
            writeInputToFile(fileName);
            System.out.println("File saved on the hard drive!");
        } else if (response == Codes.NO_FILE_CODE) {
            System.out.println("The response says that this file is not found!");
        } else {
            throw new IllegalStateException("" + response);
        }
    }

    private void saveFile() throws IOException {
        System.out.print("Enter name of the file: ");
        String localFileName = scanner.nextLine();

        System.out.print("Enter name of the file to be saved on server: ");
        String remoteFileName = scanner.nextLine();

        if (remoteFileName.isBlank()) {
            remoteFileName = localFileName;
        }

        send(localFileName, remoteFileName);
        System.out.println("The request was sent.");
        printSaveFileResponse();
    }

        var response = new Response(input.readUTF());
        if (response.code() == Response.OK_CODE) {
            System.out.println("The response says that file was created!");
        } else if (response.code() == Response.FILE_ALREADY_EXISTS_CODE) {
            System.out.println("The response says that creating the file was forbidden!");
        } else {
            throw new IllegalStateException(response.toString());
        }
    }

    private void deleteFile() throws IOException {
        System.out.print("Enter filename: ");
        String fileName = scanner.nextLine();

        output.writeUTF("DEL " + fileName);
        System.out.println("The request was sent.");

        var response = new Response(input.readUTF());
        if (response.code() == Response.OK_CODE) {
            System.out.println("The response says that the file was successfully deleted!");
        } else if (response.code() == Response.NO_FILE_CODE) {
            System.out.println("The response says that the file was not found!");
        } else {
            throw new IllegalStateException(response.toString());
        }
    }
}
