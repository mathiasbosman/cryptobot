package be.mathiasbosman.crytpobot.entities.bitvavo;

import be.mathiasbosman.crytpobot.entities.Order;
import be.mathiasbosman.crytpobot.entities.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoOrderResponse implements Order {
  @JsonProperty("orderId")
  private String id;
  @JsonProperty("market")
  private String marketCode;
  private int created;
  private int updated;
  @JsonProperty("orderStatus")
  private OrderStatus status;
}
