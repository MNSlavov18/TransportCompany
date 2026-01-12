package org.example.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntityLogicTest {

    // --- ТЕСТОВЕ ЗА VEHICLE ---

    @Test
    void testVehicleCapacityInfo_Bus_ShouldShowSeats() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.BUS);
        v.setCapacity(50.0);

        // Очаква се да пише "места", защото е автобус
        assertTrue(v.getCapacityInfo().contains("места"));
        assertTrue(v.getCapacityInfo().contains("50"));
    }

    @Test
    void testVehicleCapacityInfo_Truck_ShouldShowKg() {
        Vehicle v = new Vehicle();
        v.setType(VehicleType.TRUCK);
        v.setCapacity(2000.0);

        // Очаква се да пише "кг", защото е камион
        assertTrue(v.getCapacityInfo().contains("кг"));
    }

    @Test
    void testVehicleCapacityInfo_Null_ShouldReturnNA() {
        Vehicle v = new Vehicle();
        assertEquals("N/A", v.getCapacityInfo());
    }

    // --- ТЕСТОВЕ ЗА TRANSPORT ---

    @Test
    void testTransportDetails_Passengers() {
        Transport t = new Transport();
        t.setCargoType(CargoType.PASSENGERS);
        t.setPassengerCount(10);

        String details = t.getDetails();
        assertTrue(details.contains("10"));
        assertTrue(details.contains("passengers") || details.contains("пътници"));
    }

    @Test
    void testTransportDetails_Cargo() {
        Transport t = new Transport();
        t.setCargoType(CargoType.GENERAL);
        t.setCargoWeight(500.5);

        String details = t.getDetails();
        assertTrue(details.contains("500.5"));
        assertTrue(details.contains("kg") || details.contains("кг"));
    }

    @Test
    void testTransportDriverName_NullDriver_ShouldReturnNA() {
        Transport t = new Transport();
        t.setDriver(null);
        assertEquals("N/A", t.getDriverName());
    }
}