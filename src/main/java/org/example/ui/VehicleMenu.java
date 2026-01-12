package org.example.ui;
import org.example.dao.*;
import org.example.entity.*;

public class VehicleMenu implements MenuHandler {
    public void show() {
        while (true) {
            System.out.println("\n--- МПС ---");
            System.out.println("1. Списък\n2. Добавяне\n3. Редакция\n4. Изтриване\n0. Назад");
            String ch = InputUtil.readString(">> ");
            if (ch.equals("0")) return;
            switch (ch) {
                case "1": VehicleDao.getAll().forEach(System.out::println); break;
                case "2":
                    Vehicle v = new Vehicle();
                    System.out.println("Тип: 1.BUS, 2.TRUCK, 3.TANKER, 4.VAN");
                    v.setType(VehicleType.values()[Integer.parseInt(InputUtil.readString(">> "))-1]);
                    v.setLicensePlate(InputUtil.readString("Рег. номер: "));
                    v.setCapacity(Double.parseDouble(InputUtil.readString("Капацитет (места/кг): ")));
                    Company c = InputUtil.selectFromList(CompanyDao.getAll(), "компания");
                    if(c!=null) { v.setCompany(c); VehicleDao.save(v); }
                    break;
                case "3":
                    Vehicle ve = InputUtil.selectFromList(VehicleDao.getAll(), "МПС");
                    if(ve!=null) { ve.setLicensePlate(InputUtil.readString("Нов номер: ")); VehicleDao.update(ve); }
                    break;
                case "4":
                    Vehicle vd = InputUtil.selectFromList(VehicleDao.getAll(), "МПС");
                    if(vd!=null) VehicleDao.delete(vd.getId());
                    break;
            }
        }
    }
}