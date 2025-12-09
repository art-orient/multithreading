package by.art.multithreading.entity;

import by.art.multithreading.exception.LogisticsBaseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public final class LogisticsBase {
  private static final Logger logger = LogManager.getLogger();
  private static final int NUMBER_TERMINALS = 5;
  private static final int BASE_CAPACITY = 100_000;
  private static final double MAX_LOAD_FACTOR = 0.8;
  private static final double MIN_LOAD_FACTOR = 0.2;
  private final AtomicInteger currentBaseCargoWeight;
  private final ArrayDeque<Terminal> terminals;
  private final ReentrantLock lock = new ReentrantLock();
  private final Condition terminalAvailable = lock.newCondition();

  private LogisticsBase() {
    this.terminals = new ArrayDeque<>(NUMBER_TERMINALS);
    for (int i = 0; i < NUMBER_TERMINALS; i++) {
      terminals.offer(new Terminal(i + 1));
    }
    currentBaseCargoWeight = new AtomicInteger(BASE_CAPACITY / 2);
    logger.debug("Initialized terminals: {}", terminals);
  }

  private static class Holder {
    private static final LogisticsBase INSTANCE = new LogisticsBase();
  }

  public static LogisticsBase getInstance() {
    return Holder.INSTANCE;
  }

  public void processTruck(Truck truck) {
    Terminal terminalForProcess = null;
    try {
      terminalForProcess = captureTerminal();
      truck.setState(TruckState.PROCESSING);
      logger.debug("Truck {} occupied terminal {}. Free terminals left: {}. Queue={}",
              truck.getTruckId(), terminalForProcess.id(), terminals.size(), terminals);
      truck.performOperation();
      updateBaseWeight(truck);
      truck.setState(TruckState.COMPLETED);
      logger.info("Truck {} ({} {}) finished work with terminal {}, state = {}, Current base weight = {}",
              truck.getTruckId(), truck.getBrand(), truck.getPlateNumber(),
              terminalForProcess.id(), truck.getState(), getCurrentBaseCargoWeight().get());
    } catch (LogisticsBaseException e) {
      Thread.currentThread().interrupt();
      logger.error("Truck {} process was interrupted", truck.getTruckId(), e);
    } finally {
      leaveTerminal(terminalForProcess);
    }
  }

  private Terminal captureTerminal() throws LogisticsBaseException {
    lock.lock();
    try {
      while (terminals.isEmpty()) {
        logger.info("No free terminals");
        terminalAvailable.await();
      }
      return terminals.poll();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error("Terminal capturing was interrupted while waiting for terminal", e);
      throw new LogisticsBaseException("Truck process was interrupted while waiting for terminal", e);
    } finally {
      lock.unlock();
    }
  }

  public void updateBaseWeight(Truck truck) {
    if (truck.getCargoUnload() > 0) {
      currentBaseCargoWeight.addAndGet(truck.getCargoUnload());
    }
    if (truck.getCargoLoad() > 0) {
      currentBaseCargoWeight.addAndGet(-truck.getCargoLoad());
    }
    checkWeight();
  }

  private void checkWeight() {
    int weight = currentBaseCargoWeight.get();
    if (weight >= BASE_CAPACITY * MAX_LOAD_FACTOR) {
      logger.info("Overload detected! Train dispatched to unload. Current base weight = {}", weight);
      currentBaseCargoWeight.addAndGet(-BASE_CAPACITY / 3);
      logger.info("Train took goods. Current base weight = {}", currentBaseCargoWeight.get());
    } else if (weight <= BASE_CAPACITY * MIN_LOAD_FACTOR) {
      logger.info("Too few goods at the base! The train has been dispatched to deliver additional goods. " +
                      "Current base weight = {}", weight);
      currentBaseCargoWeight.addAndGet(BASE_CAPACITY / 3);
      logger.info("Train delivered goods. Current base weight = {}", currentBaseCargoWeight.get());
    }
  }

  private void leaveTerminal(Terminal terminal) {
    if (terminal != null) {
      lock.lock();
      try {
        terminals.addFirst(terminal);
        terminalAvailable.signal();
        logger.debug("Terminal {} released", terminal.id());
      } finally {
        lock.unlock();
      }
    }
  }

  public AtomicInteger getCurrentBaseCargoWeight() {
    return currentBaseCargoWeight;
  }
}
