package by.art.multithreading.entity;

import by.art.multithreading.exception.LogisticsBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class LogisticsBase {
  private static final Logger logger = LogManager.getLogger();
  private static final int NUMBER_TERMINALS = 5;
  private static final int BASE_MAX_CAPACITY = 100_000;
  private static final double MAX_LOAD_FACTOR = 0.8;
  private static final double MIN_LOAD_FACTOR = 0.2;
  private final AtomicInteger currentBaseCargoWeight;
  private final Terminal[] terminals;

  private LogisticsBase() {
    this.terminals = new Terminal[NUMBER_TERMINALS];
    for (int i = 0; i < terminals.length; i++) {
      terminals[i] = new Terminal(i + 1);
    }
    currentBaseCargoWeight = new AtomicInteger(BASE_MAX_CAPACITY / 2);
  }

  private static class Holder {
    private static LogisticsBase instance;
  }

  public static LogisticsBase getInstance() {
    if (Holder.instance == null) {
      Holder.instance = new LogisticsBase();
    }
    return Holder.instance;
  }

  public void dispatchTrucks(List<Truck> trucks) {
    for (Truck truck : trucks) {
      Thread thread = new Thread(truck);
      if (truck.isPerishable()) {
        thread.setPriority(Thread.MAX_PRIORITY);
      }
      thread.start();
      logger.info("Truck {} ({} {}) sent for processing",
              truck.getTruckId(), truck.getBrand(), truck.getPlateNumber());
    }
  }

  public void processTruck(Truck truck) {
    Terminal terminalForProcess = null;
    try {
      while (terminalForProcess == null) {
        for (Terminal terminal : terminals) {
          if (terminal.occupyTerminal()) {
            terminalForProcess = terminal;
            logger.debug("Truck {} ({} {}) has occupied the terminal {}", truck.getTruckId(), truck.getBrand(),
                    truck.getPlateNumber(), terminalForProcess.getId());
            truck.setState(TruckState.PROCESSING);
            break;
          }
        }
        if (terminalForProcess == null) {
          TimeUnit.MILLISECONDS.sleep(200);
        }
      }
      truck.performOperation();
      updateWeight(truck);
      logger.info("Truck {} ({} {}) finished work with terminal {}, state = {}, Current base weight = {}",
              truck.getTruckId(), truck.getBrand(), truck.getPlateNumber(), terminalForProcess.getId(),
              truck.getState(), getCurrentBaseCargoWeight().get());
      truck.setState(TruckState.COMPLETED);
    } catch (InterruptedException | LogisticsBaseException e) {
      Thread.currentThread().interrupt();
      logger.error("Truck {} was interrupted", truck.getTruckId(), e);
    } finally {
      if (terminalForProcess != null) {
        terminalForProcess.releaseTerminal();
        logger.debug("Terminal {} released", terminalForProcess.getId());
      }
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
    if (weight >= BASE_MAX_CAPACITY * MAX_LOAD_FACTOR) {
      logger.info("Overload detected! Train dispatched to unload. CurrentWeight = {}", weight);
      currentBaseCargoWeight.addAndGet(-BASE_MAX_CAPACITY / 3);
    } else if (weight <= BASE_MAX_CAPACITY * MIN_LOAD_FACTOR) {
      logger.info("Too few goods at the base! The train has been dispatched to deliver additional goods. " +
                      "CurrentWeight = {}", weight);
      currentBaseCargoWeight.addAndGet(BASE_MAX_CAPACITY / 3);
    }
  }

  public AtomicInteger getCurrentBaseCargoWeight() {
    return currentBaseCargoWeight;
  }
}
