package org.example.ui;
import org.example.dao.CompanyDao;
import org.example.entity.Company;
import java.math.BigDecimal;

public class CompanyMenu implements MenuHandler {
    public void show() {
        while (true) {
            System.out.println("\n--- КОМПАНИИ ---");
            System.out.println("1. Списък\n2. Добавяне\n3. Редакция\n4. Изтриване\n0. Назад");
            String ch = InputUtil.readString(">> ");
            if (ch.equals("0")) return;
            switch (ch) {
                case "1": CompanyDao.getAll().forEach(System.out::println); break;
                case "2": Company c = new Company(); c.setName(InputUtil.readString("Име: ")); CompanyDao.save(c); break;
                case "3":
                    Company ce = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if(ce!=null) { ce.setName(InputUtil.readString("Ново име: ")); CompanyDao.update(ce); } break;
                case "4": Company cd = InputUtil.selectFromList(CompanyDao.getAll(), "компания"); if(cd!=null) CompanyDao.delete(cd.getId()); break;
            }
        }
    }
}