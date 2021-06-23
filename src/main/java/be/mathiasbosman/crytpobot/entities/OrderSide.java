package be.mathiasbosman.crytpobot.entities;

import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderSide {
  BUY("buy"),
  SELL("sell");

  private final String label;

  OrderSide(String label) {
    this.label = label;
  }

  public String getLabel() {
    return label;
  }

  @Override
  @JsonValue
  public String toString() {
    return getLabel();
  }
}
