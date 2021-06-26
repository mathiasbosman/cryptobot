package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import lombok.Getter;

@Getter
public class BitvavoBuyRequest extends BitvavoOrderRequest {

  private final String amountQuote;

  public BitvavoBuyRequest(String marketCode, OrderType type, double amountQuote) {
    super(marketCode, OrderSide.BUY, type);
    this.amountQuote = String.valueOf(amountQuote);
  }
}
