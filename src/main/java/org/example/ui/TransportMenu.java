package org.example.ui;

import org.example.dao.*;
import org.example.entity.*;
import org.example.service.TransportService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransportMenu implements MenuHandler {
    private final TransportService service = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- УПРАВЛЕНИЕ НА ПРЕВОЗИ ---");
            System.out.println("1. Списък");
            System.out.println("2. Създай нов превоз");
            System.out.println("3. Редакция");
            System.out.println("4. Отбележи като ПРИСТИГНАЛ");
            System.out.println("5. Изтриване");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1": TransportDao.getAll().forEach(System.out::println); break;
                case "2": createTransport(); break;
                case "3": editTransport(); break;
                case "4": markArrived(); break;
                case "5": deleteTransport(); break;
            }
        }
    }

    private void createTransport() {
        try {
            Transport t = new Transport();
            t.setStartPoint(InputUtil.readString("Начална точка: "));
            t.setEndPoint(InputUtil.readString("Крайна точка: "));
            t.setPrice(new BigDecimal(InputUtil.readString("Цена: ")));
            t.setDepartureDate(LocalDate.now());

            System.out.println("--- Вид Товар ---");
            System.out.println("1. Пътници (Хора)");
            System.out.println("2. Животни");
            System.out.println("3. Горива (Опасни)");
            System.out.println("4. Чупливи стоки");
            System.out.println("5. Общ товар");

            int typeIdx = Integer.parseInt(InputUtil.readString("Избор (1-5): ")) - 1;
            CargoType cargoType = CargoType.values()[typeIdx];
            t.setCargoType(cargoType);

            if (cargoType == CargoType.PASSENGERS) {
                t.setPassengerCount(Integer.parseInt(InputUtil.readString("Брой пътници: ")));
            } else {
                t.setCargoWeight(Double.parseDouble(InputUtil.readString("Тегло (кг): ")));
            }

            Company comp = InputUtil.selectFromList(CompanyDao.getAll(), "компания превозвач");
            if (comp == null) return;
            t.setCompany(comp);

            List<Vehicle> compatibleVehicles = VehicleDao.getAll().stream()
                    .filter(v -> v.getCompany().getId().equals(comp.getId()))
                    .filter(v -> service.isVehicleCompatible(v, cargoType))
                    .filter(v -> service.hasCapacity(v, t))
                    .collect(Collectors.toList());

            Vehicle vehicle = InputUtil.selectFromList(compatibleVehicles, "подходящо МПС");
            if (vehicle == null) {
                System.out.println("Грешка: Няма подходящо МПС в тази фирма!");
                return;
            }
            t.setVehicle(vehicle);

            List<Employee> compatibleDrivers = EmployeeDao.getAll().stream()
                    .filter(e -> e.getCompany().getId().equals(comp.getId()))
                    .filter(e -> service.isDriverCompatible(e, cargoType))
                    .collect(Collectors.toList());

            Employee driver = InputUtil.selectFromList(compatibleDrivers, "квалифициран шофьор");
            if (driver == null) {
                System.out.println("Грешка: Няма квалифициран шофьор!");
                return;
            }
            t.setDriver(driver);

            Client client = InputUtil.selectFromList(ClientDao.getAll(), "клиент");
            if (client == null) return;
            t.setClient(client);

            service.createTransport(t);
            System.out.println("Успешно създаден превоз!");

        } catch (Exception e) {
            System.out.println("Грешка: " + e.getMessage());
        }
    }

    private void editTransport() {
        Transport t = InputUtil.selectFromList(TransportDao.getAll(), "превоз");
        if (t == null) return;

        System.out.println("Какво ще редактирате?");
        System.out.println("1. Маршрут (Начало/Край)");
        System.out.println("2. Цена");
        System.out.println("3. Статус на плащане");
        System.out.println("4. Смяна на шофьор");

        String ch = InputUtil.readString(">> ");

        try {
            switch (ch) {
                case "1":
                    t.setStartPoint(InputUtil.readString("Нова начална точка: "));
                    t.setEndPoint(InputUtil.readString("Нова крайна точка: "));
                    break;
                case "2":
                    t.setPrice(new BigDecimal(InputUtil.readString("Нова цена: ")));
                    break;
                case "3":
                    System.out.println("1. Платено | 2. Неплатено");
                    String p = InputUtil.readString(">> ");
                    t.setPaid(p.equals("1"));
                    break;
                case "4":
                    List<Employee> drivers = EmployeeDao.getAll().stream()
                        .filter(e -> e.getCompany().getId().equals(t.getCompany().getId()))
                        .filter(e -> service.isDriverCompatible(e, t.getCargoType()))
                        .collect(Collectors.toList());

                    Employee newDriver = InputUtil.selectFromList(drivers, "нов шофьор");
                    if (newDriver != null) t.setDriver(newDriver);
                    break;
                default:
                    System.out.println("Невалиден избор.");
                    return;
            }
            TransportDao.update(t);
            System.out.println("Превозът е обновен успешно.");
        } catch (Exception e) {
            System.out.println("Грешка при редакция: " + e.getMessage());
        }
    }

    private void markArrived() {
        List<Transport> active = TransportDao.getAll().stream()
                .filter(t -> t.getArrivalDate() == null)
                .collect(Collectors.toList());

        Transport t = InputUtil.selectFromList(active, "активен курс");
        if (t != null) {
            t.setArrivalDate(LocalDate.now());
            TransportDao.update(t);
            System.out.println("Отбелязан като пристигнал!");
        }
    }

    private void deleteTransport() {
        Transport t = InputUtil.selectFromList(TransportDao.getAll(), "превоз");
        if (t != null) {
            try {
                TransportDao.delete(t.getId());
                System.out.println("Превозът е изтрит успешно.");
            } catch (Exception e) {
                System.out.println("Грешка при изтриване: " + e.getMessage());
            }
        }
    }
}