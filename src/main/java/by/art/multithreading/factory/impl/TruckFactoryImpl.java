package by.art.multithreading.factory.impl;

import by.art.multithreading.entity.Truck;
import by.art.multithreading.entity.TruckData;
import by.art.multithreading.entity.TruckOperation;
import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.factory.TruckFactory;
import by.art.multithreading.util.TruckIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TruckFactoryImpl implements TruckFactory {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public List<Truck> createTrucks(List<TruckData> trucksData) throws LogisticsBaseException {
    List<Truck> trucks = new ArrayList<>();
    for (TruckData truckData : trucksData) {
      int id = TruckIdGenerator.generateId();
      TruckOperation operation = switch (truckData.operation().toUpperCase()) {
        case "UNLOAD" -> TruckOperation.UNLOAD;
        case "LOAD" -> TruckOperation.LOAD;
        case "UNLOAD_LOAD", "UNLOAD-LOAD"  -> TruckOperation.UNLOAD_LOAD;
        default -> { logger.warn("incorrect operation - {}", truckData.operation());
          throw new LogisticsBaseException("Unknown operation: " + truckData.operation());
        }
      };
      Truck truck = new Truck(id, truckData.brand(), truckData.plateNumber(), truckData.truckCapacity(),
              truckData.cargoUnload(), truckData.cargoLoad(), operation, truckData.perishable());
      trucks.add(truck);
      logger.debug("Truck created - ID = {}, brand - {}, plate number - {}",
              id, truckData.brand(), truckData.plateNumber());
    }
    return trucks;
  }
}
