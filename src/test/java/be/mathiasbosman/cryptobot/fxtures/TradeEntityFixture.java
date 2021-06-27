package be.mathiasbosman.cryptobot.fxtures;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.api.entities.OrderType;
import be.mathiasbosman.cryptobot.persistency.entities.TradeEntity;
import java.time.Instant;
import java.util.UUID;

public abstract class TradeEntityFixture {

  TradeEntityFixture() {
    // fixture
  }

  public static TradeEntity newTradeEntity(double price, double amount, double feePaid,
      OrderSide side) {
    TradeEntity tradeEntity = new TradeEntity();
    tradeEntity.setTaker(true);
    tradeEntity.setPrice(price);
    tradeEntity.setAmount(amount);
    tradeEntity.setOrderSide(side);
    tradeEntity.setFeePaid(feePaid);
    tradeEntity.setId(UUID.randomUUID());
    tradeEntity.setMarketCode("ABC-DEF");
    tradeEntity.setTimestamp(Instant.now());
    tradeEntity.setOrderType(OrderType.MARKET);
    tradeEntity.setOrderId(UUID.randomUUID().toString());
    return tradeEntity;
  }

}
