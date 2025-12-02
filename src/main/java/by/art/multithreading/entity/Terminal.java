package by.art.multithreading.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public class Terminal {
  private static final Logger logger = LogManager.getLogger();
  private final int id;
  private final ReentrantLock lock = new ReentrantLock();

  public Terminal(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public boolean occupyTerminal() {
    return lock.tryLock();
  }

  public void releaseTerminal() {
    lock.unlock();
  }

  public boolean isOccupied() {
    return lock.isLocked();
  }
}
