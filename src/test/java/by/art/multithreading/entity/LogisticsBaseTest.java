package by.art.multithreading.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogisticsBaseTest {
  private LogisticsBase base;

  @BeforeEach
  void setUp() {
    base = LogisticsBase.getInstance();
    base.getCurrentBaseCargoWeight().set(50_000);
  }

  @Test
  void testSingletonInstance() {
    LogisticsBase anotherBase = LogisticsBase.getInstance();
    assertSame(base, anotherBase, "Singleton must return the same object");
  }

  @Test
  void testUnloadTruckIncreasesWeight() {
    Truck truck = new Truck("Volvo", "AA-111", 5000,
            2000, 0, TruckOperation.UNLOAD, false);
    base.updateBaseWeight(truck);
    assertEquals(52_000, base.getCurrentBaseCargoWeight().get(),
            "The weight should increase by the amount of unloading");
  }

  @Test
  void testLoadTruckDecreasesWeight() {
    Truck truck = new Truck("MAN", "BB-222", 6000,
            0, 3000, TruckOperation.LOAD, false);
    base.updateBaseWeight(truck);
    assertEquals(47_000, base.getCurrentBaseCargoWeight().get(),
            "The weight should be reduced by the amount of the load");
  }

  @Test
  void testOverloadTriggersTrainUnload() {
    base.getCurrentBaseCargoWeight().set(90_000);
    Truck truck = new Truck("Scania", "CC-333", 7000,
            5000, 0, TruckOperation.UNLOAD, false);
    base.updateBaseWeight(truck);
    int weight = base.getCurrentBaseCargoWeight().get();
    assertTrue(weight < 90_000, "In case of overload, a train must be sent for unloading.");
  }

  @Test
  void testUnderloadTriggersTrainDelivery() {
    base.getCurrentBaseCargoWeight().set(10_000);
    Truck truck = new Truck( "DAF", "DD-444", 7000,
            0, 1000, TruckOperation.LOAD, false);
    base.updateBaseWeight(truck);
    int weight = base.getCurrentBaseCargoWeight().get();
    assertTrue(weight > 10_000, "In case of underload, a train must be sent " +
            "to deliver the goods to the base.");
  }

  @Test
  void testTruckOccupiesAndReleasesTerminal() {
    Truck truck = new Truck("Iveco", "EE-555", 5000,
            1000, 0, TruckOperation.UNLOAD, false);
    base.processTruck(truck);
    assertEquals(TruckState.COMPLETED, truck.getState(),
            "After servicing the truck should be in condition COMPLETED");
  }
}