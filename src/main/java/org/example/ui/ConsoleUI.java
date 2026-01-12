package org.example.ui;

public class ConsoleUI {
    public void start() {
        while (true) {
            System.out.println("\n=== ТРАНСПОРТНА КОМПАНИЯ ===");
            System.out.println("1. Компании");
            System.out.println("2. Служители");
            System.out.println("3. Клиенти");
            System.out.println("4. МПС");
            System.out.println("5. Превози");
            System.out.println("6. Справки и Файлове");
            System.out.println("0. Изход");

            String choice = InputUtil.readString(">> ");

            switch (choice) {
                case "1": new CompanyMenu().show(); break;
                case "2": new EmployeeMenu().show(); break;
                case "3": new ClientMenu().show(); break;
                case "4": new VehicleMenu().show(); break;
                case "5": new TransportMenu().show(); break;
                case "6": new ReportMenu().show(); break;
                case "0": return;
                default: System.out.println("Невалиден избор!");
            }
        }
    }
}