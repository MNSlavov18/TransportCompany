package org.example;

import org.example.ui.ConsoleUI;
import org.example.util.DataSeeder;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        System.out.println("\n=== ИЗБОР НА РЕЖИМ ===");
        System.out.println("1. Нормален режим (Ръчно управление)");
        System.out.println("2. Автоматична демонстрация (Сценарий)");
        System.out.println("3. Зареждане на тестови данни (Първоначално пълнене)");
        System.out.print(">> Въведете избор: ");

        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();

        if (mode.equals("2")) {
            AutomatedDemo.run();
        } else if (mode.equals("3")) {
            DataSeeder.seed();
            System.out.println("\n[ИНФО] Превключване към нормален режим...");
            new ConsoleUI().start();
        } else {
            new ConsoleUI().start();
        }
    }
}