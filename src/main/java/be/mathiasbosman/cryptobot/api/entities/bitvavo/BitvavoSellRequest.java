package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import lombok.Getter;

@Getter
public class BitvavoSellRequest extends BitvavoOrderRequest {

  private final String amount;

  public BitvavoSellRequest(String marketCode, OrderType type, double amount) {
    super(marketCode, OrderSide.SELL, type);
    this.amount = String.valueOf(amount);
  }
}
