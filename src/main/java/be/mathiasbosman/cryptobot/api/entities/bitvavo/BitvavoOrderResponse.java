package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.Order;
import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderStatus;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoOrderResponse implements Order {

  @JsonProperty("orderId")
  private String id;
  @JsonProperty("market")
  private String marketCode;
  @JsonProperty("orderStatus")
  private OrderStatus status;
  @JsonProperty("orderType")
  private OrderType type;
  private OrderSide side;
  private double amount;
  private double filledAmount;
  private double price;
  private double feePaid;
  private Instant created;
  private Instant updated;
}
