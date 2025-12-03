package by.art.multithreading.validator.impl;

import by.art.multithreading.entity.TruckData;
import by.art.multithreading.validator.TruckValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TruckValidatorImpl implements TruckValidator {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public boolean validateCargo(TruckData truckData) {
    boolean isCargoValueCorrect = true;
    if (truckData.cargoUnload() > truckData.truckCapacity()) {
      logger.warn("Truck {} {} tries to unload more than capacity!",
              truckData.brand(), truckData.plateNumber());
      isCargoValueCorrect = false;
    }
    if (truckData.cargoLoad() > truckData.truckCapacity()) {
      logger.warn("Truck {} {} tries to load more than capacity!",
              truckData.brand(), truckData.plateNumber());
      isCargoValueCorrect = false;
    }
    return isCargoValueCorrect;
  }
}
