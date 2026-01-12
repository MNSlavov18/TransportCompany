package org.example.ui;

import java.util.List;
import java.util.Scanner;

public class InputUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public static <T> T selectFromList(List<T> items, String entityName) {
        if (items.isEmpty()) {
            System.out.println("Няма налични " + entityName + ".");
            return null;
        }

        System.out.println("\n--- Избери " + entityName + " ---");
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s%n", (i + 1), items.get(i).toString());
        }
        System.out.print("Въведи номер (0 за отказ): ");

        try {
            String input = scanner.nextLine();
            if (input.equals("0")) return null;

            int index = Integer.parseInt(input) - 1;
            if (index >= 0 && index < items.size()) {
                return items.get(index);
            } else {
                System.out.println("Невалиден номер.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Моля въведете число.");
        }
        return null;
    }
}