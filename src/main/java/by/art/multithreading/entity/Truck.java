package by.art.multithreading.entity;

import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.util.TruckIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Truck implements Runnable {
  private static final Logger logger = LogManager.getLogger();
  private final int truckId;
  private final String brand;
  private final String plateNumber;
  private final int truckCapacity;
  private final int cargoUnload;
  private final int cargoLoad;
  private final TruckOperation operation;
  private final boolean perishable;
  private TruckState state;

  public Truck(String brand, String plateNumber, int truckCapacity,
               int cargoUnload, int cargoLoad, TruckOperation operation, boolean perishable) {
    this.truckId = TruckIdGenerator.generateId();
    this.brand = brand;
    this.plateNumber = plateNumber;
    this.truckCapacity = truckCapacity;
    this.cargoUnload = cargoUnload;
    this.cargoLoad = cargoLoad;
    this.operation = operation;
    this.perishable = perishable;
    state = TruckState.WAITING;
  }

  @Override
  public void run() {
    LogisticsBase base = LogisticsBase.getInstance();
    base.processTruck(this);
  }

  public void performOperation() throws LogisticsBaseException {
    logger.debug("Truck {} ({} {}) arrives. Operation={}, perishable={}",
            truckId, brand, plateNumber, operation, perishable);
    long time = calculateProcessingTime(cargoUnload, cargoLoad);
    try {
      switch (operation) {
        case UNLOAD -> {
          logger.debug("Truck {} unloads {} kg", truckId, cargoUnload);
          TimeUnit.MILLISECONDS.sleep(time);
        }
        case LOAD -> {
          logger.info("Truck {} loads {} kg", truckId, cargoLoad);
          TimeUnit.MILLISECONDS.sleep(time);
        }
        case UNLOAD_LOAD -> {
          logger.info("Truck {} unloads {} kg and then loads {} kg",
                  truckId, cargoUnload, cargoLoad);
          TimeUnit.MILLISECONDS.sleep(time);
        }
        default -> {
          logger.error("Truck {} has unknown operation: {}", truckId, operation);
          throw new LogisticsBaseException("Unknown truck operation: " + operation);
        }
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error("Truck {} process was interrupted", truckId, e);
    }
  }

  public int getTruckId() {
    return truckId;
  }

  public String getBrand() {
    return brand;
  }

  public String getPlateNumber() {
    return plateNumber;
  }

  public int getTruckCapacity() {
    return truckCapacity;
  }

  public int getCargoUnload() {
    return cargoUnload;
  }

  public int getCargoLoad() {
    return cargoLoad;
  }

  public TruckOperation getOperation() {
    return operation;
  }

  public boolean isPerishable() {
    return perishable;
  }

  public TruckState getState() {
    return state;
  }

  public void setState(TruckState state) {
    this.state = state;
  }

  private long calculateProcessingTime(int cargoUnload, int cargoLoad) {
    return (cargoUnload + cargoLoad) / 10 + 100L;
  }
}
