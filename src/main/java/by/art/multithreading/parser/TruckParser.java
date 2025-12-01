package by.art.multithreading.parser;

import by.art.multithreading.entity.TruckData;

import java.util.List;

public interface TruckParser {
  List<TruckData> parse(List<String> trucksInfo);
}
