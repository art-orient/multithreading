package by.art.multithreading;

import by.art.multithreading.exception.LogisticBaseException;
import by.art.multithreading.reader.TruckReader;
import by.art.multithreading.reader.impl.TruckReaderImpl;

public class Main {
  private static final String FILEPATH = "data/trucks.txt";

  public static void main(String[] args) throws LogisticBaseException {
    TruckReader reader = new TruckReaderImpl();
    String trucksInfo = reader.readTruckInfo(FILEPATH);
    System.out.println(trucksInfo);
  }
}