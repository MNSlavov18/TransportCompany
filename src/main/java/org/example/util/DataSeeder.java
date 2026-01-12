package org.example.util;

import org.example.dao.*;
import org.example.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

public class DataSeeder {
    public static void seed() {
        if (!CompanyDao.getAll().isEmpty()) {
            System.out.println("[ИНФО] Базата данни вече съдържа записи.");
            return;
        }
        System.out.println("[ИНФО] Започва зареждане на тестови данни...");

        // Компании
        Company dhl = createCompany("DHL Express", 50000);
        Company econt = createCompany("Econt Trans", 12000);
        Company union = createCompany("Union Ivkoni", 85000);

        // Служители
        Employee dhlDriver1 = createEmployee("Петър Петров", Qualification.HEAVY_LOAD, 3000, dhl);
        Employee dhlDriver2 = createEmployee("Георги Георгиев", Qualification.FLAMMABLE, 3500, dhl);
        Employee econtDriver1 = createEmployee("Иван Иванов", Qualification.GENERAL, 1800, econt);
        Employee busDriver1 = createEmployee("Димитър Димитров", Qualification.PASSENGER, 2500, union);
        Employee busDriver2 = createEmployee("Стоян Стоянов", Qualification.PASSENGER, 2400, union);

        // МПС
        Vehicle truck1 = createVehicle(VehicleType.TRUCK, "CA 1111 TT", 20000.0, dhl);
        Vehicle tanker1 = createVehicle(VehicleType.TANKER, "CA 2222 FL", 15000.0, dhl);
        Vehicle van1 = createVehicle(VehicleType.VAN, "CB 3333 VV", 3000.0, econt);
        Vehicle bus1 = createVehicle(VehicleType.BUS, "PB 4444 AA", 50.0, union);
        Vehicle bus2 = createVehicle(VehicleType.BUS, "PB 5555 BB", 30.0, union);

        // Клиенти
        Client amazon = createClient("Amazon EU", true);
        Client lidl = createClient("Lidl Bulgaria", true);
        Client shell = createClient("Shell", true);
        Client privateClient = createClient("Частно лице", false);

        // Превози
        createTransport("София", "Берлин", 5000, CargoType.GENERAL, 5000.0, null, dhl, dhlDriver1, truck1, amazon, true, true);
        createTransport("Бургас", "София", 1500, CargoType.FUEL, 10000.0, null, dhl, dhlDriver2, tanker1, shell, true, false);
        createTransport("София", "Истанбул", 3000, CargoType.PASSENGERS, null, 45, union, busDriver1, bus1, privateClient, false, true);
        createTransport("Варна", "Русе", 400, CargoType.FRAGILE, 500.0, null, econt, econtDriver1, van1, lidl, true, false);
        createTransport("Пловдив", "Атина", 4000, CargoType.PASSENGERS, null, 25, union, busDriver2, bus2, privateClient, true, true);
        createTransport("Мюнхен", "София", 6000, CargoType.GENERAL, 18000.0, null, dhl, dhlDriver1, truck1, amazon, true, true);

        System.out.println("[ОК] Готово! Базата е заредена успешно.");
    }

    // Хелпърите остават същите, само логовете се сменят
    private static Company createCompany(String name, double revenue) {
        Company c = new Company(); c.setName(name); c.setRevenue(BigDecimal.valueOf(revenue)); CompanyDao.save(c); return c;
    }
    private static Employee createEmployee(String name, Qualification q, double salary, Company c) {
        Employee e = new Employee(); e.setName(name); e.setQualification(q); e.setSalary(BigDecimal.valueOf(salary)); e.setCompany(c); EmployeeDao.save(e); return e;
    }
    private static Vehicle createVehicle(VehicleType type, String plate, double capacity, Company c) {
        Vehicle v = new Vehicle(); v.setType(type); v.setLicensePlate(plate); v.setCapacity(capacity); v.setCompany(c); VehicleDao.save(v); return v;
    }
    private static Client createClient(String name, boolean paid) {
        Client c = new Client(); c.setName(name); c.setHasPaidObligations(paid); ClientDao.save(c); return c;
    }
    private static void createTransport(String from, String to, double price, CargoType type, Double weight, Integer passengers, Company comp, Employee driver, Vehicle vehicle, Client client, boolean isPaid, boolean isArrived) {
        Transport t = new Transport(); t.setStartPoint(from); t.setEndPoint(to); t.setPrice(BigDecimal.valueOf(price)); t.setDepartureDate(LocalDate.now().minusDays(new Random().nextInt(10)));
        if (isArrived) t.setArrivalDate(LocalDate.now());
        t.setCargoType(type); t.setCargoWeight(weight); t.setPassengerCount(passengers); t.setCompany(comp); t.setDriver(driver); t.setVehicle(vehicle); t.setClient(client); t.setPaid(isPaid);
        TransportDao.save(t);
    }
}