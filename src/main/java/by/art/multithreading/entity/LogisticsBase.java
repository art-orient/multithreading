package by.art.multithreading.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class LogisticsBase {
  private final Terminal[] terminals;
  private final int baseMaxCapacity;
  private final double maxLoadFactor = 0.8;
  private final double minLoadFactor = 0.2;
  private AtomicInteger currentWeight;
  private final List<Truck> trucks = new ArrayList<>();

  public LogisticsBase(int terminalCount, int maxWeightCapacity) {
    this.terminals = new Terminal[terminalCount];
    for (int i = 0; i < terminalCount; i++) {
      terminals[i] = new Terminal(i + 1);
    }
    this.baseMaxCapacity = maxWeightCapacity;
    currentWeight = new AtomicInteger(maxWeightCapacity / 2);
  }

  public void processTrucks() {
    ExecutorService pool = Executors.newFixedThreadPool(terminals.length);
    for (Truck truck : trucks) {
      pool.submit(truck);
    }
    //TODO
  }
}
