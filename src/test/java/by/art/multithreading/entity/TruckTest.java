package by.art.multithreading.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TruckTest {
  Truck truck = new Truck("MAN", "BB2222-7", 6000,
          1000, 500, TruckOperation.UNLOAD_LOAD, false);

  @Test
  void testTruckInitialization() {
    assertAll("Truck initialization",
            () -> assertTrue(truck.getTruckId() > 0, "Truck ID should be generated"),
            () -> assertEquals("MAN", truck.getBrand(), "Brand should match"),
            () -> assertEquals("BB2222-7", truck.getPlateNumber(), "Plate number should match"),
            () -> assertEquals(TruckState.WAITING, truck.getState(), "Initial state should be WAITING")
    );
  }

  @Test
  void testPerformUnloadOperation() {
    assertDoesNotThrow(truck::performOperation,"UNLOAD operation should execute without exceptions");
  }

  @Test
  void testPerformLoadOperation() {
    Truck truck = new Truck("DAF", "DD-444", 7000,
            0, 2000, TruckOperation.LOAD, false);
    assertDoesNotThrow(truck::performOperation,"LOAD operation should execute without exceptions");
  }

  @Test
  void testPerformUnloadLoadOperation() {
    Truck truck = new Truck("Iveco", "EE-555", 5000,
            1000, 500, TruckOperation.UNLOAD_LOAD, false);
    assertDoesNotThrow(truck::performOperation,"UNLOAD_LOAD operation should execute without exceptions");
  }

  @Test
  void testSetAndGetState() {
    Truck truck = new Truck("Volvo", "AA-111", 5000,
            2000, 0, TruckOperation.UNLOAD, false);

    truck.setState(TruckState.PROCESSING);

    assertAll("Truck state change",
            () -> assertEquals(TruckState.PROCESSING, truck.getState(),
                    "State should change to PROCESSING")
    );
  }
}