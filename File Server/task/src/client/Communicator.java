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
            switch (answer) {
                case 1:
                    getFile();
                    break;
                case 2:
                    createFile();
                    break;
                case 3:
                    deleteFile();
                    break;
                case 4:
                    return;
            }
        }
    }

    private void getFile() throws IOException {
        System.out.print("Enter filename: ");
        String fileName = scanner.nextLine();

        output.writeUTF("GET " + fileName);
        System.out.println("The request was sent.");

        Response response = new Response(input.readUTF());
        if (response.code() == Response.GOOD_CODE) {
            System.out.println("The content of the file is: " + response.content());
        } else if (response.code() == Response.NO_FILE_CODE) {
            System.out.println("The response says that the file was not found!");
        }
    }
}
