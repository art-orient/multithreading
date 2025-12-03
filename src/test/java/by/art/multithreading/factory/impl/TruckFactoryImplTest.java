package by.art.multithreading.factory.impl;

import by.art.multithreading.entity.Truck;
import by.art.multithreading.entity.TruckData;
import by.art.multithreading.entity.TruckOperation;
import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.factory.TruckFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckFactoryImplTest {
  private final TruckFactory factory = new TruckFactoryImpl();

  @Test
  void testCreateValidTruckUnload() throws LogisticsBaseException {
    TruckData data = new TruckData("Volvo", "AA-111", 5000,
            2000, 0, "UNLOAD", false);
    List<Truck> trucks = factory.createTrucks(List.of(data));
    assertAll("Valid UNLOAD truck",
            () -> assertEquals(1, trucks.size(), "One truck should be created"),
            () -> assertEquals("Volvo", trucks.get(0).getBrand(), "Brand should match"),
            () -> assertEquals("AA-111", trucks.get(0).getPlateNumber(), "Plate number should match"),
            () -> assertEquals(TruckOperation.UNLOAD, trucks.get(0).getOperation(), "Operation should be UNLOAD")
    );
  }

  @Test
  void testCreateValidTruckLoad() throws LogisticsBaseException {
    TruckData data = new TruckData("MAN", "BB-222", 6000,
            0, 3000, "LOAD", true);
    List<Truck> trucks = factory.createTrucks(List.of(data));
    assertAll("Valid LOAD truck",
            () -> assertEquals(1, trucks.size(), "One truck should be created"),
            () -> assertEquals("MAN", trucks.get(0).getBrand(), "Brand should match"),
            () -> assertTrue(trucks.get(0).isPerishable(), "Truck should be perishable"),
            () -> assertEquals(TruckOperation.LOAD, trucks.get(0).getOperation(), "Operation should be LOAD")
    );
  }

  @Test
  void testCreateValidTruckUnloadLoad() throws LogisticsBaseException {
    TruckData data = new TruckData("Scania", "CC-333", 7000,
            1500, 2000, "UNLOAD_LOAD", false);
    List<Truck> trucks = factory.createTrucks(List.of(data));
    assertAll("Valid UNLOAD_LOAD truck",
            () -> assertEquals(1, trucks.size(), "One truck should be created"),
            () -> assertEquals(TruckOperation.UNLOAD_LOAD, trucks.get(0).getOperation(),
                    "Operation should be UNLOAD_LOAD")
    );
  }

  @Test
  void testInvalidCargoSkipped() throws LogisticsBaseException {
    TruckData data = new TruckData("DAF", "DD-444", 3000,
            4000, 0, "UNLOAD", false); // unload > capacity
    List<Truck> trucks = factory.createTrucks(List.of(data));
    assertTrue(trucks.isEmpty(), "Truck with invalid cargo should be skipped");
  }

  @Test
  void testUnknownOperationThrowsException() {
    TruckData data = new TruckData("Iveco", "EE-555", 5000,
            1000, 500, "INVALID_OP", false);
    assertThrows(LogisticsBaseException.class,
            () -> factory.createTrucks(List.of(data)),
            "Unknown operation should throw LogisticsBaseException");
  }

  @Test
  void testMultipleTrucksMixedValidity() throws LogisticsBaseException {
    TruckData valid1 = new TruckData("Volvo", "AA-111", 5000,
            2000, 0, "UNLOAD", false);
    TruckData invalid = new TruckData("DAF", "DD-444", 3000,
            4000, 0, "UNLOAD", false); // invalid cargo
    TruckData valid2 = new TruckData("MAN", "BB-222", 6000,
            0, 3000, "LOAD", true);
    List<Truck> trucks = factory.createTrucks(List.of(valid1, invalid, valid2));
    assertAll("Mixed validity trucks",
            () -> assertEquals(2, trucks.size(), "Only valid trucks should be created"),
            () -> assertEquals("Volvo", trucks.get(0).getBrand(), "First truck should be Volvo"),
            () -> assertEquals("MAN", trucks.get(1).getBrand(), "Second truck should be MAN")
    );
  }
}