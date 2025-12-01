package by.art.multithreading.state.impl;

import by.art.multithreading.state.TruckState;

public final class CompletedState implements TruckState {
  @Override
  public String name() {
    return "COMPLETED";
  }
}
