package org.example.ui;
import org.example.dao.*;
import org.example.entity.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class ConsoleUI {
    public void start() {
        initData();
        while (true) {
            System.out.println("\n=== TRANSPORT COMPANY ===");
            System.out.println("1. Companies\n2. Employees\n3. Clients\n4. Vehicles\n5. Transports\n6. Reports\n0. Exit");
            String choice = InputUtil.readString(">> ");
            switch (choice) {
                case "1": new CompanyMenu().show(); break;
                case "2": new EmployeeMenu().show(); break;
                case "3": new ClientMenu().show(); break;
                case "4": new VehicleMenu().show(); break;
                case "5": new TransportMenu().show(); break;
                case "6": new ReportMenu().show(); break;
                case "0": return;
            }
        }
    }
    private void initData() {
        if (!CompanyDao.getAll().isEmpty()) return;
        Company c = new Company(); c.setName("Speedy"); CompanyDao.save(c);
        Employee e = new Employee(); e.setName("Ivan"); e.setSalary(new BigDecimal("2000")); e.setQualification(Qualification.PASSENGER); e.setCompany(c); EmployeeDao.save(e);
        Vehicle v = new Vehicle(); v.setType(VehicleType.BUS); v.setLicensePlate("CB1234"); v.setCapacity(50.0); v.setCompany(c); VehicleDao.save(v);
        Client cl = new Client(); cl.setName("Client A"); ClientDao.save(cl);
    }
}