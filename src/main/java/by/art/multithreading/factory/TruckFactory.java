package by.art.multithreading.factory;

import by.art.multithreading.entity.Truck;
import by.art.multithreading.entity.TruckData;
import by.art.multithreading.exception.LogisticsBaseException;

import java.util.List;

public interface TruckFactory {
  List<Truck> createTrucks(List<TruckData> trucksData) throws LogisticsBaseException;
}
