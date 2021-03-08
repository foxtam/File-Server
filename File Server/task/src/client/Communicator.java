package client;

import core.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Communicator {
    private static final Scanner scanner = new Scanner(System.in);

    private final DataInputStream input;
    private final DataOutputStream output;

    public Communicator(DataInputStream input, DataOutputStream output) {
        this.input = input;
        this.output = output;
    }

    public void run() throws IOException {
        while (true) {
            System.out.print("Enter action (1 - get a file, 2 - create a file, 3 - delete a file, 4 - exit): ");
            int answer = Integer.parseInt(scanner.nextLine());
            if (answer == 1) {
                getFile();
            } else if (answer == 2) {
                createFile();
            } else if (answer == 3) {
                deleteFile();
            } else if (answer == 4) {
                return;
            }
        }
    }

    private void getFile() throws IOException {
        System.out.print("Enter filename: ");
        String fileName = scanner.nextLine();

        output.writeUTF("GET " + fileName);
        System.out.println("The request was sent.");

        var response = new Response(input.readUTF());
        if (response.code() == Response.OK_CODE) {
            System.out.println("The content of the file is: " + response.content());
        } else if (response.code() == Response.NO_FILE_CODE) {
            System.out.println("The response says that the file was not found!");
        } else {
            throw new IllegalStateException(response.toString());
        }
    }

    private void createFile() throws IOException {
        System.out.print("Enter filename: ");
        String fileName = scanner.nextLine();

        System.out.print("Enter file content: ");
        String content = scanner.nextLine();

        output.writeUTF("PUT " + fileName + " " + content);
        System.out.println("The request was sent.");

        var response = new Response(input.readUTF());
        if (response.code() == Response.OK_CODE) {
            System.out.println("The response says that file was created!");
        } else if (response.code() == Response.FILE_ALREADY_EXISTS_CODE) {
            System.out.println("The response says that creating the file was forbidden!");
        } else {
            throw new IllegalStateException(response.toString());
        }
    }
}