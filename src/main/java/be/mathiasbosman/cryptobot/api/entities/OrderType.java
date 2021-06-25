package be.mathiasbosman.cryptobot.api.entities;

import be.mathiasbosman.cryptobot.persistency.entities.LabelEnum;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrderType implements LabelEnum {
  MARKET("market"),
  LIMIT("limit"),
  STOP_LOSS("stopLoss"),
  STOP_LOSS_LIMIT("stopLossLimit"),
  TAKE_PROFIT("takeProfit"),
  TAKE_PROFIT_LIMIT("takeProfitLimit");

  private final String label;

  OrderType(String label) {
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
