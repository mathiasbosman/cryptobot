package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.Assertions.assertThat;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.fixtures.TradeEntityFixture;
import java.util.List;
import org.junit.jupiter.api.Test;

class TradeServiceTest {

  private final TradeService service = new TradeService(null);

  @Test
  void calculateCurrentValue() {
    assertThat(service.calculateCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-504);
    assertThat(service.calculateCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-1008);
    assertThat(service.calculateCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.SELL)
    ))).isEqualTo(-8);
    assertThat(service.calculateCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(92);
    assertThat(service.calculateCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(-412);
  }
}
