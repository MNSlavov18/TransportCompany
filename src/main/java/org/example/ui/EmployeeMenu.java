package org.example.ui;
import org.example.dao.*;
import org.example.entity.*;
import java.math.BigDecimal;

public class EmployeeMenu implements MenuHandler {
    public void show() {
        while (true) {
            System.out.println("\n--- СЛУЖИТЕЛИ ---");
            System.out.println("1. Списък\n2. Наемане\n3. Редакция\n4. Уволнение\n0. Назад");
            String ch = InputUtil.readString(">> ");
            if (ch.equals("0")) return;
            switch (ch) {
                case "1": EmployeeDao.getAll().forEach(System.out::println); break;
                case "2":
                    Employee e = new Employee();
                    e.setName(InputUtil.readString("Име: "));
                    e.setSalary(new BigDecimal(InputUtil.readString("Заплата: ")));
                    System.out.println("Квалификация: 1.FLAMMABLE, 2.PASSENGER, 3.HEAVY, 4.GENERAL");
                    e.setQualification(Qualification.values()[Integer.parseInt(InputUtil.readString(">> "))-1]);
                    Company c = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if(c!=null) { e.setCompany(c); EmployeeDao.save(e); }
                    break;
                case "3":
                    Employee ee = InputUtil.selectFromList(EmployeeDao.getAll(), "служител");
                    if(ee!=null) { ee.setName(InputUtil.readString("Ново име: ")); EmployeeDao.update(ee); }
                    break;
                case "4":
                    Employee ed = InputUtil.selectFromList(EmployeeDao.getAll(), "служител");
                    if(ed!=null) EmployeeDao.delete(ed.getId());
                    break;
            }
        }
    }
}