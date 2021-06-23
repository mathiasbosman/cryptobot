package be.mathiasbosman.crytpobot.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum MarketStatus{
  TRADING("trading"),
  HALTED("halted"),
  AUCTION("auction");

  private final String status;

  MarketStatus(String status) {
    this.status = status;
  }

  public String getStatus() {
    return status;
  }

  @Override
  @JsonValue
  public String toString() {
    return status;
  }
}
