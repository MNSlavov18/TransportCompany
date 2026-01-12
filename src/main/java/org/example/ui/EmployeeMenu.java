package org.example.ui;

import org.example.dao.*;
import org.example.entity.*;
import java.math.BigDecimal;

public class EmployeeMenu implements MenuHandler {
    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- СЛУЖИТЕЛИ ---");
            System.out.println("1. Списък");
            System.out.println("2. Наемане");
            System.out.println("3. Редакция");
            System.out.println("4. Уволнение");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1": EmployeeDao.getAll().forEach(System.out::println); break;
                case "2":
                    try {
                        Employee e = new Employee();
                        e.setName(InputUtil.readString("Име: "));
                        e.setSalary(new BigDecimal(InputUtil.readString("Заплата: ")));
                        System.out.println("Квалификация: 1.FLAMMABLE, 2.PASSENGER, 3.HEAVY_LOAD, 4.GENERAL");
                        int q = Integer.parseInt(InputUtil.readString("Избор (1-4): ")) - 1;
                        e.setQualification(Qualification.values()[q]);

                        Company c = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                        if (c != null) {
                            e.setCompany(c);
                            EmployeeDao.save(e);
                            System.out.println("Служителят е нает успешно.");
                        }
                    } catch (Exception ex) {
                        System.out.println("Грешка при въвеждане.");
                    }
                    break;
                case "3":
                    Employee ee = InputUtil.selectFromList(EmployeeDao.getAll(), "служител");
                    if (ee != null) {
                        System.out.println("Какво ще редактирате?");
                        System.out.println("1. Име");
                        System.out.println("2. Заплата");
                        System.out.println("3. Квалификация");
                        System.out.println("4. Смяна на фирма");

                        String editChoice = InputUtil.readString(">> ");

                        try {
                            switch (editChoice) {
                                case "1":
                                    ee.setName(InputUtil.readString("Ново име: "));
                                    break;
                                case "2":
                                    ee.setSalary(new BigDecimal(InputUtil.readString("Нова заплата: ")));
                                    break;
                                case "3":
                                    System.out.println("1.FLAMMABLE, 2.PASSENGER, 3.HEAVY_LOAD, 4.GENERAL");
                                    int q = Integer.parseInt(InputUtil.readString("Избор: ")) - 1;
                                    ee.setQualification(Qualification.values()[q]);
                                    break;
                                case "4":
                                    Company newComp = InputUtil.selectFromList(CompanyDao.getAll(), "нова фирма");
                                    if(newComp != null) ee.setCompany(newComp);
                                    break;
                                default:
                                    System.out.println("Невалиден избор.");
                            }
                            EmployeeDao.update(ee);
                            System.out.println("Данните са обновени.");
                        } catch (Exception e) {
                            System.out.println("Грешка при редакция: " + e.getMessage());
                        }
                    }
                    break;
                case "4":
                    Employee ed = InputUtil.selectFromList(EmployeeDao.getAll(), "служител");
                    if (ed != null) {
                        try {
                            EmployeeDao.delete(ed.getId());
                            System.out.println("Служителят е уволнен.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
            }
        }
    }
}