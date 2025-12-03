package by.art.multithreading.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class TruckIdGeneratorTest {

  @BeforeEach
  void setUp() {
    TruckIdGenerator.reset();
  }

  @Test
  void testFirstGeneratedIdIsOne() {
    int id = TruckIdGenerator.generateId();
    assertEquals(1, id, "First generated ID should be 1");
  }

  @Test
  void testIdsIncrementSequentially() {
    int id1 = TruckIdGenerator.generateId();
    int id2 = TruckIdGenerator.generateId();
    int id3 = TruckIdGenerator.generateId();
    assertAll("Sequential ID generation",
            () -> assertEquals(id1 + 1, id2, "Second ID should be first + 1"),
            () -> assertEquals(id2 + 1, id3, "Third ID should be second + 1")
    );
  }

  @Test
  void testIdsAreUniqueAcrossMultipleCalls() {
    Set<Integer> ids = new HashSet<>();
    for (int i = 0; i < 100; i++) {
      ids.add(TruckIdGenerator.generateId());
    }
    assertEquals(100, ids.size(), "All generated IDs should be unique");
  }

  @Test
  void testThreadSafety() throws InterruptedException {
    int threads = 20;
    int idsPerThread = 50;
    Set<Integer> ids = new HashSet<>();
    CountDownLatch latch = new CountDownLatch(threads);

    Runnable task = () -> {
      for (int i = 0; i < idsPerThread; i++) {
        synchronized (ids) {
          ids.add(TruckIdGenerator.generateId());
        }
      }
      latch.countDown();
    };
    for (int i = 0; i < threads; i++) {
      new Thread(task).start();
    }
    latch.await();
    assertEquals(threads * idsPerThread, ids.size(),
            "All IDs generated in parallel should be unique");
  }
}