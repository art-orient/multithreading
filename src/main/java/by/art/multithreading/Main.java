package by.art.multithreading;

import by.art.multithreading.entity.LogisticsBase;
import by.art.multithreading.entity.Truck;
import by.art.multithreading.entity.TruckData;
import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.factory.TruckFactory;
import by.art.multithreading.factory.impl.TruckFactoryImpl;
import by.art.multithreading.parser.TruckParser;
import by.art.multithreading.parser.impl.TruckParserImpl;
import by.art.multithreading.reader.TruckReader;
import by.art.multithreading.reader.impl.TruckReaderImpl;

import java.util.List;

public class Main {
  private static final String FILEPATH = "data/trucks.txt";

  public static void main(String[] args) throws LogisticsBaseException {
    TruckReader reader = new TruckReaderImpl();
    List<String> trucksInfo = reader.readTruckInfo(FILEPATH);
    TruckParser truckParser = new TruckParserImpl();
    List<TruckData> trucksData = truckParser.parse(trucksInfo);
    TruckFactory factory = new TruckFactoryImpl();
    List<Truck> trucks = factory.createTrucks(trucksData);
    LogisticsBase base = LogisticsBase.getInstance();
    base.dispatchTrucks(trucks);
  }
}