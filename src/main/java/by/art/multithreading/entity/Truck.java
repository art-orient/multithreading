package by.art.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Truck implements Runnable {
  private static final Logger logger = LogManager.getLogger();
  private static final Logger log = LogManager.getLogger();
  private final int id;
  private final String brand;
  private final String plateNumber;
  private final int truckCapacity;
  private final int cargoUnload;
  private final int cargoLoad;
  private final TruckOperation operation;
  private final boolean isPerishable;
  private TruckState state;

  public Truck(int id, String brand, String plateNumber, int truckCapacity,
               int cargoUnload, int cargoLoad, TruckOperation operation, boolean isPerishable) {
    this.id = id;
    this.brand = brand;
    this.plateNumber = plateNumber;
    this.truckCapacity = truckCapacity;
    this.cargoUnload = cargoUnload;
    this.cargoLoad = cargoLoad;
    this.operation = operation;
    this.isPerishable = isPerishable;
    state = TruckState.WAITING;
  }

  @Override
  public void run() {
    long start = System.currentTimeMillis();
    log.info("Truck {} [{}] arrives. Operation={}, perishable={}", id, brand, operation, isPerishable);
    //TODO


    long processingTime = (System.currentTimeMillis() - start) / 1000;
    logger.info("Truck with ID = {} is completed in {} seconds", id, processingTime);
  }

  public int getId() {
    return id;
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
    return isPerishable;
  }

  public TruckState getState() {
    return state;
  }

  public String getStateName() {
    return state.name();
  }

  public void setState(TruckState state) {
    this.state = state;
  }
}
