package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class FileServer {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Set<String> existFileNames = getFileNames();
    private final Set<String> filesStorage = new HashSet<>();

    private static Set<String> getFileNames() {
        Set<String> files = new HashSet<>();
        for (int i = 1; i <= 10; i++) {
            files.add("file" + i);
        }
        return files;
    }

    public void run() {
        while (true) {
            String[] input = scanner.nextLine().split("\\s+");
            String command = input[0];
            if (command.equals("exit")) break;
            executeCommand(command, input[1]);
        }
    }

    private void executeCommand(String command, String fileName) {
        switch (command) {
            case "add":
                addFile(fileName);
                break;
            case "get":
                getFile(fileName);
                break;
            case "delete":
                deleteFile(fileName);
                break;
        }
    }

    private void addFile(String fileName) {
        if (existFileNames.contains(fileName) && filesStorage.add(fileName)) {
            System.out.printf("The file %s added successfully%n", fileName);
        } else {
            System.out.printf("Cannot add the file %s%n", fileName);
        }
    }

    private void getFile(String fileName) {
        if (filesStorage.contains(fileName)) {
            System.out.printf("The file %s was sent%n", fileName);
        } else {
            System.out.printf("The file %s not found%n", fileName);
        }
    }

    private void deleteFile(String fileName) {
        if (filesStorage.remove(fileName)) {
            System.out.printf("The file %s was deleted%n", fileName);
        } else {
            System.out.printf("The file %s not found%n", fileName);
        }
    }
}
