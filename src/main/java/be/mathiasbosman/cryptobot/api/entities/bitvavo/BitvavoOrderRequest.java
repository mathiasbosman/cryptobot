package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public abstract class BitvavoOrderRequest {

  @JsonProperty("market")
  private final String marketCode;
  private final OrderSide side;
  private final OrderType orderType;

  public BitvavoOrderRequest(String marketCode, OrderSide side, OrderType type) {
    this.marketCode = marketCode;
    this.side = side;
    this.orderType = type;
  }
}
