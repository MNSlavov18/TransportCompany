package org.example.entity;

public enum CargoType {
    PASSENGERS,   // Хора -> Само BUS
    ANIMALS,      // Животни -> Специален превоз (TRUCK)
    FUEL,         // Горива -> Само TANKER
    FRAGILE,      // Чупливи -> TRUCK/VAN
    GENERAL,      // Общи -> TRUCK/VAN
    HEAVY_LOAD    // Tежи тивари -> TRUCK
}