package by.art.multithreading.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TerminalTest {
  Terminal terminal;

  @BeforeEach
  void setUp() {
    terminal = new Terminal(5);
  }

  @AfterEach
  void tearDown() {
    terminal = null;
  }

  @Test
  void testGetId() {
    assertEquals(5, terminal.getId(), "Terminal ID should match the constructor value");
  }

  @Test
  void testOccupyTerminal() {
    boolean occupied = terminal.occupyTerminal();
    assertAll("Occupy terminal",
            () -> assertTrue(occupied, "First attempt to occupy should succeed"),
            () -> assertTrue(terminal.isOccupied(), "Terminal should be marked as occupied")
    );
  }

  @Test
  void testReleaseTerminal() {
    terminal.occupyTerminal();
    terminal.releaseTerminal();
    assertFalse(terminal.isOccupied(), "Terminal should be released and not occupied");
  }

  @Test
  void testOccupyTwice() {
    boolean first = terminal.occupyTerminal();
    boolean second = terminal.occupyTerminal();
    assertAll("Occupy twice",
            () -> assertTrue(first, "First attempt should succeed"),
            () -> assertTrue(second, "Second attempt should also succeed because lock is reentrant"),
            () -> assertTrue(terminal.isOccupied(), "Terminal should remain occupied after second attempt")
    );
  }

  @Test
  void testReleaseWithoutOccupyThrowsException() {
    assertThrows(IllegalMonitorStateException.class, terminal::releaseTerminal,
            "Releasing without occupying should throw IllegalMonitorStateException");
  }
}