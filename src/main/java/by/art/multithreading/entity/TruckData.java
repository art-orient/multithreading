package by.art.multithreading.entity;

public record TruckData (
        String brand,
        String plateNumber,
        int truckCapacity,
        int cargoUnload,
        int cargoLoad,
        String operation,
        boolean perishable) { }
