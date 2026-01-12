package org.example.util;

import org.example.dto.TransportDto;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileIoUtil {
    // Дефинираме името на папката
    private static final String EXPORT_DIR = "exports";

    public static void writeTransports(List<TransportDto> transports, String filename) {
        // 1. Създаваме папката, ако не съществува
        File directory = new File(EXPORT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // 2. Оформяме пълния път
        if (!filename.endsWith(".csv")) filename += ".csv";
        File file = new File(directory, filename);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Route,Price,Driver,Date\n");
            for (TransportDto t : transports) {
                writer.write(String.format("%s,%s-%s,%.2f,%s,%s\n",
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriverName(), t.getDepartureDate()));
            }
            System.out.println("Записано във файл: " + file.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAndPrintFile(String filename) {
        // Четем от папката exports
        Path filePath = Path.of(EXPORT_DIR, filename);

        System.out.println("\n--- FILE CONTENT: " + filename + " ---");
        try {
            List<String> lines = Files.readAllLines(filePath);
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("Файлът не е намерен в папка " + EXPORT_DIR + "!");
        }
        System.out.println("-----------------------------------");
    }

    public static List<String> listCsvFiles() {
        List<String> csvFiles = new ArrayList<>();
        File folder = new File(EXPORT_DIR);

        // Ако папката още не съществува, връщаме празен списък
        if (!folder.exists() || !folder.isDirectory()) {
            return csvFiles;
        }

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".csv")) {
                    csvFiles.add(file.getName());
                }
            }
        }
        return csvFiles;
    }
}