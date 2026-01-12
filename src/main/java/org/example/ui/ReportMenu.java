package org.example.ui;

import org.example.dao.CompanyDao;
import org.example.dao.EmployeeDao;
import org.example.entity.Company;
import org.example.service.TransportService;
import org.example.util.FileIoUtil;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReportMenu implements MenuHandler {
    private final TransportService transportService = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- 📊 СПРАВКИ ---");
            System.out.println("1. Сортиране Компании (по Приход)");
            System.out.println("2. Сортиране Служители (по Заплата)");
            System.out.println("3. Сортиране Превози (по Дестинация)");
            System.out.println("4. Статистика (Брой/Суми/Шофьори)");
            System.out.println("5. Приходи на компания за период");
            System.out.println("6. Експорт на превози на КОМПАНИЯ");
            System.out.println("7. Четене от файл");
            System.out.println("0. Назад");
            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;
            switch (choice) {
                case "1": CompanyDao.getSorted(true).forEach(System.out::println); break;
                case "2": EmployeeDao.getSorted(true).forEach(System.out::println); break;
                case "3": transportService.getSortedTransports().forEach(System.out::println); break;
                case "4": transportService.printStats(); break;
                case "5":
                    Company c = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (c != null) {
                        BigDecimal rev = CompanyDao.getRevenueForPeriod(c.getId(), LocalDate.now().minusMonths(12), LocalDate.now());
                        System.out.println("💰 Приходи: " + rev);
                    }
                    break;
                case "6":
                    Company ce = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (ce != null) {
                        String name = InputUtil.readString("Име на файл: ");
                        if(name.isBlank()) name = ce.getName();
                        transportService.exportCompanyTransports(ce.getId(), name);
                    }
                    break;
                case "7":
                    List<String> files = FileIoUtil.listCsvFiles();
                    String f = InputUtil.selectFromList(files, "файл");
                    if (f != null) FileIoUtil.readAndPrintFile(f);
                    break;
            }
        }
    }
}