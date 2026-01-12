package org.example.ui;

import org.example.dao.*;
import org.example.entity.*;
import org.example.service.TransportService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class TransportMenu implements MenuHandler {
    private final TransportService transportService = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- 📦 ПРЕВОЗИ ---");
            System.out.println("1. Списък");
            System.out.println("2. Нов превоз");
            System.out.println("3. Редакция / Плащане");
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
            t.setStartPoint(InputUtil.readString("От: "));
            t.setEndPoint(InputUtil.readString("До: "));
            t.setPrice(new BigDecimal(InputUtil.readString("Цена: ")));
            t.setDepartureDate(LocalDate.now());

            System.out.println("--- Вид Товар ---");
            System.out.println("1. Пътници\n2. Животни\n3. Горива\n4. Чупливи\n5. Общ");
            int typeIdx = Integer.parseInt(InputUtil.readString("Избор: ")) - 1;
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

            // Филтриране чрез Service
            List<Vehicle> compatibleVehicles = VehicleDao.getAll().stream()
                    .filter(v -> v.getCompany().getId().equals(comp.getId()))
                    .filter(v -> transportService.isVehicleCompatible(v, cargoType))
                    .filter(v -> transportService.hasCapacity(v, t))
                    .collect(Collectors.toList());

            Vehicle vehicle = InputUtil.selectFromList(compatibleVehicles, "подходящо МПС");
            if (vehicle == null) return;
            t.setVehicle(vehicle);

            List<Employee> compatibleDrivers = EmployeeDao.getAll().stream()
                    .filter(e -> e.getCompany().getId().equals(comp.getId()))
                    .filter(e -> transportService.isDriverCompatible(e, cargoType))
                    .collect(Collectors.toList());

            Employee driver = InputUtil.selectFromList(compatibleDrivers, "шофьор");
            if (driver == null) return;
            t.setDriver(driver);

            Client client = InputUtil.selectFromList(ClientDao.getAll(), "клиент");
            if (client == null) return;
            t.setClient(client);

            // ВИКАНЕ НА СЪРВИСА
            transportService.createTransport(t);
            System.out.println("✅ Успешно създаден превоз!");

        } catch (Exception e) {
            System.out.println("❌ Грешка: " + e.getMessage());
        }
    }

    private void editTransport() {
        Transport t = InputUtil.selectFromList(TransportDao.getAll(), "превоз");
        if (t == null) return;
        System.out.println("1. Плати\n2. Промени Цена");
        String ch = InputUtil.readString(">> ");
        if (ch.equals("1")) t.setPaid(true);
        else if (ch.equals("2")) t.setPrice(new BigDecimal(InputUtil.readString("Нова цена: ")));
        TransportDao.update(t);
    }

    private void markArrived() {
        List<Transport> active = TransportDao.getAll().stream().filter(t -> t.getArrivalDate() == null).collect(Collectors.toList());
        Transport t = InputUtil.selectFromList(active, "активен курс");
        if (t != null) { t.setArrivalDate(LocalDate.now()); TransportDao.update(t); }
    }

    private void deleteTransport() {
        Transport t = InputUtil.selectFromList(TransportDao.getAll(), "превоз");
        if (t != null) TransportDao.delete(t.getId());
    }
}