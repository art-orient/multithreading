package by.art.multithreading.reader;

import by.art.multithreading.exception.LogisticBaseException;

public interface TruckReader {
  String readTruckInfo(String filepath) throws LogisticBaseException;
}
