package org.example.ui;

import org.example.dao.CompanyDao;
import org.example.entity.Company;
import java.math.BigDecimal;

public class CompanyMenu implements MenuHandler {
    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- КОМПАНИИ ---");
            System.out.println("1. Списък");
            System.out.println("2. Добавяне");
            System.out.println("3. Редакция");
            System.out.println("4. Изтриване");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1":
                    CompanyDao.getAll().forEach(System.out::println);
                    break;
                case "2":
                    Company c = new Company();
                    c.setName(InputUtil.readString("Име на компания: "));
                    CompanyDao.save(c);
                    System.out.println("Успешно записано.");
                    break;
                case "3":
                    Company ce = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (ce != null) {
                        ce.setName(InputUtil.readString("Ново име: "));
                        CompanyDao.update(ce);
                        System.out.println("Обновено.");
                    }
                    break;
                case "4":
                    Company cd = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if (cd != null) {
                        try {
                            CompanyDao.delete(cd.getId());
                            System.out.println("  Компанията беше изтрита успешно.");
                        } catch (Exception e) {
                            // Тук хващаме грешката, за да не спре програмата!
                            System.out.println("   ГРЕШКА: Не може да изтриете тази компания!");
                            System.out.println("   Причина: Тя има активни служители, камиони или превози.");
                            System.out.println("   Първо трябва да изтриете свързаните с нея данни.");
                        }
                    }
                    break;
            }
        }
    }
}