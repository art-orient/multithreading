package by.art.multithreading.parser.impl;

import by.art.multithreading.entity.TruckData;
import by.art.multithreading.parser.TruckParser;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TruckParserImplTest {
  private final TruckParser parser = new TruckParserImpl();

  @Test
  void testParseValidTruckInfo() {
    List<String> input = List.of("Volvo, AA-111, 5000, 2000, 0, UNLOAD, true");
    List<TruckData> result = parser.parse(input);
    assertAll("Valid truck parsing",
            () -> assertEquals(1, result.size(), "One truck should be parsed"),
            () -> assertEquals("Volvo", result.get(0).brand(), "Brand should match"),
            () -> assertEquals("AA-111", result.get(0).plateNumber(), "Plate number should match"),
            () -> assertEquals(5000, result.get(0).truckCapacity(), "Capacity should match"),
            () -> assertEquals(2000, result.get(0).cargoUnload(), "Unload should match"),
            () -> assertEquals(0, result.get(0).cargoLoad(), "Load should match"),
            () -> assertEquals("UNLOAD", result.get(0).operation(), "Operation should match"),
            () -> assertTrue(result.get(0).perishable(), "Perishable should be true")
    );
  }

  @Test
  void testParseInvalidFormatIgnored() {
    List<String> input = List.of("Volvo, AA-111, 5000, 2000, UNLOAD");
    List<TruckData> result = parser.parse(input);
    assertTrue(result.isEmpty(), "Invalid format should be ignored");
  }

  @Test
  void testParseMultipleLinesMixedValidity() {
    List<String> input = List.of(
            "Scania, CC-333, 7000, 1500, 500, UNLOAD_LOAD, true",
            "InvalidLineWithoutEnoughParts",
            "DAF, DD-444, 6000, 1000, 2000, LOAD, false"
    );
    List<TruckData> result = parser.parse(input);
    assertAll("Mixed validity parsing",
            () -> assertEquals(2, result.size(), "Two valid trucks should be parsed"),
            () -> assertEquals("Scania", result.get(0).brand(), "First truck brand should match"),
            () -> assertEquals("DAF", result.get(1).brand(), "Second truck brand should match")
    );
  }

  @Test
  void testParseEmptyInputReturnsEmptyList() {
    List<String> input = List.of();
    List<TruckData> result = parser.parse(input);
    assertTrue(result.isEmpty(), "Empty input should return empty list");
  }
}