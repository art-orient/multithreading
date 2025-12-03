package by.art.multithreading.validator.impl;

import by.art.multithreading.entity.TruckData;
import by.art.multithreading.validator.TruckValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TruckValidatorImplTest {

  private final TruckValidator validator = new TruckValidatorImpl();

  @ParameterizedTest(name = "capacity={0}, unload={1}, load={2} => expectedValid={3}")
  @CsvSource({
          "5000, 2000, 3000, true",
          "4000, 5000, 0, false",
          "6000, 0, 7000, false",
          "3000, 4000, 5000, false"
  })
  void testValidateCargo(int capacity, int unload, int load, boolean expectedValid) {
    TruckData truckData = new TruckData("BrandX", "PLATE-123", capacity,
            unload, load, "UNLOAD_LOAD", false);
    boolean result = validator.validateCargo(truckData);
    assertEquals(expectedValid, result,
            () -> String.format("Validation failed for capacity=%d, unload=%d, load=%d",
                    capacity, unload, load));
  }
}