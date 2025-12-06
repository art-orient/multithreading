package by.art.multithreading.entity;

import java.util.Comparator;

public class TruckPerishableComparator implements Comparator<Truck> {
  @Override
  public int compare(Truck t1, Truck t2) {
    return Boolean.compare(t2.isPerishable(), t1.isPerishable());
  }
}
