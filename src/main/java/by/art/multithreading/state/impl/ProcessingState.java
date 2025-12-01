package by.art.multithreading.state.impl;

import by.art.multithreading.state.TruckState;

public final class ProcessingState implements TruckState {
  @Override
  public String name() {
    return "PROCESSING";
  }
}
