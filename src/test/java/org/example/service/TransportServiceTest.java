package org.example.service;

import org.example.entity.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransportServiceTest {

    private final TransportService service = new TransportService();

    // --- ТЕСТОВЕ ЗА СЪВМЕСТИМОСТ НА МПС ---

    @Test
    void testVehicleCompatible_BusAndPassengers_ShouldReturnTrue() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.BUS);
        assertTrue(service.isVehicleCompatible(v, CargoType.PASSENGERS), "Автобусът трябва да може да вози пътници");
    }

    @Test
    void testVehicleCompatible_TruckAndPassengers_ShouldReturnFalse() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.TRUCK);
        assertFalse(service.isVehicleCompatible(v, CargoType.PASSENGERS), "Камионът НЕ трябва да вози пътници");
    }

    @Test
    void testVehicleCompatible_TankerAndFuel_ShouldReturnTrue() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.TANKER);
        assertTrue(service.isVehicleCompatible(v, CargoType.FUEL), "Цистерната трябва да вози гориво");
    }

    @Test
    void testVehicleCompatible_BusAndFuel_ShouldReturnFalse() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.BUS);
        assertFalse(service.isVehicleCompatible(v, CargoType.FUEL), "Автобусът НЕ трябва да вози гориво");
    }

    // --- ТЕСТОВЕ ЗА КАПАЦИТЕТ ---

    @Test
    void testHasCapacity_BusEnoughSeats_ShouldReturnTrue() {
        Vehicle bus = new Vehicle();
        bus.setType(VehicleType.BUS);
        bus.setCapacity(50.0);

        Transport t = new Transport();
        t.setCargoType(CargoType.PASSENGERS);
        t.setPassengerCount(45); // По-малко от 50

        assertTrue(service.hasCapacity(bus, t), "Трябва да има място за 45 души в 50-местен бус");
    }

    @Test
    void testHasCapacity_BusNotEnoughSeats_ShouldReturnFalse() {
        Vehicle bus = new Vehicle();
        bus.setType(VehicleType.BUS);
        bus.setCapacity(10.0);

        Transport t = new Transport();
        t.setCargoType(CargoType.PASSENGERS);
        t.setPassengerCount(15); // Повече от 10

        assertFalse(service.hasCapacity(bus, t), "Не трябва да позволява претоварване с пътници");
    }

    @Test
    void testHasCapacity_TruckEnoughWeight_ShouldReturnTrue() {
        Vehicle truck = new Vehicle();
        truck.setType(VehicleType.TRUCK);
        truck.setCapacity(1000.0);

        Transport t = new Transport();
        t.setCargoType(CargoType.GENERAL);
        t.setCargoWeight(900.0);

        assertTrue(service.hasCapacity(truck, t), "Камионът трябва да събере товара");
    }

    @Test
    void testHasCapacity_TruckOverload_ShouldReturnFalse() {
        Vehicle truck = new Vehicle();
        truck.setType(VehicleType.TRUCK);
        truck.setCapacity(1000.0);

        Transport t = new Transport();
        t.setCargoType(CargoType.GENERAL);
        t.setCargoWeight(1001.0); // 1 кг отгоре

        assertFalse(service.hasCapacity(truck, t), "Не трябва да позволява претоварване на камион");
    }

    // --- ТЕСТОВЕ ЗА ШОФЬОРИ ---

    @Test
    void testDriverCompatible_PassengerQual_ShouldDriveBus() {
        Employee e = new Employee();
        e.setQualification(Qualification.PASSENGER);
        assertTrue(service.isDriverCompatible(e, CargoType.PASSENGERS));
    }

    @Test
    void testDriverCompatible_FlammableQual_ShouldNotDriveBus() {
        Employee e = new Employee();
        e.setQualification(Qualification.FLAMMABLE);
        // Шофьор за горива не би трябвало да кара хора
        assertFalse(service.isDriverCompatible(e, CargoType.PASSENGERS));
    }

    @Test
    void testDriverCompatible_GeneralQual_ShouldDriveGeneralCargo() {
        Employee e = new Employee();
        e.setQualification(Qualification.GENERAL);
        assertTrue(service.isDriverCompatible(e, CargoType.GENERAL));
    }

    @Test
    void testDriverCompatible_NullDriver_ShouldReturnFalse() {
        assertFalse(service.isDriverCompatible(null, CargoType.GENERAL));
    }
}