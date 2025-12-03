package by.art.multithreading.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class TruckIdGenerator {
  private static final AtomicInteger counter = new AtomicInteger(0);

  private TruckIdGenerator() {
  }

  public static int generateId() {
    return counter.incrementAndGet();
  }

  public static void reset() {
    counter.set(0);
  }
}
