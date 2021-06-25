package be.mathiasbosman.cryptobot.api.entities;

import be.mathiasbosman.cryptobot.persistency.entities.LabelEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderSide implements LabelEnum {
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
