package org.example.ui;

import org.example.dao.*;
import org.example.dto.TransportDto;
import org.example.entity.*;
import org.example.util.FileIoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;

    public ConsoleUI() {
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        // Инициализиране на данни при старт
        initData();

        while (true) {
            System.out.println("\n=========================================");
            System.out.println("       ТРАНСПОРТНА КОМПАНИЯ (SOLID)");
            System.out.println("=========================================");
            System.out.println("1. 🏢 Управление на КОМПАНИИ");
            System.out.println("2. 👨‍💼 Управление на СЛУЖИТЕЛИ");
            System.out.println("3. 👤 Управление на КЛИЕНТИ");
            System.out.println("4. 🚛 Управление на МПС");
            System.out.println("5. 📦 Управление на ПРЕВОЗИ");
            System.out.println("6. 📊 СПРАВКИ и ФАЙЛОВЕ");
            System.out.println("0. 🚪 ИЗХОД");
            System.out.print(">> Изберете опция: ");

            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": menuCompany(); break;
                    case "2": menuEmployee(); break;
                    case "3": menuClient(); break;
                    case "4": menuVehicle(); break;
                    case "5": menuTransport(); break;
                    case "6": menuReports(); break;
                    case "0":
                        System.out.println("Довиждане!");
                        return;
                    default:
                        System.out.println("⚠ Невалиден избор!");
                }
            } catch (Exception e) {
                System.out.println("❌ ГРЕШКА: " + e.getMessage());
            }
        }
    }

    // --- ПОД-МЕНЮТА ---

    private void menuCompany() {
        System.out.println("\n--- 🏢 КОМПАНИИ ---");
        System.out.println("1. Списък");
        System.out.println("2. Добавяне");
        System.out.println("3. Редакция");
        System.out.println("4. Изтриване");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": CompanyDao.getAll().forEach(System.out::println); break;
            case "2":
                Company c = new Company();
                System.out.print("Име: "); c.setName(scanner.nextLine());
                CompanyDao.save(c);
                System.out.println("✅ Записано.");
                break;
            case "3":
                System.out.print("ID: ");
                Company editC = CompanyDao.getById(Long.parseLong(scanner.nextLine()));
                if(editC != null) {
                    System.out.print("Ново име ("+editC.getName()+"): ");
                    editC.setName(scanner.nextLine());
                    CompanyDao.update(editC);
                    System.out.println("✅ Обновено.");
                }
                break;
            case "4":
                System.out.print("ID: ");
                CompanyDao.delete(Long.parseLong(scanner.nextLine()));
                System.out.println("✅ Изтрито.");
                break;
        }
    }

    private void menuEmployee() {
        System.out.println("\n--- 👨‍💼 СЛУЖИТЕЛИ ---");
        System.out.println("1. Списък");
        System.out.println("2. Наемане");
        System.out.println("3. Редакция");
        System.out.println("4. Уволнение");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": EmployeeDao.getAll().forEach(System.out::println); break;
            case "2":
                Employee e = new Employee();
                System.out.print("Име: "); e.setName(scanner.nextLine());
                System.out.print("Заплата: "); e.setSalary(new BigDecimal(scanner.nextLine()));
                System.out.println("Квалификация (1.FLAMMABLE, 2.PASSENGER, 3.HEAVY, 4.GENERAL): ");
                int q = Integer.parseInt(scanner.nextLine());
                e.setQualification(Qualification.values()[q-1]);
                System.out.print("ID на Компания: ");
                e.setCompany(CompanyDao.getById(Long.parseLong(scanner.nextLine())));
                EmployeeDao.save(e);
                System.out.println("✅ Нает.");
                break;
            case "3":
                System.out.print("ID: ");
                Employee editE = EmployeeDao.getById(Long.parseLong(scanner.nextLine()));
                if(editE != null) {
                    System.out.print("Ново име: "); editE.setName(scanner.nextLine());
                    EmployeeDao.update(editE);
                    System.out.println("✅ Обновено.");
                }
                break;
            case "4":
                System.out.print("ID: ");
                EmployeeDao.delete(Long.parseLong(scanner.nextLine()));
                System.out.println("✅ Уволнен.");
                break;
        }
    }

    private void menuClient() {
        System.out.println("\n--- 👤 КЛИЕНТИ ---");
        System.out.println("1. Списък");
        System.out.println("2. Добавяне");
        System.out.println("3. Изтриване");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": ClientDao.getAll().forEach(System.out::println); break;
            case "2":
                Client cl = new Client();
                System.out.print("Име: "); cl.setName(scanner.nextLine());
                ClientDao.save(cl);
                System.out.println("✅ Добавен.");
                break;
            case "3":
                System.out.print("ID: ");
                ClientDao.delete(Long.parseLong(scanner.nextLine()));
                System.out.println("✅ Изтрит.");
                break;
        }
    }

    private void menuVehicle() {
        System.out.println("\n--- 🚛 МПС ---");
        System.out.println("1. Списък");
        System.out.println("2. Добавяне");
        System.out.println("3. Изтриване");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": VehicleDao.getAll().forEach(System.out::println); break;
            case "2":
                Vehicle v = new Vehicle();
                System.out.println("Тип (1.BUS, 2.TRUCK, 3.TANKER): ");
                int t = Integer.parseInt(scanner.nextLine());
                v.setType(VehicleType.values()[t-1]);
                System.out.print("Рег. номер: "); v.setLicensePlate(scanner.nextLine());
                System.out.print("ID на Компания: ");
                v.setCompany(CompanyDao.getById(Long.parseLong(scanner.nextLine())));
                VehicleDao.save(v);
                System.out.println("✅ Добавено.");
                break;
            case "3":
                System.out.print("ID: ");
                VehicleDao.delete(Long.parseLong(scanner.nextLine()));
                System.out.println("✅ Изтрито.");
                break;
        }
    }

    private void menuTransport() {
        System.out.println("\n--- 📦 ПРЕВОЗИ ---");
        System.out.println("1. Списък");
        System.out.println("2. Нов превоз");
        System.out.println("3. Плащане на превоз (т.6)");
        System.out.println("4. Изтриване");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": TransportDao.getAll().forEach(System.out::println); break;
            case "2":
                Transport tr = new Transport();
                System.out.print("От: "); tr.setStartPoint(scanner.nextLine());
                System.out.print("До: "); tr.setEndPoint(scanner.nextLine());
                System.out.print("Цена: "); tr.setPrice(new BigDecimal(scanner.nextLine()));
                tr.setDepartureDate(LocalDate.now());

                System.out.print("ID Компания: "); tr.setCompany(CompanyDao.getById(Long.parseLong(scanner.nextLine())));
                System.out.print("ID Шофьор: "); tr.setDriver(EmployeeDao.getById(Long.parseLong(scanner.nextLine())));
                System.out.print("ID Клиент: "); tr.setClient(ClientDao.getById(Long.parseLong(scanner.nextLine())));

                TransportDao.save(tr);
                System.out.println("✅ Регистриран.");
                break;
            case "3":
                System.out.print("ID на превоз: ");
                Transport existing = TransportDao.getById(Long.parseLong(scanner.nextLine()));
                if(existing != null) {
                    System.out.print("Платено? (true/false): ");
                    existing.setPaid(Boolean.parseBoolean(scanner.nextLine()));
                    TransportDao.update(existing);
                    System.out.println("✅ Статус променен.");
                }
                break;
            case "4":
                System.out.print("ID: ");
                TransportDao.delete(Long.parseLong(scanner.nextLine()));
                System.out.println("✅ Изтрит.");
                break;
        }
    }

    private void menuReports() {
        System.out.println("\n--- 📊 СПРАВКИ ---");
        System.out.println("1. Сортиране Компании (Име/Приход)");
        System.out.println("2. Сортиране Служители (Квал./Заплата)");
        System.out.println("3. Сортиране Превози (Дестинация)");
        System.out.println("4. Статистика (Брой/Суми/Шофьори)");
        System.out.println("5. Приходи на компания за период");
        System.out.println("6. Експорт във файл");
        System.out.println("7. Четене от файл");
        System.out.print(">> ");
        String ch = scanner.nextLine();

        switch (ch) {
            case "1": CompanyDao.getSorted(true).forEach(System.out::println); break;
            case "2": EmployeeDao.getSorted(true).forEach(System.out::println); break;
            case "3": TransportDao.getSortedByDestination().forEach(System.out::println); break;
            case "4": TransportDao.printStats(); break;
            case "5":
                System.out.print("ID Компания: ");
                long cid = Long.parseLong(scanner.nextLine());
                BigDecimal rev = CompanyDao.getRevenueForPeriod(cid, LocalDate.now().minusMonths(12), LocalDate.now());
                System.out.println("💰 Приходи: " + rev);
                break;
            case "6":
                FileIoUtil.writeTransports(TransportDao.getSortedByDestination(), "transports.csv");
                break;
            case "7":
                FileIoUtil.readAndPrintFile("transports.csv");
                break;
        }
    }

    private void initData() {
        if (!CompanyDao.getAll().isEmpty()) return;

        System.out.println("Generating Init Data...");
        Company c = new Company(); c.setName("Speedy"); CompanyDao.save(c);
        Employee e = new Employee(); e.setName("Ivan"); e.setSalary(new BigDecimal("2000")); e.setQualification(Qualification.HEAVY_LOAD); e.setCompany(c); EmployeeDao.save(e);
        Client cl = new Client(); cl.setName("Client A"); ClientDao.save(cl);
        Transport t = new Transport(); t.setStartPoint("Sofia"); t.setEndPoint("Varna"); t.setPrice(new BigDecimal("200")); t.setCompany(c); t.setDriver(e); t.setClient(cl); t.setDepartureDate(LocalDate.now()); TransportDao.save(t);
    }
}