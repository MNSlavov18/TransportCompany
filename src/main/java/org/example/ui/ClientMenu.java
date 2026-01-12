package org.example.ui;

import org.example.dao.ClientDao;
import org.example.dto.ClientDto;
import org.example.entity.Client;
import org.example.service.TransportService;
import java.util.List;

public class ClientMenu implements MenuHandler {
    private final TransportService service = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- КЛИЕНТИ ---");
            System.out.println("1. Списък");
            System.out.println("2. Добавяне");
            System.out.println("3. Редакция");
            System.out.println("4. Изтриване");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1":
                    service.getAllClientsAsDto().forEach(System.out::println);
                    break;
                case "2":
                    Client c = new Client();
                    c.setName(InputUtil.readString("Име: "));
                    ClientDao.save(c);
                    System.out.println("Добавен.");
                    break;
                case "3":
                    Client ce = InputUtil.selectFromList(ClientDao.getAll(), "клиент");
                    if (ce != null) {
                        System.out.println("Какво ще редактирате?");
                        System.out.println("1. Име");
                        System.out.println("2. Статус на задължения");

                        String ec = InputUtil.readString(">> ");
                        if(ec.equals("1")) {
                            ce.setName(InputUtil.readString("Ново име: "));
                        } else if(ec.equals("2")) {
                            String paid = InputUtil.readString("Платени задължения? (true/false): ");
                            ce.setHasPaidObligations(Boolean.parseBoolean(paid));
                        }
                        ClientDao.update(ce);
                        System.out.println("Обновено.");
                    }
                    break;
                case "4":
                    Client cd = InputUtil.selectFromList(ClientDao.getAll(), "клиент");
                    if (cd != null) {
                        try {
                            ClientDao.delete(cd.getId());
                            System.out.println("Изтрит.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
            }
        }
    }
}