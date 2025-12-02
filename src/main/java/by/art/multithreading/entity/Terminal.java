package by.art.multithreading.entity;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Terminal {
  private final int id;
  private final Lock lock = new ReentrantLock();

  public Terminal(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }

  public void occupyTerminal() {
    lock.lock();
  }

  public void releaseTerminal() {
    lock.unlock();
  }
}
