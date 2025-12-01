package by.art.multithreading;

import by.art.multithreading.entity.TruckData;
import by.art.multithreading.exception.LogisticBaseException;
import by.art.multithreading.parser.TruckParser;
import by.art.multithreading.parser.impl.TruckParserImpl;
import by.art.multithreading.reader.TruckReader;
import by.art.multithreading.reader.impl.TruckReaderImpl;

import java.util.List;

public class Main {
  private static final String FILEPATH = "data/trucks.txt";

  public static void main(String[] args) throws LogisticBaseException {
    TruckReader reader = new TruckReaderImpl();
    List<String> trucksInfo = reader.readTruckInfo(FILEPATH);
    TruckParser truckParser = new TruckParserImpl();
    List<TruckData> trucksData = truckParser.parse(trucksInfo);
  }
}