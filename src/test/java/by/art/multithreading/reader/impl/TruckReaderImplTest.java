package by.art.multithreading.reader.impl;

import by.art.multithreading.exception.LogisticsBaseException;
import by.art.multithreading.reader.TruckReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckReaderImplTest {
  private final TruckReader reader = new TruckReaderImpl();

  @Test
  void testReadTruckInfoSuccess() throws IOException, LogisticsBaseException {
    Path tempFile = Files.createTempFile("truck", ".txt");
    Files.write(tempFile, List.of("Volvo AA-111 5000 UNLOAD", "MAN BB-222 6000 LOAD"));
    List<String> result = reader.readTruckInfo(tempFile.toString());
    assertAll("Successful file read",
            () -> assertEquals(2, result.size(), "File should contain 2 records"),
            () -> assertEquals("Volvo AA-111 5000 UNLOAD", result.get(0), "First line should match"),
            () -> assertEquals("MAN BB-222 6000 LOAD", result.get(1), "Second line should match")
    );
  }

  @Test
  void testReadTruckInfoEmptyFileThrowsException() throws IOException {
    Path tempFile = Files.createTempFile("truck_empty", ".txt");
    assertThrows(LogisticsBaseException.class,
            () -> reader.readTruckInfo(tempFile.toString()),
            "Empty file should throw LogisticsBaseException");
  }

  @Test
  void testReadTruckInfoNonExistentFileThrowsException() {
    String nonExistentPath = "non_existent_file.txt";
    assertThrows(LogisticsBaseException.class,
            () -> reader.readTruckInfo(nonExistentPath),
            "Non-existent file should throw LogisticsBaseException");
  }
}