package be.mathiasbosman.cryptobot.services;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import be.mathiasbosman.cryptobot.api.entities.OrderSide;
import be.mathiasbosman.cryptobot.fxtures.TradeEntityFixture;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class BitvavoServiceTest {

  private final BitvavoService service = new BitvavoService(null, null, null);

  @Test
  void hasProfit() {
    assertThat(service.hasProfit(10, 4, -30, 1, 5)).isTrue();
    assertThat(service.hasProfit(10, 4, -30, 1, 50)).isFalse();
  }

  @Test
  void getCurrentValue() {
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-496);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY)
    ))).isEqualTo(-992);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.SELL)
    ))).isEqualTo(0);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(100);
    assertThat(service.getCurrentValue(List.of(
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(50, 10, 4, OrderSide.BUY),
        TradeEntityFixture.newTradeEntity(60, 10, 4, OrderSide.SELL)
    ))).isEqualTo(-396);
  }

  @Test
  void getFee() {
    assertThat(service.getFee(100, 0.5, 0.0025)).isEqualTo(0.13);
  }
}