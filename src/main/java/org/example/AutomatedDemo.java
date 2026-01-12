package org.example;

import org.example.dao.*;
import org.example.entity.*;
import org.example.util.FileIoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AutomatedDemo {

    public static void run() {
        System.out.println("==========================================");
        System.out.println("   АВТОМАТИЗИРАН ТЕСТОВ СЦЕНАРИЙ");
        System.out.println("==========================================");

        // 1. Създаване на Компания
        System.out.println("\n[СТЪПКА 1] Регистрация на компания...");
        Company company = new Company();
        company.setName("Trans-Express Demo");
        CompanyDao.save(company);
        System.out.println("-> Компанията е създадена: " + company.getName() + " (ID: " + company.getId() + ")");

        // 2. Наемане на Служител
        System.out.println("\n[СТЪПКА 2] Наемане на шофьор...");
        Employee driver = new Employee();
        driver.setName("Димитър Петров");
        driver.setSalary(new BigDecimal("2500"));
        driver.setQualification(Qualification.FLAMMABLE); // Опасни товари
        driver.setCompany(company);
        EmployeeDao.save(driver);
        System.out.println("-> Шофьорът е нает: " + driver.getName() + " (Квалификация: " + driver.getQualification() + ")");

        // 3. Закупуване на МПС
        System.out.println("\n[СТЪПКА 3] Закупуване на МПС...");
        Vehicle tanker = new Vehicle();
        tanker.setType(VehicleType.TANKER);
        tanker.setLicensePlate("CA 9999 TT");
        tanker.setCapacity(20000.0); // 20 тона
        tanker.setCompany(company);
        VehicleDao.save(tanker);
        System.out.println("-> МПС е добавено: " + tanker.getLicensePlate() + " (Капацитет: " + tanker.getCapacityInfo() + ")");

        // 4. Регистриране на Клиент
        System.out.println("\n[СТЪПКА 4] Регистрация на клиент...");
        Client client = new Client();
        client.setName("Shell Bulgaria");
        ClientDao.save(client);
        System.out.println("-> Клиентът е регистриран: " + client.getName());

        // 5. Създаване на Транспорт
        System.out.println("\n[СТЪПКА 5] Създаване на поръчка за транспорт (Гориво)...");
        Transport transport = new Transport();
        transport.setStartPoint("Нефтохим Бургас");
        transport.setEndPoint("Бензиностанция София");
        transport.setPrice(new BigDecimal("1500.00"));
        transport.setDepartureDate(LocalDate.now());

        transport.setCargoType(CargoType.FUEL);
        transport.setCargoWeight(15000.0); // 15 тона

        // Връзване на обектите
        transport.setCompany(company);
        transport.setDriver(driver);
        transport.setVehicle(tanker);
        transport.setClient(client);

        TransportDao.save(transport);
        System.out.println("-> Транспортът е създаден успешно.");
        System.out.println("   Маршрут: " + transport.getStartPoint() + " - " + transport.getEndPoint());
        System.out.println("   Товар: " + transport.getDetails());
        System.out.println("   Шофьор: " + transport.getDriverName());

        // 6. Изпълнение на курса
        System.out.println("\n[СТЪПКА 6] Изпълнение и приключване...");

        // Симулираме плащане
        transport.setPaid(true);
        System.out.println("   Статус плащане: ПЛАТЕНО (" + transport.getPrice() + " BGN)");

        // Симулираме пристигане
        transport.setArrivalDate(LocalDate.now());
        System.out.println("   Статус движение: ПРИСТИГНАЛ");

        TransportDao.update(transport);
        System.out.println("-> Данните са обновени в системата.");

        // 7. Генериране на Справка
        System.out.println("\n[СТЪПКА 7] Генериране на финансова справка...");
        BigDecimal revenue = CompanyDao.getRevenueForPeriod(company.getId(), LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        System.out.println("-> Общ приход за " + company.getName() + ": " + revenue);

        // 8. Експорт във файл
        System.out.println("\n[СТЪПКА 8] Експорт на данни...");
        FileIoUtil.writeTransports(List.of(
            new org.example.dto.TransportDto(
                transport.getId(), transport.getStartPoint(), transport.getEndPoint(),
                transport.getPrice(), transport.getDriverName(), transport.getCompanyName(),
                transport.getDepartureDate()
            )
        ), "demo_export.csv");

        System.out.println("==========================================");
        System.out.println("   ДЕМОНСТРАЦИЯТА ЗАВЪРШИ УСПЕШНО");
        System.out.println("==========================================");
    }
}