package by.art.multithreading.reader.impl;

import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.reader.TruckReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
      List<String> trucksInfo = Files.readAllLines(path, StandardCharsets.UTF_8);
      if (trucksInfo.isEmpty()) {
        logger.warn("File {} is empty", filepath);
        throw new LogisticsBaseException("File " + filepath + " contains no truck data");
      }
      logger.info("File {} was read successfully, {} records found", filepath, trucksInfo.size());
      return trucksInfo;
    } catch (IOException e) {
      logger.error("Failed to read file {}", filepath, e);
      throw new LogisticsBaseException(String.format("Failed to read file %s", filepath), e);
    }
  }
}
