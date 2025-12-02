package by.art.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public final class LogisticsBase {
  private static final Logger logger = LogManager.getLogger();
  private final Terminal[] terminals;
  private final int baseMaxCapacity;
  private final double maxLoadFactor;
  private final double minLoadFactor;
  private AtomicInteger currentBaseCargoWeight;

  private LogisticsBase(int terminalCount, int baseMaxCapacity) {
    this.terminals = new Terminal[terminalCount];
    for (int i = 0; i < terminals.length; i++) {
      terminals[i] = new Terminal(i + 1);
    }
    this.baseMaxCapacity = baseMaxCapacity;
    maxLoadFactor = 0.8;
    minLoadFactor = 0.2;
    currentBaseCargoWeight = new AtomicInteger(baseMaxCapacity / 2);
  }

  private static class Holder {
    private static LogisticsBase instance;
  }

  public static LogisticsBase getInstance(int terminalCount, int baseMaxCapacity) {
    if (Holder.instance == null) {
      Holder.instance = new LogisticsBase(terminalCount, baseMaxCapacity);
    }
    return Holder.instance;
  }

  public void processTrucks(List<Truck> trucks) {
    for (Truck truck : trucks) {
      truck.setLogisticsBase(this);
      Thread thread = new Thread(truck);
      if (truck.isPerishable()) {
        thread.setPriority(Thread.MAX_PRIORITY);
      }
      thread.start();
      logger.info("Truck {} ({} {}) started working", truck.getTruckId(),
              truck.getBrand(), truck.getPlateNumber());
    }
  }

  public void updateWeight(Truck truck) {
    if (truck.getCargoUnload() > 0) {
      currentBaseCargoWeight.addAndGet(-truck.getCargoUnload());
    }
    if (truck.getCargoLoad() > 0) {
      currentBaseCargoWeight.addAndGet(truck.getCargoLoad());
    }
    checkWeight();
  }

  private void checkWeight() {
    int weight = currentBaseCargoWeight.get();
    if (weight >= baseMaxCapacity * maxLoadFactor) {
      logger.info("Overload detected! Train dispatched to unload. CurrentWeight = {}", weight);
      currentBaseCargoWeight.addAndGet(-baseMaxCapacity / 3);
    } else if (weight <= baseMaxCapacity * minLoadFactor) {
      logger.info("Too few goods at the base! The train has been dispatched to deliver additional goods. " +
                      "CurrentWeight = {}", weight);
      currentBaseCargoWeight.addAndGet(baseMaxCapacity / 3);
    }
  }

  public Terminal[] getTerminals() {
    return terminals;
  }

  public AtomicInteger getCurrentBaseCargoWeight() {
    return currentBaseCargoWeight;
  }
}
