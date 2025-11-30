package by.art.multithreading.reader.impl;

import by.art.multithreading.exception.LogisticBaseException;
import by.art.multithreading.reader.TruckReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TruckReaderImpl implements TruckReader {
  private static final Logger logger = LogManager.getLogger();

  @Override
  public String readTruckInfo(String filepath) throws LogisticBaseException {
    String fullText;
    Path path = Paths.get(filepath);
    try {
      fullText = Files.readString(path);
      logger.info("File {} was read successfully", filepath);
    } catch (IOException e) {
      logger.error("Failed to read file {}", filepath);
      throw new LogisticBaseException(String.format("Failed to read file %s", filepath), e);
    }
    return fullText;
  }
}
