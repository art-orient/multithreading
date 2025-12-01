package by.art.multithreading.reader.impl;

import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.reader.TruckReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TruckReaderImpl implements TruckReader {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public List<String> readTruckInfo(String filepath) throws LogisticsBaseException {
    Path path = Paths.get(filepath);
    try {
      List<String> trucksInfo = Files.readAllLines(path);
      logger.info("File {} was read successfully", filepath);
      return trucksInfo;
    } catch (IOException e) {
      logger.error("Failed to read file {}", filepath);
      throw new LogisticsBaseException(String.format("Failed to read file %s", filepath), e);
    }
  }
}
