package by.art.multithreading.validator;

import by.art.multithreading.entity.TruckData;

public interface TruckValidator {
  boolean validateCargo(TruckData truckData);
}
