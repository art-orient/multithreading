package by.art.multithreading.parser.impl;

import by.art.multithreading.entity.TruckData;
import by.art.multithreading.parser.TruckParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class TruckParserImpl implements TruckParser {
  private static final Logger logger = LogManager.getLogger();
  @Override
  public List<TruckData> parse(List<String> trucksInfo) {
    List<TruckData> trucksData = new ArrayList<>();
    for (String truckInfo : trucksInfo) {
      String[] parts = truckInfo.split(",");
      String brand = parts[0].strip();
      String plateNumber = parts[1].strip();
      int truckCapacity = Integer.parseInt(parts[2].strip());
      int cargoUnload = Integer.parseInt(parts[3].strip());
      int cargoLoad = Integer.parseInt(parts[4].strip());
      String operation = parts[5].strip();
      boolean isPerishable = Boolean.parseBoolean(parts[6].strip());
      TruckData truckData = new TruckData(brand, plateNumber, truckCapacity, cargoUnload, cargoLoad,
              operation, isPerishable);
      trucksData.add(truckData);
      logger.info("Added truck: {}", truckData);
    }
    return trucksData;
  }
}
