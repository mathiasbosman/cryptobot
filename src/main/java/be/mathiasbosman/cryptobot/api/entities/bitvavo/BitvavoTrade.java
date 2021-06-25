package be.mathiasbosman.cryptobot.api.entities.bitvavo;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.Trade;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BitvavoTrade implements Trade {

  private String id;
  private String orderId;
  private OrderSide side;
  private double amount;
  private double price;
  private boolean taker;
  private double fee;
  private String feeCurrency;
  private boolean settled;
  private Instant timestamp;
  @JsonProperty("market")
  private String marketCode;
}
