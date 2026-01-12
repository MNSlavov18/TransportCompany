package org.example.ui;

import org.example.dao.CompanyDao;
import org.example.dao.VehicleDao;
import org.example.dto.VehicleDto;
import org.example.entity.Company;
import org.example.entity.Vehicle;
import org.example.entity.VehicleType;
import org.example.service.TransportService;
import java.util.List;

public class VehicleMenu implements MenuHandler {
    private final TransportService service = new TransportService();

    @Override
    public void show() {
        while (true) {
            System.out.println("\n--- МПС ---");
            System.out.println("1. Списък");
            System.out.println("2. Добавяне");
            System.out.println("3. Редакция");
            System.out.println("4. Изтриване");
            System.out.println("0. Назад");

            String choice = InputUtil.readString(">> ");
            if (choice.equals("0")) return;

            switch (choice) {
                case "1":
                    List<VehicleDto> vehicles = service.getAllVehiclesAsDto();
                    vehicles.forEach(System.out::println);
                    break;
                case "2":
                    try {
                        Vehicle v = new Vehicle();
                        System.out.println("Тип: 1.BUS, 2.TRUCK, 3.TANKER, 4.VAN");
                        int typeIdx = Integer.parseInt(InputUtil.readString("Избор: ")) - 1;
                        v.setType(VehicleType.values()[typeIdx]);
                        v.setLicensePlate(InputUtil.readString("Рег. номер: "));
                        v.setCapacity(Double.parseDouble(InputUtil.readString("Капацитет: ")));

                        Company c = InputUtil.selectFromList(CompanyDao.getAll(), "собственик");
                        if (c != null) {
                            v.setCompany(c);
                            VehicleDao.save(v);
                            System.out.println("МПС добавено.");
                        }
                    } catch (Exception e) {
                        System.out.println("Грешка при въвеждане.");
                    }
                    break;
                case "3":
                    Vehicle ve = InputUtil.selectFromList(VehicleDao.getAll(), "МПС");
                    if (ve != null) {
                        System.out.println("Какво ще редактирате?");
                        System.out.println("1. Регистрационен номер");
                        System.out.println("2. Капацитет");
                        System.out.println("3. Смяна на собственик");

                        String editChoice = InputUtil.readString(">> ");
                        try {
                            switch (editChoice) {
                                case "1":
                                    ve.setLicensePlate(InputUtil.readString("Нов номер: "));
                                    break;
                                case "2":
                                    ve.setCapacity(Double.parseDouble(InputUtil.readString("Нов капацитет: ")));
                                    break;
                                case "3":
                                    Company newC = InputUtil.selectFromList(CompanyDao.getAll(), "нова фирма");
                                    if(newC != null) ve.setCompany(newC);
                                    break;
                            }
                            VehicleDao.update(ve);
                            System.out.println("Обновено.");
                        } catch (Exception e) {
                            System.out.println("Грешка: " + e.getMessage());
                        }
                    }
                    break;
                case "4":
                    Vehicle vd = InputUtil.selectFromList(VehicleDao.getAll(), "МПС");
                    if (vd != null) {
                        try {
                            VehicleDao.delete(vd.getId());
                            System.out.println("Изтрито.");
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    break;
            }
        }
    }
}