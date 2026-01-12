package org.example.service;

import org.example.dao.*;
import org.example.dto.*;
import org.example.entity.*;
import org.example.util.FileIoUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransportService {

    public List<EmployeeDto> getSortedEmployees(boolean bySalary) {
        return EmployeeDao.getAll().stream()
                .map(e -> new EmployeeDto(e.getName(), e.getQualification(), e.getSalary(), e.getCompanyName()))
                .sorted(bySalary
                    ? Comparator.comparing(EmployeeDto::getSalary).reversed()
                    : Comparator.comparing(EmployeeDto::getQualification))
                .collect(Collectors.toList());
    }

    public List<CompanyDto> getSortedCompanies(boolean byRevenue) {
        return CompanyDao.getAll().stream()
                .map(c -> new CompanyDto(c.getName(), c.getRevenue()))
                .sorted(byRevenue
                    ? Comparator.comparing(CompanyDto::getRevenue).reversed()
                    : Comparator.comparing(CompanyDto::getName))
                .collect(Collectors.toList());
    }

    public void createTransport(Transport t) throws Exception {
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

    public void exportCompanyTransports(String companyId, String filename) {
        List<Transport> transports = TransportDao.getByCompanyId(companyId);
        List<TransportDto> dtos = transports.stream()
                .map(t -> new TransportDto(
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriverName(), t.getCompanyName(), t.getDepartureDate()))
                .collect(Collectors.toList());
        FileIoUtil.writeTransports(dtos, filename);
    }

    public List<ClientDto> getAllClientsAsDto() {
        return ClientDao.getAll().stream()
                .map(c -> new ClientDto(c.getName(), c.isHasPaidObligations() ? "Платил" : "Дължи"))
                .collect(Collectors.toList());
    }

    public List<VehicleDto> getAllVehiclesAsDto() {
        return VehicleDao.getAll().stream()
                .map(v -> new VehicleDto(v.getLicensePlate(), v.getType(), v.getCapacityInfo(), v.getCompanyName()))
                .collect(Collectors.toList());
    }

    public List<TransportDto> getSortedTransports() {
        return TransportDao.getAll().stream()
                .map(t -> new TransportDto(
                        t.getId(), t.getStartPoint(), t.getEndPoint(), t.getPrice(),
                        t.getDriverName(), t.getCompanyName(), t.getDepartureDate()))
                .sorted(Comparator.comparing(TransportDto::getEndPoint))
                .collect(Collectors.toList());
    }

    public List<DriverReportDto> getDriverStatistics() {
        List<Transport> all = TransportDao.getAll();
        Map<String, List<Transport>> grouped = all.stream()
                .filter(t -> t.getDriver() != null)
                .collect(Collectors.groupingBy(t -> t.getDriver().getName()));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String driverName = entry.getKey();
                    String companyName = entry.getValue().get(0).getDriver().getCompanyName();
                    BigDecimal total = entry.getValue().stream()
                            .map(Transport::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new DriverReportDto(driverName, companyName, (long) entry.getValue().size(), total);
                })
                .sorted(Comparator.comparing(DriverReportDto::getTotalIncome).reversed())
                .collect(Collectors.toList());
    }

    public CompanyIncomeDto getCompanyIncomeReport(String companyId, LocalDate start, LocalDate end) {
        Company c = CompanyDao.getById(companyId);
        if (c == null) return null;
        BigDecimal revenue = CompanyDao.getRevenueForPeriod(companyId, start, end);
        return new CompanyIncomeDto(c.getName(), revenue);
    }

    public void printCompleteStatistics() {
        List<Transport> all = TransportDao.getAll();
        long totalCount = all.size();
        BigDecimal totalRevenue = all.stream()
                .map(Transport::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("=== ОБЩА СТАТИСТИКА ===");
        System.out.println("Общ брой превози: " + totalCount);
        System.out.println("Общ приход:       " + totalRevenue + " BGN");
        System.out.println("-------------------------");
        System.out.println("--- КЛАСАЦИЯ НА ШОФЬОРИ ---");

        List<DriverReportDto> driverStats = getDriverStatistics();
        if (driverStats.isEmpty()) {
            System.out.println(" (Няма данни за шофьори)");
        } else {
            for (DriverReportDto stat : driverStats) {
                System.out.printf("Шофьор: %-20s | Компания: %-15s | Курсове: %2d | Приход: %10.2f BGN%n",
                        stat.getDriverName(), stat.getCompanyName(), stat.getTripCount(), stat.getTotalIncome());
            }
        }
    }

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