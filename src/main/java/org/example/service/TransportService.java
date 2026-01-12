package org.example.service;

import org.example.dao.TransportDao;
import org.example.dto.TransportDto;
import org.example.entity.*;
import org.example.util.FileIoUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransportService {

    public void createTransport(Transport t) throws Exception {
        // Бизнес логика и валидация
        if (t.getCompany() == null) throw new Exception("Трябва да изберете компания!");
        if (t.getVehicle() == null) throw new Exception("Трябва да изберете МПС!");
        if (t.getDriver() == null) throw new Exception("Трябва да изберете шофьор!");

        if (!isVehicleCompatible(t.getVehicle(), t.getCargoType())) {
            throw new Exception("Избраното МПС не е подходящо за този тип товар!");
        }
        if (!hasCapacity(t.getVehicle(), t)) {
            throw new Exception("Няма достатъчно капацитет в МПС-то!");
        }
        if (!isDriverCompatible(t.getDriver(), t.getCargoType())) {
            throw new Exception("Шофьорът няма нужната квалификация!");
        }

        TransportDao.save(t);
    }

    public List<TransportDto> getSortedTransports() {
        return TransportDao.getAll().stream()
                .map(t -> new TransportDto(
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriverName(), t.getCompanyName(), t.getDepartureDate()))
                .sorted(Comparator.comparing(TransportDto::getEndPoint))
                .collect(Collectors.toList());
    }

    public void exportCompanyTransports(String companyId, String filename) {
        List<TransportDto> data = TransportDao.getByCompany(companyId);
        FileIoUtil.writeTransports(data, filename);
    }

    public void printStats() {
        List<Transport> all = TransportDao.getAll();
        System.out.println("Общ брой превози: " + all.size());
        BigDecimal totalSum = all.stream().map(Transport::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("Обща сума: " + totalSum);

        System.out.println("\n--- Брой превози по шофьор ---");
        Map<String, Long> byDriver = all.stream().filter(t -> t.getDriver() != null)
                .collect(Collectors.groupingBy(t -> t.getDriver().getName(), Collectors.counting()));
        byDriver.forEach((d, c) -> System.out.println(d + ": " + c));

        System.out.println("\n--- Приход по шофьор ---");
        Map<String, BigDecimal> revByDriver = all.stream().filter(t -> t.getDriver() != null)
                .collect(Collectors.groupingBy(t -> t.getDriver().getName(),
                        Collectors.reducing(BigDecimal.ZERO, Transport::getPrice, BigDecimal::add)));
        revByDriver.forEach((d, r) -> System.out.println(d + ": " + r));
    }

    // --- Валидации ---
    public boolean isVehicleCompatible(Vehicle v, CargoType cargo) {
        if (v == null) return false;
        switch (cargo) {
            case PASSENGERS: return v.getType() == VehicleType.BUS;
            case FUEL:       return v.getType() == VehicleType.TANKER;
            case ANIMALS:    return v.getType() == VehicleType.TRUCK;
            default:         return v.getType() == VehicleType.TRUCK || v.getType() == VehicleType.VAN;
        }
    }

    public boolean hasCapacity(Vehicle v, Transport t) {
        if (v == null || v.getCapacity() == null) return true;
        if (t.getCargoType() == CargoType.PASSENGERS) {
            return v.getCapacity() >= (t.getPassengerCount() != null ? t.getPassengerCount() : 0);
        } else {
            return v.getCapacity() >= (t.getCargoWeight() != null ? t.getCargoWeight() : 0.0);
        }
    }

    public boolean isDriverCompatible(Employee e, CargoType cargo) {
        if (e == null) return false;
        switch (cargo) {
            case PASSENGERS: return e.getQualification() == Qualification.PASSENGER;
            case FUEL:       return e.getQualification() == Qualification.FLAMMABLE;
            case ANIMALS:    return e.getQualification() == Qualification.HEAVY_LOAD || e.getQualification() == Qualification.GENERAL;
            default:         return true;
        }
    }
}