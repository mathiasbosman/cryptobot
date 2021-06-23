package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.Market;
import be.mathiasbosman.crytpobot.entities.OrderSide;
import be.mathiasbosman.crytpobot.entities.OrderType;
import lombok.Getter;

@Getter
public class BitvavoOrderRequest {
  private final String market;
  private final OrderSide side;
  private final OrderType orderType;
  private final String amount;

  public BitvavoOrderRequest(Market market, OrderSide side, OrderType type, double amount) {
    this.market = market.getCode();
    this.side = side;
    this.orderType = type;
    this.amount = String.valueOf(amount);
  }
}
