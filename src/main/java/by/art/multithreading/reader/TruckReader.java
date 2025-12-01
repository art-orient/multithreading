package by.art.multithreading.reader;

import by.art.multithreading.exception.LogisticBaseException;

import java.util.List;

public interface TruckReader {
  List<String> readTruckInfo(String filepath) throws LogisticBaseException;
}
