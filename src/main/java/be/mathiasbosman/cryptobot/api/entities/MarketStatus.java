package be.mathiasbosman.cryptobot.api.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MarketStatus{
  TRADING("trading"),
  HALTED("halted"),
  AUCTION("auction");

  private final String label;

  MarketStatus(String status) {
    this.label = status;
  }

  public String getLabel() {
    return label;
  }

  @Override
  @JsonValue
  public String toString() {
    return label;
  }
}
