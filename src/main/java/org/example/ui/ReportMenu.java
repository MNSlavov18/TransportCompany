package org.example.ui;

import org.example.dao.CompanyDao;
import org.example.dto.CompanyIncomeDto;
import org.example.entity.Company;
import org.example.service.TransportService;
import org.example.util.FileIoUtil;

import java.time.LocalDate;
import java.util.List;

public class ReportMenu implements MenuHandler {
    private final TransportService service = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- СПРАВКИ И ФАЙЛОВЕ (DTO) ---");
            System.out.println("1. Списък Компании (по Приход)");
            System.out.println("2. Списък Служители (по Заплата)");
            System.out.println("3. Списък Превози (по Дестинация)");
            System.out.println("4. Пълна Статистика (Общи суми + Шофьори)");
            System.out.println("5. Приходи на компания за период");
            System.out.println("6. Експорт на превози във файл");
            System.out.println("7. Четене от файл");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1":
                    service.getSortedCompanies(true).forEach(System.out::println);
                    break;
                case "2":
                    service.getSortedEmployees(true).forEach(System.out::println);
                    break;
                case "3":
                    service.getSortedTransports().forEach(System.out::println);
                    break;
                case "4":
                    service.printCompleteStatistics();
                    break;
                case "5":
                    Company c = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (c != null) {
                        CompanyIncomeDto dto = service.getCompanyIncomeReport(
                                c.getId(), LocalDate.now().minusMonths(12), LocalDate.now());
                        System.out.println(dto);
                    }
                    break;
                case "6":
                    Company ce = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (ce != null) {
                        String name = InputUtil.readString("Име на файл: ");
                        if (name.isBlank()) name = ce.getName();
                        service.exportCompanyTransports(ce.getId(), name);
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