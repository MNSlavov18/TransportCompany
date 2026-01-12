package org.example.ui;
import org.example.dao.ClientDao;
import org.example.entity.Client;

public class ClientMenu implements MenuHandler {
    public void show() {
        while (true) {
            System.out.println("\n--- КЛИЕНТИ ---");
            System.out.println("1. Списък\n2. Добавяне\n3. Редакция\n4. Изтриване\n0. Назад");
            String ch = InputUtil.readString(">> ");
            if (ch.equals("0")) return;
            switch (ch) {
                case "1": ClientDao.getAll().forEach(System.out::println); break;
                case "2": Client c = new Client(); c.setName(InputUtil.readString("Име: ")); ClientDao.save(c); break;
                case "3": Client ce = InputUtil.selectFromList(ClientDao.getAll(), "клиент"); if(ce!=null) { ce.setHasPaidObligations(Boolean.parseBoolean(InputUtil.readString("Платено? (true/false): "))); ClientDao.update(ce); } break;
                case "4": Client cd = InputUtil.selectFromList(ClientDao.getAll(), "клиент"); if(cd!=null) ClientDao.delete(cd.getId()); break;
            }
        }
    }
}