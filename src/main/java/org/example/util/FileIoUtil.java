package org.example.util;

import org.example.dto.TransportDto;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileIoUtil {

    public static void writeTransports(List<TransportDto> transports, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ID,Route,Price,Driver,Date\n");
            for (TransportDto t : transports) {
                writer.write(String.format("%d,%s-%s,%.2f,%s,%s\n",
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriverName(), t.getDepartureDate()));
            }
            System.out.println("Записано в: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAndPrintFile(String filename) {
        System.out.println("--- FILE CONTENT ---");
        try {
            List<String> lines = Files.readAllLines(Path.of(filename));
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.out.println("File not found!");
        }
        System.out.println("--------------------");
    }
}