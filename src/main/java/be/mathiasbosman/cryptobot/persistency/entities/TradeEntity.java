package be.mathiasbosman.cryptobot.persistency.entities;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "trades")
public class TradeEntity implements DbEntity {

  @Id
  @GeneratedValue
  private UUID id;
  private String orderId;
  private String marketCode;
  @Enumerated(EnumType.STRING)
  private OrderType orderType;
  @Enumerated(EnumType.STRING)
  private OrderSide orderSide;
  private Double amount;
  private Double price;
  private Double feePaid;
  private boolean taker;
  private Instant timestamp;
}

