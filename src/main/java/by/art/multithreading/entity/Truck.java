package by.art.multithreading.entity;

import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.util.TruckIdGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Truck implements Runnable {
  private static final Logger logger = LogManager.getLogger();
  private static final Logger log = LogManager.getLogger();
  private final int truckId;
  private final String brand;
  private final String plateNumber;
  private final int truckCapacity;
  private final int cargoUnload;
  private final int cargoLoad;
  private final TruckOperation operation;
  private final boolean perishable;
  private TruckState state;
  private LogisticsBase logisticsBase;

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
    long start = System.currentTimeMillis();
    log.debug("Truck {} ({} {}) arrives. Operation={}, perishable={}",
            truckId, brand, plateNumber, operation, perishable);
    Terminal terminal = null;
    try {
      while (terminal == null) {
        for (Terminal t : logisticsBase.getTerminals()) {
          if (t.occupyTerminal()) {
            terminal = t;
            logger.info("Truck {} occupied terminal {}", truckId, t.getId());
            setState(TruckState.PROCESSING);
            break;
          }
        }
        if (terminal == null) {
          logger.debug("Truck {} is waiting for a free terminal", truckId);
          TimeUnit.MILLISECONDS.sleep(200);
        }
      }

      state = TruckState.PROCESSING;
      switch (operation) {
        case UNLOAD -> {
          if (cargoUnload > truckCapacity) {
            logger.warn("Truck {} tries to unload more than capacity!", truckId);
            throw new LogisticsBaseException(
                    "Truck " + truckId + " unloads more than capacity: " + cargoUnload);
          }
          logger.debug("Truck {} unloads {} kg", truckId, cargoUnload);
          TimeUnit.MILLISECONDS.sleep(300);
        }
        case LOAD -> {
          if (cargoLoad > truckCapacity) {
            logger.warn("Truck {} tries to load more than capacity!", truckId);
            throw new LogisticsBaseException(
                    "Truck " + truckId + " loads more than capacity: " + cargoUnload);
          }
          logger.info("Truck {} loads {} kg", truckId, cargoLoad);
          TimeUnit.MILLISECONDS.sleep(300);
        }
        case UNLOAD_LOAD -> {
          logger.info("Truck {} unloads {} kg and then loads {} kg", truckId, cargoUnload, cargoLoad);
          TimeUnit.MILLISECONDS.sleep(500);
        }
      }
      logisticsBase.updateWeight(this);
    } catch (InterruptedException | LogisticsBaseException e) {
      logger.error("Truck {} processing was interrupted", truckId, e);
      Thread.currentThread().interrupt();
    } finally {
      if (terminal != null) {
        terminal.releaseTerminal();
        logger.debug("Terminal {} released", terminal.getId());
      }
    }
    state = TruckState.COMPLETED;
    long processingTime = (System.currentTimeMillis() - start) / 1000;
    logger.info("Truck processing with ID = {} took {} minutes", truckId, processingTime);
    logger.info("Truck {} ({} {}) finished work with terminal {}, state = {}, Current base weight = {}",
        truckId, brand, plateNumber, terminal.getId(), state, logisticsBase.getCurrentBaseCargoWeight().get());
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

  public void setLogisticsBase(LogisticsBase logisticsBase) {
    this.logisticsBase = logisticsBase;
  }
}
