package by.art.multithreading.state.impl;

import by.art.multithreading.state.TruckState;

public final class WaitingState implements TruckState {
  @Override
  public String name() {
    return "WAITING";
  }
}
